package com.indra.rimac.allMovies.presentacion.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.indra.rimac.allMovies.presentacion.theme.ContentInsetThreeHundred
import com.indra.rimac.allMovies.presentacion.theme.Purple80
import com.indra.rimac.allMovies.presentacion.theme.blue_gray_500
import com.indra.rimac.allMovies.presentacion.theme.contentInsetFive
import com.indra.rimac.allMovies.presentacion.theme.contentInsetQuarter
import com.indra.rimac.allMovies.presentacion.theme.dynamicBodyTextSize
import com.indra.rimac.allMovies.presentacion.theme.dynamicContentTextSize
import com.indra.rimac.allMovies.presentacion.theme.dynamicDisplayTextSize
import com.indra.rimac.allMovies.presentacion.theme.dynamicTwentyFourTextSize

@Composable
fun CustomText(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = contentInsetQuarter
            ),
        fontSize = dynamicTwentyFourTextSize,
        color = Purple80,
        textAlign = TextAlign.Start,
        lineHeight = dynamicDisplayTextSize
    )
}

@Composable
fun MessageDialog(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = rememberScrollState())
            .heightIn(max = ContentInsetThreeHundred)
    ) {
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = contentInsetQuarter),
            fontSize = dynamicContentTextSize,
            color = blue_gray_500,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun TextClickButton(
    modifier: Modifier,
    textButton: String,
    textColorButton: Color,
    backgroundColorButton: Color,
    onClick: () -> Unit
) {
    val rounded = RoundedCornerShape(contentInsetQuarter)
    TextButton(
        onClick = { onClick() },
        modifier = modifier,
        shape = rounded,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColorButton, contentColor = textColorButton
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = contentInsetFive
        )
    ) {
        Text(
            text = textButton, fontSize = dynamicBodyTextSize, maxLines = 1
        )
    }
}