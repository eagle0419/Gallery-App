package com.dot.gallery.feature_node.data.data_types

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.database.MergeCursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.dot.gallery.feature_node.data.data_source.Query
import com.dot.gallery.feature_node.data.data_source.Query.MediaQuery
import com.dot.gallery.feature_node.domain.model.Media
import java.io.File
import kotlin.jvm.Throws

fun ContentResolver.query(
    mediaQuery: Query
): Cursor {
    return MergeCursor(
        arrayOf(
            query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                mediaQuery.projection,
                mediaQuery.bundle,
                null
            ),
            query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaQuery.projection,
                mediaQuery.bundle,
                null
            )
        )
    )
}

fun ContentResolver.getMediaUri(media: Media): Uri? {
    val mediaQuery = MediaQuery().copy(
        bundle = Bundle().apply {
            putInt(
                MediaStore.QUERY_ARG_MATCH_TRASHED,
                MediaStore.MATCH_ONLY
            )
            putString(
                ContentResolver.QUERY_ARG_SQL_SELECTION,
                MediaStore.MediaColumns.DATA + "= ?"
            )
            putStringArray(
                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                arrayOf(media.path)
            )
        }
    )
    val cursor = query(mediaQuery)
    val uri = if (cursor.moveToFirst()) {
        val isImage =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))
                .contains("image/")
        val contentUri =
            if (isImage) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ContentUris.withAppendedId(
            contentUri,
            cursor.getLong(cursor.getColumnIndexOrThrow(mediaQuery.projection.first()))
        )
    } else null
    cursor.close()
    return uri
}


@Throws(Exception::class)
fun Cursor.getMediaFromCursor(): Media {
    val id: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
    val path: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
    val title: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
    val albumID: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID))
    val albumLabel: String = try {
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME))
    } catch (_: Exception) {
        Build.MODEL
    }
    val timestamp: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
    val duration: String? = try {
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION))
    } catch (_: Exception) {
        null
    }
    val orientation: Int =
        getInt(getColumnIndexOrThrow(MediaStore.MediaColumns.ORIENTATION))
    val mimeType: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))
    val isFavorite: Int =
        getInt(getColumnIndexOrThrow(MediaStore.MediaColumns.IS_FAVORITE))
    val isTrashed: Int =
        getInt(getColumnIndexOrThrow(MediaStore.MediaColumns.IS_TRASHED))
    return Media(
        id = id,
        label = title,
        uri = Uri.fromFile(File(path)),
        path = path,
        albumID = albumID,
        albumLabel = albumLabel,
        timestamp = timestamp,
        duration = duration,
        favorite = isFavorite,
        trashed = isTrashed,
        orientation = orientation,
        mimeType = mimeType
    )
}