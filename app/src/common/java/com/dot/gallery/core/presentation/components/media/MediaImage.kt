/*
 * SPDX-FileCopyrightText: 2023 IacobIacob01
 * SPDX-License-Identifier: Apache-2.0
 */

package com.dot.gallery.core.presentation.components.media

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.MediaStoreSignature
import com.dot.gallery.core.Constants.Animation
import com.dot.gallery.feature_node.domain.model.Media
import com.dot.gallery.ui.theme.Dimens
import com.dot.gallery.ui.theme.Shapes

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediaImage(
    media: Media,
    preloadRequestBuilder: RequestBuilder<Drawable>,
    selectionState: MutableState<Boolean>,
    isSelected: Boolean
) {
    val selectedSize = if (isSelected) 12.dp else 0.dp
    val scale = if (isSelected) 0.5f else 1f
    val selectedShape = if (isSelected) Shapes.large else Shapes.extraSmall
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .size(Dimens.Photo())
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(selectedSize)
                .clip(selectedShape),
            model = media.uri,
            contentDescription = media.label,
            contentScale = ContentScale.Crop,
        ) {
            it.thumbnail(preloadRequestBuilder)
                .signature(MediaStoreSignature(media.mimeType, media.timestamp, media.orientation))
                .priority(Priority.HIGH)
        }
        if (media.duration != null) {
            VideoDurationHeader(
                modifier = Modifier
                    .padding(selectedSize / 2)
                    .scale(scale),
                media = media
            )
        }

        AnimatedVisibility(
            visible = media.favorite == 1,
            enter = Animation.enterAnimation,
            exit = Animation.exitAnimation,
            modifier = Modifier
                .padding(selectedSize / 2)
                .align(Alignment.BottomEnd)
        ) {
            Image(
                modifier = Modifier
                    .scale(scale)
                    .padding(8.dp)
                    .size(16.dp),
                imageVector = Icons.Filled.Favorite,
                colorFilter = ColorFilter.tint(Color.Red),
                contentDescription = null
            )
        }

        AnimatedVisibility(
            visible = selectionState.value,
            enter = Animation.enterAnimation,
            exit = Animation.exitAnimation
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                if (isSelected) MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                                else Color.Transparent,
                                Color.Transparent
                            )
                        )
                    )
            ) {
                RadioButton(
                    modifier = Modifier.padding(8.dp),
                    selected = isSelected,
                    onClick = null
                )
            }
        }
    }
}
