package br.univali.projetom3bancodedados.jigsawpuzzle

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Piece(
    imageBitmap: Bitmap,
    modifier: Modifier = Modifier
)
{
    AndroidView(
        factory = { context ->
            ImageView(context).apply()
            {
                setImageBitmap(imageBitmap)
                setOnLongClickListener {
                    val shadowBuilder = View.DragShadowBuilder(it)

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                    {
                        @Suppress("deprecation")
                        it.startDrag(null, shadowBuilder, drawable, 0)
                    }
                    else
                    {
                        it.startDragAndDrop(null, shadowBuilder, drawable, 0)
                    }
                    true
                }
            }
        },
        modifier = modifier
    )
}
