package br.univali.projetom3bancodedados.paint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.univali.projetom3bancodedados.R

@Composable
fun rememberPencils() = remember()
{
    listOf(
        Pencil(R.drawable.pencil_yellow, Color(0xfffff026)),
        Pencil(R.drawable.pencil_blue, Color(0xff2a4bc6)),
        Pencil(R.drawable.pencil_red, Color(0xffff1d1e)),
        Pencil(R.drawable.pencil_orange, Color(0xffff8f16)),
        Pencil(R.drawable.pencil_green, Color(0xff31a21c)),
        Pencil(R.drawable.pencil_purple, Color(0xff712da8)),
        Pencil(R.drawable.pencil_light_pink, Color(0xffffa592)),
        Pencil(R.drawable.pencil_cinnamon, Color(0xff8c4e35)),
        Pencil(R.drawable.pencil_black, Color(0xff000000)),
    )
}

@Composable
fun Pencils(
    modifier: Modifier = Modifier,
    onChangeColorSelection: (selectedColor: Color) -> Unit
)
{
    val pencils = rememberPencils()
    var selectedColor by remember{ mutableStateOf(pencils[0].color) }
    onChangeColorSelection(selectedColor)

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        modifier = modifier.fillMaxWidth()
    )
    {
        for (pencil in pencils)
        {
            val selected = (selectedColor == pencil.color)

            Pencil(
                drawableId = pencil.drawableId,
                selected = selected,
                modifier = Modifier
                    .selectable(
                        selected = selected,
                        onClick = {
                            selectedColor = pencil.color
                        }
                    )
                    .weight(1f)
            )
        }
    }
}
