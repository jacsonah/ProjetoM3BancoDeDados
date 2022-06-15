package br.univali.projetom3bancodedados.paint

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import br.univali.projetom3bancodedados.R
import com.richpath.RichPathView

@Composable
fun Draw(
    drawableId: Int,
    selectedColor: Color,
    modifier: Modifier = Modifier
)
{
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val drawState = remember {
        DrawState(drawableId, coroutineScope, context)
    }

    Column(
        modifier = modifier
    )
    {
        Button(
            onClick = {
                drawState.eraseColors()
            },
            modifier = Modifier.align(Alignment.End)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.eraser),
                contentDescription = null
            )
        }

        AndroidView(
            factory = {
                RichPathView(it).apply {
                    setVectorDrawable(drawableId)
                    drawState.loadColors(findAllRichPaths())
                }
            },
            update = {
                it.setOnPathClickListener { richPath ->
                    drawState.colorPath(richPath, selectedColor.toArgb())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}
