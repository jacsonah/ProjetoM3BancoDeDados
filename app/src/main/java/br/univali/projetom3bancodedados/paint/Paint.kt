package br.univali.projetom3bancodedados.paint

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Paint(drawableId: Int)
{
    var selectedColor by remember{ mutableStateOf(Color.Unspecified) }

    Column()
    {
        Draw(
            drawableId = drawableId,
            selectedColor = selectedColor,
            modifier = Modifier.weight(1f)
        )

        Pencils(
            onChangeColorSelection = {
                selectedColor = it
            }
        )
    }
}
