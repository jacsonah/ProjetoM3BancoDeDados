package br.univali.projetom3bancodedados.paint

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

data class Pencil(val drawableId: Int, val color: Color)

@Composable
fun Pencil(
    drawableId: Int,
    selected: Boolean,
    modifier: Modifier = Modifier
)
{
    val height = if (selected) 110.dp else 80.dp

    Image(
        painter = painterResource(id = drawableId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopCenter,
        modifier = modifier.height(height)
    )
}
