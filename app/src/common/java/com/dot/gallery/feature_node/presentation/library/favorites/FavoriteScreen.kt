/*
 * SPDX-FileCopyrightText: 2023 IacobIacob01
 * SPDX-License-Identifier: Apache-2.0
 */

package com.dot.gallery.feature_node.presentation.library.favorites

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dot.gallery.R
import com.dot.gallery.feature_node.presentation.MediaScreen
import com.dot.gallery.feature_node.domain.model.Media
import com.dot.gallery.feature_node.presentation.MediaViewModel
import com.dot.gallery.feature_node.presentation.library.favorites.components.EmptyFavorites
import com.dot.gallery.feature_node.presentation.library.favorites.components.FavoriteNavActions

@Composable
fun FavoriteScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    albumName: String = stringResource(id = R.string.favorites),
    viewModel: MediaViewModel,
) = MediaScreen(
    navController = navController,
    paddingValues = paddingValues,
    albumName = albumName,
    viewModel = viewModel,
    NavActions = { _: MutableState<Boolean>,
                   selectedMedia: SnapshotStateList<Media>,
                   selectionState: MutableState<Boolean>,
                   result: ActivityResultLauncher<IntentSenderRequest> ->
        FavoriteNavActions(viewModel, selectedMedia, selectionState, result)
    },
    EmptyComponent = { EmptyFavorites(Modifier.fillMaxSize()) },
    onActivityResult = { selectedMedia, selectionState, result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedMedia.clear()
            selectionState.value = false
        }
    }
)