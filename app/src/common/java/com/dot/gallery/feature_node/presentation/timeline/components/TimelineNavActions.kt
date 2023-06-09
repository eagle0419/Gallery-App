package com.dot.gallery.feature_node.presentation.timeline.components

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.dot.gallery.R
import com.dot.gallery.core.Constants
import com.dot.gallery.feature_node.domain.model.Media
import com.dot.gallery.feature_node.presentation.MediaViewModel
import com.dot.gallery.feature_node.presentation.util.Screen
import com.dot.gallery.feature_node.presentation.util.shareMedia
import kotlinx.coroutines.launch

@Composable
fun RowScope.TimelineNavActions(
    viewModel: MediaViewModel,
    expandedDropDown: MutableState<Boolean>,
    selectedMedia: SnapshotStateList<Media>,
    selectionState: MutableState<Boolean>,
    navController: NavController,
    result: ActivityResultLauncher<IntentSenderRequest>
) {
    val shareMedia = stringResource(id = R.string.share_media)
    val trashMedia = stringResource(R.string.trash)
    val context = LocalContext.current
    val state by remember { viewModel.photoState }
    val albumId = remember { viewModel.albumId }
    val scope = viewModel.viewModelScope
    AnimatedVisibility(
        visible = selectionState.value,
        enter = Constants.Animation.enterAnimation,
        exit = Constants.Animation.exitAnimation
    ) {
        Row {
            IconButton(
                onClick = {
                    scope.launch {
                        context.shareMedia(selectedMedia)
                        selectionState.value = false
                        selectedMedia.clear()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = shareMedia
                )
            }
            IconButton(
                onClick = {
                    scope.launch {
                        viewModel.handler.toggleFavorite(
                            result,
                            selectedMedia
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = trashMedia
                )
            }
            IconButton(
                onClick = {
                    scope.launch {
                        viewModel.handler.trashMedia(
                            result,
                            selectedMedia
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = trashMedia
                )
            }
        }
    }
    IconButton(onClick = { expandedDropDown.value = !expandedDropDown.value }) {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = stringResource(R.string.drop_down_cd)
        )
    }
    DropdownMenu(
        modifier = Modifier,
        expanded = expandedDropDown.value,
        onDismissRequest = { expandedDropDown.value = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = if (selectionState.value)
                        stringResource(R.string.unselect_all)
                    else
                        stringResource(R.string.select_all)
                )
            },
            onClick = {
                selectionState.value = !selectionState.value
                if (selectionState.value)
                    selectedMedia.addAll(state.media)
                else
                    selectedMedia.clear()
                expandedDropDown.value = false
            },
        )
        if (albumId != -1L) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.move_album_to_trash)) },
                onClick = {
                    scope.launch {
                        viewModel.handler.trashMedia(
                            result = result,
                            mediaList = state.media,
                            trash = true
                        )
                        navController.navigateUp()
                    }
                    expandedDropDown.value = false
                },
            )
        }
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.favorites)) },
            onClick = {
                navController.navigate(Screen.FavoriteScreen.route)
                expandedDropDown.value = false
            },
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.trash)) },
            onClick = {
                navController.navigate(Screen.TrashedScreen.route)
                expandedDropDown.value = false
            },
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.settings_title)) },
            onClick = {
                navController.navigate(Screen.SettingsScreen.route)
                expandedDropDown.value = false
            }
        )
    }
}

