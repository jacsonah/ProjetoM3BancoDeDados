package br.univali.projetom3bancodedados.jigsawpuzzle

import android.graphics.Bitmap
import android.os.Build
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView

data class Piece(
    val rowIndex: Int,
    val columnIndex: Int,
    val currentRowIndex: Int?,
    val currentColumnIndex: Int?,
    val bitmap: Bitmap
)

private data class DragLocalState(
    val dragView: View,
    val piece: Piece
)

@Composable
fun Piece(
    piece: Piece?,
    modifier: Modifier = Modifier,
    acceptDragEvents: Boolean = false,
    acceptDropEvents: Boolean = false,
    onDrop: (pieceDropped: Piece) -> Unit = {}
)
{
    var alpha by remember{ mutableStateOf(1f) }

    AndroidView(
        factory = { context ->
            ImageView(context).apply {}
        },
        update = { imageView ->
            var dragStartedX: Float? = null
            var dragStartedY: Float? = null

            imageView.setImageBitmap(piece?.bitmap)

            imageView.setOnLongClickListener {
                when (acceptDragEvents) {
                    true -> {
                        val shadowBuilder = View.DragShadowBuilder(it)
                        val dragLocalState = piece?.let { piece ->
                            DragLocalState(
                                dragView = it,
                                piece = piece
                            )
                        }

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            @Suppress("deprecation")
                            it.startDrag(null, shadowBuilder, dragLocalState, 0)
                        }
                        else {
                            it.startDragAndDrop(null, shadowBuilder, dragLocalState, View.DRAG_FLAG_OPAQUE)
                        }
                        true
                    }
                    false -> false
                }
            }

            imageView.setOnDragListener { dropView, dragEvent ->
                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        val dragLocalState = dragEvent.localState

                        if (dragLocalState is DragLocalState && dragLocalState.dragView == dropView) {
                            dragStartedX = dragEvent.x
                            dragStartedY = dragEvent.y
                            true
                        }
                        else {
                            acceptDropEvents
                        }
                    }
                    DragEvent.ACTION_DRAG_ENTERED -> true
                    DragEvent.ACTION_DRAG_LOCATION -> {
                        if (alpha != 0f) {
                            val dragLocalState = dragEvent.localState

                            if ((dragLocalState is DragLocalState) &&
                                (dragLocalState.dragView == dropView) &&
                                (dragStartedX != dragEvent.x || dragStartedY != dragEvent.y)) {
                                alpha = 0f
                            }
                        }
                        true
                    }
                    DragEvent.ACTION_DRAG_EXITED -> true
                    DragEvent.ACTION_DROP -> {
                        val dragLocalState = dragEvent.localState

                        if (dragLocalState is DragLocalState) {
                            if (dragLocalState.dragView == dropView) {
                                false
                            }
                            else {
                                onDrop(dragLocalState.piece)
                                true
                            }
                        }
                        else {
                            false
                        }
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        if (alpha != 1f) {
                            alpha = 1f
                        }
                        true
                    }
                    else -> false
                }
            }
        },
        modifier = modifier.alpha(alpha)
    )
}
