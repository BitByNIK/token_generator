package com.jcs242611.producer

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class ProvideTokens : ContentProvider() {
    companion object {
        private const val AUTHORITY = "com.jcs242611.producer.provider"
        const val TABLE_NAME = "TokenRepository"
        const val IS_MATCH = 1
        val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, IS_MATCH)
        }
    }

    private lateinit var database: ProducerDatabase

    override fun onCreate(): Boolean {
        database = ProducerDatabase.getDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (URI_MATCHER.match(uri)) {
            IS_MATCH -> {
                database.tokenDao().getLatestTokens()
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.dir/$TABLE_NAME"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Insert not allowed. Read-only provider.")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Delete not allowed. Read-only provider.")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Update not allowed. Read-only provider.")
    }
}