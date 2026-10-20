package com.example.puriyayacucho.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

/** Un boleto comprado por el usuario. */
@Entity(tableName = "boletos")
data class BoletoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    /** Código único que va dentro del QR. */
    val codigoUuid: String,
    val usuarioCorreo: String,
    val eventoId: Int,
    val nombreEvento: String,
    val fecha: String,
    val hora: String,
    val lugar: String,
    val tipoEntrada: String,
    val cantidad: Int,
    val total: Double,
    val estado: String = "PAGADO", // PAGADO, USADO, CANCELADO
    val fechaCompra: String
)

@Dao
interface BoletoDao {
    @Query("SELECT * FROM boletos WHERE usuarioCorreo = :correo ORDER BY id DESC")
    suspend fun obtenerBoletos(correo: String): List<BoletoEntity>

    @Query("SELECT * FROM boletos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): BoletoEntity?

    @Insert
    suspend fun insertar(boleto: BoletoEntity): Long
}
