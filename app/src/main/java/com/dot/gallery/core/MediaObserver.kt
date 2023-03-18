package com.dot.gallery.core

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

/**
 * Register an observer class that gets callbacks when data identified by a given content URI
 * changes.
 */
fun ContentResolver.contentFlowObserver(uri: Uri) = callbackFlow {
    val observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            trySend(selfChange)
        }

        override fun onChange(selfChange: Boolean) {
            onChange(selfChange, null)
        }
    }
    registerContentObserver(uri, true, observer)
    // trigger first.
    trySend(false)
    awaitClose {
        unregisterContentObserver(observer)
    }
}