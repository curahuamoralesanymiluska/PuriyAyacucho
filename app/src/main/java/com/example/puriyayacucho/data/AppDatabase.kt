package com.example.puriyayacucho.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UsuarioEntity::class,
        FavoritoEntity::class,
        ReservaEntity::class,
        BoletoEntity::class,
        RecorridoEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun favoritoDao(): FavoritoDao
    abstract fun reservaDao(): ReservaDao
    abstract fun boletoDao(): BoletoDao
    abstract fun recorridoDao(): RecorridoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * v5 → v6: agrega las tablas de boletos y recorridos SIN borrar
         * los usuarios, favoritos ni reservas que ya existen.
         */
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `boletos` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`codigoUuid` TEXT NOT NULL, " +
                        "`usuarioCorreo` TEXT NOT NULL, " +
                        "`eventoId` INTEGER NOT NULL, " +
                        "`nombreEvento` TEXT NOT NULL, " +
                        "`fecha` TEXT NOT NULL, " +
                        "`hora` TEXT NOT NULL, " +
                        "`lugar` TEXT NOT NULL, " +
                        "`tipoEntrada` TEXT NOT NULL, " +
                        "`cantidad` INTEGER NOT NULL, " +
                        "`total` REAL NOT NULL, " +
                        "`estado` TEXT NOT NULL, " +
                        "`fechaCompra` TEXT NOT NULL)"
                )
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `recorridos` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`usuarioCorreo` TEXT NOT NULL, " +
                        "`titulo` TEXT NOT NULL, " +
                        "`modo` TEXT NOT NULL, " +
                        "`waypointsJson` TEXT NOT NULL, " +
                        "`fecha` TEXT NOT NULL)"
                )
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `reservas` ADD COLUMN `numPersonas` INTEGER NOT NULL DEFAULT 1")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "puriy_ayacucho.db"
                )
                .addMigrations(MIGRATION_5_6, MIGRATION_6_7)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
