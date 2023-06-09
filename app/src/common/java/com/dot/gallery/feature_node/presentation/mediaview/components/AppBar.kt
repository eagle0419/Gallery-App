/*
 * SPDX-FileCopyrightText: 2023 IacobIacob01
 * SPDX-License-Identifier: Apache-2.0
 */

package com.dot.gallery.feature_node.presentation.mediaview.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dot.gallery.core.Constants
import com.dot.gallery.ui.theme.Black40P

@Composable
fun MediaViewAppBar(
    showUI: Boolean,
    currentDate: String,
    paddingValues: PaddingValues,
    onGoBack: () -> Unit,
) {
    AnimatedVisibility(
        visible = showUI,
        enter = Constants.Animation.enterAnimation(Constants.DEFAULT_TOP_BAR_ANIMATION_DURATION),
        exit = Constants.Animation.exitAnimation(Constants.DEFAULT_TOP_BAR_ANIMATION_DURATION)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Black40P, Color.Transparent)
                    )
                )
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onGoBack) {
                Image(
                    imageVector = Icons.Outlined.ArrowBack,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "Go back",
                    modifier = Modifier
                        .height(48.dp)
                )
            }
            Text(
                text = currentDate.uppercase(),
                modifier = Modifier,
                style = MaterialTheme.typography.titleSmall,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                textAlign = TextAlign.End
            )
        }
    }
}