package br.univali.projetom3bancodedados.jigsawpuzzle

import android.view.DragEvent
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PiecesList(
    listPieces: SnapshotStateList<Piece>,
    pieceWidth: Dp,
    pieceHeight: Dp,
    modifier: Modifier = Modifier,
    onDrop: (droppedPiece: Piece, dropIndex: Int) -> Boolean
)
{
    Row(
        modifier = modifier
    )
    {
        BoxWithConstraints(
            modifier = Modifier.weight(1f)
        )
        {
            PiecesListPlaceholder(
                onDrop = {
                    onDrop(it, 0)
                },
                modifier = Modifier.width(maxWidth).fillMaxHeight()
            )
        }

        LazyRow {
            itemsIndexed(listPieces) { index, piece ->
                Row {
                    ListPiece(
                        piece = piece,
                        onLeftSideDrop = {
                            onDrop(it, index)
                        },
                        onRightSideDrop = {
                            onDrop(it, index + 1)
                        },
                        modifier = Modifier.size(width = pieceWidth, height = pieceHeight)
                    )

                    if (index < (listPieces.size - 1)) {
                        PiecesListPlaceholder(
                            onDrop = {
                                onDrop(it, index + 1)
                            },
                            modifier = Modifier.width(10.dp).fillMaxHeight()
                        )
                    }
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier.weight(1f)
        )
        {
            PiecesListPlaceholder(
                onDrop = {
                    onDrop(it, listPieces.size)
                },
                modifier = Modifier.width(maxWidth).fillMaxHeight()
            )
        }
    }
}

@Composable
private fun PiecesListPlaceholder(
    modifier: Modifier = Modifier,
    onDrop: (pieceDropped: Piece) -> Boolean
)
{
    AndroidView(
        factory = { context ->
            View(context).apply {
                setOnDragListener { _, dragEvent ->
                    when (dragEvent.action) {
                        DragEvent.ACTION_DRAG_STARTED -> true
                        DragEvent.ACTION_DRAG_ENTERED -> true
                        DragEvent.ACTION_DRAG_LOCATION -> true
                        DragEvent.ACTION_DRAG_EXITED -> true
                        DragEvent.ACTION_DROP -> {
                            val dragLocalState = dragEvent.localState

                            if (dragLocalState is DragLocalState) {
                                onDrop(dragLocalState.piece)
                            }
                            else {
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
