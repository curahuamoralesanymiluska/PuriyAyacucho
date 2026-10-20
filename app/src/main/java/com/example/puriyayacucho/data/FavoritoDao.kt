package com.example.puriyayacucho.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoritoDao {
    @Insert
    suspend fun insertar(favorito: FavoritoEntity)

    @Delete
    suspend fun eliminar(favorito: FavoritoEntity)

    @Query("SELECT * FROM favoritos WHERE usuarioCorreo = :correo")
    suspend fun obtenerPorUsuario(correo: String): List<FavoritoEntity>

    @Query("SELECT * FROM favoritos WHERE usuarioCorreo = :correo AND lugarId = :lugarId LIMIT 1")
    suspend fun buscarFavorito(correo: String, lugarId: Int): FavoritoEntity?

    @Query("DELETE FROM favoritos WHERE usuarioCorreo = :correo AND lugarId = :lugarId")
    suspend fun eliminarPorId(correo: String, lugarId: Int)
}
