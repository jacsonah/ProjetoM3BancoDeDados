package br.univali.projetom3bancodedados.jigsawpuzzle

import android.view.DragEvent
import android.view.View
import kotlin.properties.Delegates

internal class PieceOnDragListener(
    private val onPieceDragged: () -> Unit,
    private val onDrop: (dropView: View, dragEvent: DragEvent, dragLocalState: DragLocalState) -> Boolean,
    private val onDragEnded: () -> Unit
) : View.OnDragListener
{
    companion object {
        private var pieceDragged: Boolean by Delegates.notNull()
        private var dragInitialX: Float by Delegates.notNull()
        private var dragInitialY: Float by Delegates.notNull()
    }

    override fun onDrag(dropView: View, dragEvent: DragEvent): Boolean
    {
        return when (dragEvent.action)
        {
            DragEvent.ACTION_DRAG_STARTED -> {
                val dragLocalState = dragEvent.localState

                if (dragLocalState is DragLocalState) {
                    if (dragLocalState.dragView == dropView) {
                        pieceDragged = false
                        dragInitialX = dragEvent.x
                        dragInitialY = dragEvent.y
                    }
                    true
                }
                else {
                    false
                }
            }
            DragEvent.ACTION_DRAG_ENTERED -> true
            DragEvent.ACTION_DRAG_LOCATION -> {
                if (!pieceDragged && (dragInitialX != dragEvent.x || dragInitialY != dragEvent.y)) {
                    pieceDragged = true
                    onPieceDragged()
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
                        onDrop(dropView, dragEvent, dragLocalState)
                    }
                }
                else {
                    false
                }
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                onDragEnded()
                true
            }
            else -> false
        }
    }
}
