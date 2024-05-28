package com.indra.rimac.allMovies.presentacion.ui.composables

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.indra.rimac.allMovies.presentacion.theme.ColorWhite
import com.indra.rimac.allMovies.presentacion.theme.blue_gray_200
import com.indra.rimac.allMovies.presentacion.theme.blue_gray_300
import com.indra.rimac.allMovies.presentacion.theme.dynamicContentTextSize

@Composable
fun CustomIconButton(
    modifier: Modifier, iconNavigation: ImageVector?, iconNavigationColor: Color?
) {
    iconNavigation?.let { iv ->
        iconNavigationColor?.let { inc ->
            Icon(
                imageVector = iv, contentDescription = "Action bar", modifier = modifier, tint = inc
            )
        }
    }
}

@Composable
fun CustomButton(
    modifier: Modifier,
    enabledButton: Boolean,
    textButton: String,
    containerColor: Color,
    onAcceptButton: () -> Unit
) {
    Button(
        onClick = { onAcceptButton() },
        modifier = modifier,
        enabled = enabledButton,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = ColorWhite,
            disabledContainerColor = blue_gray_200,
            disabledContentColor = blue_gray_300
        )
    ) {
        Text(
            text = textButton, fontSize = dynamicContentTextSize
        )
    }
}