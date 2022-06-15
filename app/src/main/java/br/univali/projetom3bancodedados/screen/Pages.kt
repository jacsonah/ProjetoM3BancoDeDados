package br.univali.projetom3bancodedados.screen

import androidx.compose.runtime.Composable
import br.univali.projetom3bancodedados.R
import br.univali.projetom3bancodedados.paint.Draw
import br.univali.projetom3bancodedados.paint.Paint

sealed class Pages(var screen: @Composable () -> Unit)
{
    object BelgiumPaint: Pages({ Paint(drawableId = R.drawable.belgium) })
}
