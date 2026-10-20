package com.example.puriyayacucho

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : BaseActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var btnLogin: MaterialButton
    private val TAG = "LoginActivity"

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)
                if (account?.idToken != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error Google: ${e.message}")
                Toast.makeText(this, R.string.login_social_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Facebook todavía entrega algunos resultados por aquí")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (::callbackManager.isInitialized) callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_login)
            ajustarBordes()

            firebaseAuth = FirebaseAuth.getInstance()
            callbackManager = CallbackManager.Factory.create()

            setupGoogleSignIn()
            setupFacebook()

            btnLogin = findViewById(R.id.btnLogin)
            btnLogin.setOnClickListener { loginWithEmail() }
            findViewById<Button>(R.id.btnGoogleLogin).setOnClickListener {
                googleSignInLauncher.launch(googleSignInClient.signInIntent)
            }
            findViewById<Button>(R.id.btnFacebookLogin).setOnClickListener { loginConFacebook() }
            findViewById<TextView>(R.id.tvOlvideContrasena).setOnClickListener { recuperarContrasena() }
            findViewById<TextView>(R.id.tvGuestLogin).setOnClickListener { loginAsGuest() }
            findViewById<TextView>(R.id.tvGoToRegister).setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error onCreate: ${e.message}")
        }
    }

    /** El fondo rosa llega hasta la barra de estado; la hoja blanca llega hasta abajo. */
    private fun ajustarBordes() {
        val header = findViewById<View>(R.id.headerLogin)
        val hoja = findViewById<View>(R.id.hojaLogin)
        val arribaOriginal = header.paddingTop
        val abajoOriginal = hoja.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            header.setPadding(header.paddingLeft, arribaOriginal + barras.top, header.paddingRight, header.paddingBottom)
            hoja.setPadding(hoja.paddingLeft, hoja.paddingTop, hoja.paddingRight, abajoOriginal + barras.bottom)
            insets
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                saveUserAndNavigate(firebaseAuth.currentUser?.displayName ?: "Usuario", firebaseAuth.currentUser?.email ?: "")
            } else {
                Toast.makeText(this, R.string.login_social_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ---------- Facebook ----------

    private fun setupFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                firebaseAuthWithFacebook(result.accessToken.token)
            }

            override fun onCancel() = Unit

            override fun onError(error: FacebookException) {
                Log.e(TAG, "Facebook: ${error.message}")
                Toast.makeText(this@LoginActivity, R.string.login_facebook_error, Toast.LENGTH_LONG).show()
            }
        })
    }

    /** true si en local.properties ya están el App ID y el Client Token reales. */
    private fun facebookConfigurado(): Boolean {
        val appId = getString(R.string.facebook_app_id)
        val token = getString(R.string.facebook_client_token)
        return appId.all { it.isDigit() } && appId.length >= 10 && appId.any { it != '0' } &&
            token.length >= 20 && !token.contains("TU_", ignoreCase = true)
    }

    private fun loginConFacebook() {
        if (!facebookConfigurado()) {
            AlertDialog.Builder(this)
                .setTitle(R.string.login_facebook_not_ready_title)
                .setMessage(R.string.login_facebook_not_ready_message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
            return
        }
        try {
            LoginManager.getInstance().logInWithReadPermissions(this, callbackManager, listOf("email", "public_profile"))
        } catch (e: Exception) {
            Log.e(TAG, "Facebook: ${e.message}")
            Toast.makeText(this, R.string.login_facebook_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithFacebook(token: String) {
        mostrarCargando(true)
        val credential = FacebookAuthProvider.getCredential(token)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            mostrarCargando(false)
            if (task.isSuccessful) {
                val usuario = firebaseAuth.currentUser
                saveUserAndNavigate(usuario?.displayName ?: "", usuario?.email ?: "")
            } else {
                Log.e(TAG, "Firebase + Facebook: ${task.exception?.message}")
                val mensaje = if (task.exception is FirebaseAuthUserCollisionException) R.string.login_account_exists
                else R.string.login_facebook_error
                LoginManager.getInstance().logOut()
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
            }
        }
    }

    // ---------- Correo y contraseña ----------

    private fun loginWithEmail() {
        val etCorreo = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val correo = etCorreo.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.login_fill_fields, Toast.LENGTH_SHORT).show()
            return
        }

        mostrarCargando(true)
        firebaseAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener { task ->
            mostrarCargando(false)
            if (task.isSuccessful) {
                saveUserAndNavigate("", correo)
            } else {
                val mensaje = when (task.exception) {
                    is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> R.string.login_wrong_credentials
                    else -> R.string.error
                }
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recuperarContrasena() {
        val correo = findViewById<TextInputEditText>(R.id.etEmail).text.toString().trim()
        if (correo.isEmpty()) {
            Toast.makeText(this, R.string.login_forgot_need_email, Toast.LENGTH_SHORT).show()
            return
        }
        firebaseAuth.sendPasswordResetEmail(correo).addOnCompleteListener { task ->
            Toast.makeText(
                this,
                if (task.isSuccessful) getString(R.string.login_reset_sent, correo) else getString(R.string.error),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun mostrarCargando(cargando: Boolean) {
        if (!::btnLogin.isInitialized) return
        btnLogin.isEnabled = !cargando
        btnLogin.setText(if (cargando) R.string.login_loading else R.string.btn_login)
    }

    private fun loginAsGuest() {
        val sharedPref = getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("nombre_usuario", getString(R.string.guest))
            putString("correo_usuario", "invitado@puriy.com")
            apply()
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun saveUserAndNavigate(nombre: String, email: String) {
        val sharedPref = getSharedPreferences("usuario_sesion", Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("nombre_usuario", if (nombre.isEmpty()) "Usuario" else nombre)
            putString("correo_usuario", email)
            apply()
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
