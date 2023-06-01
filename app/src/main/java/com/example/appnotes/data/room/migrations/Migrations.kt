package com.example.appnotes.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        val newTable = "NewNoteModel"
        val table = "NoteModel"

        //criar tabela nova com todos os campos esperados
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS $newTable(" +
                    "`id` TEXT PRIMARY KEY NOT NULL, " +
                    "`titulo` TEXT NOT NULL, " +
                    "`descricao` TEXT NOT NULL," +
                    "`imagem` TEXT)"
        )

        //copiar dados da tabela atual para a tabela nova
        database.execSQL(
            "INSERT INTO $newTable (id, titulo, descricao, imagem) " +
                    "SELECT id, titulo, descricao, imagem FROM $table"
        )

        //gerar ids diferentes e novos
        val cursor = database.query("SELECT * FROM $newTable")
        while (cursor.moveToNext()) {
            val id = cursor.getString(
                cursor.getColumnIndex("id")
            )
            database.execSQL("UPDATE $newTable SET id = '${UUID.randomUUID()}' WHERE id = $id")
        }

        //apagar tabela atual
        database.execSQL("DROP TABLE $table")

        //renomear tabela nova com o nome da tabela atual
        database.execSQL("ALTER TABLE $newTable RENAME TO $table")
    }
}

val MIGRATION_2_3 = object : Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE NoteModel ADD synced INTEGER NOT NULL DEFAULT = 0")
    }
}

val MIGRATION_3_4 = object : Migration(3,4){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE NoteModel ADD disable INTEGER NOT NULL DEFAULT = 0")
    }
}