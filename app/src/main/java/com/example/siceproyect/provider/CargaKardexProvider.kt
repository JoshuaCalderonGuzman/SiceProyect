package com.example.siceproyect.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.siceproyect.data.local.AppDatabaseProvider
import androidx.sqlite.db.SimpleSQLiteQuery

class CargaKardexProvider : ContentProvider() {

    private lateinit var db: AppDatabaseProvider

    companion object {

        const val AUTHORITY = "com.example.siceproyect.provider"

        val URI_CARGA = Uri.parse("content://$AUTHORITY/carga")
        val URI_KARDEX = Uri.parse("content://$AUTHORITY/kardex")

        private const val CARGA = 1
        private const val KARDEX = 2

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "carga", CARGA)
            addURI(AUTHORITY, "kardex", KARDEX)
        }
    }

    override fun onCreate(): Boolean {
        Log.d("ProviderR", "Provider creado")
        db = AppDatabaseProvider
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d("ProviderR", "Query ejecutada")

        val database = db.get(context!!).openHelper.readableDatabase

        val table = when (uriMatcher.match(uri)) {
            CARGA -> "carga"
            KARDEX -> "kardex"
            else -> throw IllegalArgumentException("URI no soportada")
        }

        val columns = projection?.joinToString(", ") ?: "*"

        val queryBuilder = StringBuilder("SELECT $columns FROM $table")

        if (!selection.isNullOrEmpty()) {
            queryBuilder.append(" WHERE $selection")
        }

        if (!sortOrder.isNullOrEmpty()) {
            queryBuilder.append(" ORDER BY $sortOrder")
        }

        val query = SimpleSQLiteQuery(queryBuilder.toString(), selectionArgs)

        return database.query(query)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val database = db.get(context!!).openHelper.writableDatabase

        val id = when (uriMatcher.match(uri)) {

            CARGA -> database.insert("carga", 0, values ?: ContentValues())

            KARDEX -> database.insert("kardex", 0, values ?: ContentValues())

            else -> throw IllegalArgumentException("URI no válida")
        }

        return Uri.withAppendedPath(uri, id.toString())
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {

        val database = db.get(context!!).openHelper.writableDatabase

        return when (uriMatcher.match(uri)) {

            CARGA -> database.delete("carga", selection, selectionArgs)

            KARDEX -> database.delete("kardex", selection, selectionArgs)

            else -> 0
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {

        val database = db.get(context!!).openHelper.writableDatabase

        return when (uriMatcher.match(uri)) {

            CARGA -> database.update(
                "carga",
                0,
                values ?: ContentValues(),
                selection,
                selectionArgs
            )

            KARDEX -> database.update(
                "kardex",
                0,
                values ?: ContentValues(),
                selection,
                selectionArgs
            )

            else -> 0
        }
    }

    override fun getType(uri: Uri): String? = null
}