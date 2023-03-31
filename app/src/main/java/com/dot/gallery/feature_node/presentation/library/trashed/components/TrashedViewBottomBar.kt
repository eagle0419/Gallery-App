package com.dot.gallery.feature_node.presentation.library.trashed.components

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.RestoreFromTrash
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dot.gallery.R
import com.dot.gallery.core.Constants
import com.dot.gallery.feature_node.domain.model.Media
import com.dot.gallery.feature_node.presentation.mediaview.components.BottomBarColumn
import com.dot.gallery.feature_node.presentation.util.deleteMedia
import com.dot.gallery.feature_node.presentation.util.trashMedia
import com.dot.gallery.ui.theme.Black40P

@Composable
fun BoxScope.TrashedViewBottomBar(
    showUI: Boolean,
    paddingValues: PaddingValues,
    currentMedia: Media?,
    currentIndex: Int,
    result: ActivityResultLauncher<IntentSenderRequest>,
    onDeleteMedia: (Int) -> Unit,
) {
    val context = LocalContext.current
    AnimatedVisibility(
        visible = showUI,
        enter = Constants.Animation.enterAnimation(Constants.DEFAULT_TOP_BAR_ANIMATION_DURATION),
        exit = Constants.Animation.exitAnimation(Constants.DEFAULT_TOP_BAR_ANIMATION_DURATION),
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Black40P)
                    )
                )
                .padding(
                    top = 24.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            // Favourite Component
            BottomBarColumn(
                currentMedia = currentMedia,
                imageVector = Icons.Outlined.RestoreFromTrash,
                title = stringResource(id = R.string.trash_restore)
            ) {
                context.trashMedia(result = result, arrayListOf(it), trash = false)
                onDeleteMedia.invoke(currentIndex)
            }
            Spacer(modifier = Modifier.size(8.dp))
            // Delete Component
            BottomBarColumn(
                currentMedia = currentMedia,
                imageVector = Icons.Outlined.DeleteOutline,
                title = stringResource(id = R.string.trash_delete)
            ) {
                context.deleteMedia(result = result, arrayListOf(it))
                onDeleteMedia.invoke(currentIndex)
            }
        }
    }
}