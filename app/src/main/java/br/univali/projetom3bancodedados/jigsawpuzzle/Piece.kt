package br.univali.projetom3bancodedados.jigsawpuzzle

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView

data class Piece(
    val rowIndex: Int,
    val columnIndex: Int,
    var currentRowIndex: Int?,
    var currentColumnIndex: Int?,
    val bitmap: Bitmap
)

private fun startPieceDrag(dragView: View, piece: Piece)
{
    val shadowBuilder = View.DragShadowBuilder(dragView)
    val dragLocalState = DragLocalState(dragView = dragView, piece = piece)

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        @Suppress("DEPRECATION")
        dragLocalState.dragView.startDrag(null, shadowBuilder, dragLocalState, 0)
    }
    else {
        dragLocalState.dragView.startDragAndDrop(null, shadowBuilder, dragLocalState, View.DRAG_FLAG_OPAQUE)
    }
}

@Composable
fun GridPiece(
    piece: Piece?,
    modifier: Modifier = Modifier,
    onDrop: (pieceDropped: Piece) -> Boolean
)
{
    var alpha by remember{ mutableStateOf(1f) }

    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.FIT_XY
            }
        },
        update = { imageView ->
            imageView.setImageBitmap(piece?.bitmap)
            imageView.setOnLongClickListener {
                if (piece != null) {
                    startPieceDrag(dragView = it, piece = piece)
                    true
                }
                else {
                    false
                }
            }
            imageView.setOnDragListener(
                PieceOnDragListener(
                    onPieceDragged = {
                        alpha = 0f
                    },
                    onDrop = { _, _, dragLocalState ->
                        if (piece == null) {
                            onDrop(dragLocalState.piece)
                        }
                        else {
                            false
                        }
                    },
                    onDragEnded = {
                        alpha = 1f
                    }
                )
            )
        },
        modifier = modifier.alpha(alpha)
    )
}

@Composable
fun ListPiece(
    piece: Piece,
    modifier: Modifier = Modifier,
    onLeftSideDrop: (pieceDropped: Piece) -> Boolean,
    onRightSideDrop: (pieceDropped: Piece) -> Boolean
)
{
    var alpha by remember{ mutableStateOf(1f) }

    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.FIT_XY
                setOnDragListener(
                    PieceOnDragListener(
                        onPieceDragged = {
                            alpha = 0f
                        },
                        onDrop = { dropView, dragEvent, dragLocalState ->
                            if (dragEvent.x < dropView.width / 2) {
                                onLeftSideDrop(dragLocalState.piece)
                            }
                            else {
                                onRightSideDrop(dragLocalState.piece)
                            }
                        },
                        onDragEnded = {
                            alpha = 1f
                        }
                    )
                )
            }
        },
        update = { imageView ->
            imageView.setImageBitmap(piece.bitmap)
            imageView.setOnLongClickListener {
                startPieceDrag(dragView = it, piece = piece)
                true
            }
        },
        modifier = modifier.alpha(alpha)
    )
}
