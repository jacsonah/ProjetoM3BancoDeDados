package br.univali.projetom3bancodedados.jigsawpuzzle

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun GridPiece(
    imageBitmap: Bitmap?,
    modifier: Modifier = Modifier
)
{
    AndroidView(
        factory = { context ->
            ImageView(context).apply()
            {
                when (imageBitmap)
                {
                    null -> setImageDrawable(null)
                    else -> setImageBitmap(imageBitmap)
                }

                setOnLongClickListener()
                {
                    if (drawable == null)
                    {
                        false
                    }
                    else
                    {
                        val shadowBuilder = View.DragShadowBuilder(it)

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                        {
                            @Suppress("deprecation")
                            it.startDrag(null, shadowBuilder, it, 0)
                        }
                        else
                        {
                            it.startDragAndDrop(null, shadowBuilder, it, 0)
                        }
                        true
                    }
                }

                setOnDragListener { dropView, dragEvent ->
                    when(dragEvent.action)
                    {
                        DragEvent.ACTION_DRAG_STARTED -> drawable == null
                        DragEvent.ACTION_DRAG_ENTERED -> true
                        DragEvent.ACTION_DRAG_LOCATION -> true
                        DragEvent.ACTION_DRAG_EXITED -> true
                        DragEvent.ACTION_DROP -> {
                            val localState = dragEvent.localState

                            if (dropView is ImageView)
                            {
                               when (localState)
                               {
                                   is Drawable -> {
                                       dropView.setImageDrawable(localState)
                                       true
                                   }
                                   is ImageView -> {
                                       dropView.setImageDrawable(localState.drawable)
                                       localState.setImageDrawable(null)
                                       true
                                   }
                                   else -> false
                               }
                            }
                            else
                            {
                                false
                            }
                        }
                        DragEvent.ACTION_DRAG_ENDED -> true
                        else -> false
                    }
                }
            }
        },
        modifier = modifier
    )
}
