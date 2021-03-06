package br.univali.projetom3bancodedados.jigsawpuzzle

import android.content.res.Resources
import android.graphics.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.univali.projetom3bancodedados.jigsawpuzzle.database.Piece as DatabasePiece
import br.univali.projetom3bancodedados.jigsawpuzzle.database.PieceDao
import kotlinx.coroutines.launch

class JigsawPuzzleViewModelFactory(
    private val pieceDao: PieceDao,
    private val puzzleBitmap: Bitmap,
    private val rows: Int,
    private val columns: Int
) : ViewModelProvider.NewInstanceFactory()
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        JigsawPuzzleViewModel(
            pieceDao = pieceDao,
            puzzleBitmap = puzzleBitmap,
            rows = rows,
            columns = columns
        ) as T
}

class JigsawPuzzleViewModel(
    private val pieceDao: PieceDao,
    puzzleBitmap: Bitmap,
    rows: Int,
    columns: Int
) : ViewModel()
{
    val pieceBitmapWidth = puzzleBitmap.width / columns
    val pieceBitmapHeight = puzzleBitmap.height / rows

    val listPieces = mutableStateListOf<Piece>()
    val gridPieces = Array(rows) {
        Array<MutableState<Piece?>>(columns) {
            mutableStateOf(null)
        }
    }

    init {
        viewModelScope.launch {
            val databasePieces = pieceDao.getPieces()
            var databasePiece = databasePieces.firstOrNull()
            val databasePiecesIterator = databasePieces.iterator()

            if (databasePiece != null) {
                databasePiecesIterator.next()
            }

            val pieceCanvas = Canvas()
            val pieceBorderPaint = Paint()
            val pieceBorderPath = Path()

            pieceBorderPaint.color = Color.BLACK
            pieceBorderPaint.style = Paint.Style.STROKE
            pieceBorderPaint.strokeWidth = (5 * Resources.getSystem().displayMetrics.density)

            for (rowIndex in 0 until rows)
            {
                for (columnIndex in 0 until columns)
                {
                    val pieceBitmap = Bitmap.createBitmap(
                        puzzleBitmap,
                        (pieceBitmapWidth * columnIndex),
                        (pieceBitmapHeight * rowIndex),
                        pieceBitmapWidth,
                        pieceBitmapHeight
                    )

                    pieceCanvas.setBitmap(pieceBitmap)
                    pieceBorderPath.reset()
                    pieceBorderPath.lineTo(pieceBitmapWidth.toFloat(), 0f)
                    pieceBorderPath.lineTo(pieceBitmapWidth.toFloat(), pieceBitmapHeight.toFloat())
                    pieceBorderPath.lineTo(0f, pieceBitmapHeight.toFloat())
                    pieceBorderPath.lineTo(0f, 0f)
                    pieceCanvas.drawPath(pieceBorderPath, pieceBorderPaint)

                    if (databasePiece?.rowIndex == rowIndex && databasePiece.columnIndex == columnIndex) {
                        gridPieces[databasePiece.currentRowIndex][databasePiece.currentColumnIndex].value =
                            Piece(
                                rowIndex = rowIndex,
                                columnIndex = columnIndex,
                                currentRowIndex = databasePiece.currentRowIndex,
                                currentColumnIndex = databasePiece.currentColumnIndex,
                                bitmap = pieceBitmap
                            )

                        databasePiece = when (databasePiecesIterator.hasNext()) {
                            true -> databasePiecesIterator.next()
                            false -> null
                        }
                    }
                    else {
                        listPieces.add(
                            Piece(
                                rowIndex = rowIndex,
                                columnIndex = columnIndex,
                                currentRowIndex = null,
                                currentColumnIndex = null,
                                bitmap = pieceBitmap
                            )
                        )
                    }
                }
            }

            listPieces.shuffle()
        }
    }

    fun dropPieceOnGrid(droppedPiece: Piece, dropRowIndex: Int, dropColumnIndex: Int)
    {
        if (droppedPiece.currentRowIndex != null && droppedPiece.currentColumnIndex != null) {
            gridPieces[droppedPiece.currentRowIndex!!][droppedPiece.currentColumnIndex!!].value = null
        }
        else {
            listPieces.remove(droppedPiece)
        }

        droppedPiece.currentRowIndex = dropRowIndex
        droppedPiece.currentColumnIndex = dropColumnIndex
        gridPieces[dropRowIndex][dropColumnIndex].value = droppedPiece

        viewModelScope.launch {
            pieceDao.insertPiece(
                DatabasePiece(
                    rowIndex = droppedPiece.rowIndex,
                    columnIndex = droppedPiece.columnIndex,
                    currentRowIndex = dropRowIndex,
                    currentColumnIndex = dropColumnIndex
                )
            )
        }


    }

    fun dropPieceOnList(droppedPiece: Piece, dropIndex: Int)
    {
        val droppedPieceCurrentRowIndex = droppedPiece.currentRowIndex
        val droppedPieceCurrentColumnIndex = droppedPiece.currentColumnIndex

        if (droppedPieceCurrentRowIndex != null && droppedPieceCurrentColumnIndex != null) {
            gridPieces[droppedPieceCurrentRowIndex][droppedPieceCurrentColumnIndex].value = null
            droppedPiece.currentRowIndex = null
            droppedPiece.currentColumnIndex = null
            listPieces.add(dropIndex, droppedPiece)

            viewModelScope.launch {
                pieceDao.deletePiece(droppedPieceCurrentRowIndex, droppedPieceCurrentColumnIndex)
            }
        }
        else {
            val droppedPieceCurrentIndex = listPieces.indexOf(droppedPiece)
            listPieces.remove(droppedPiece)

            if (droppedPieceCurrentIndex < dropIndex) {
                listPieces.add(dropIndex - 1, droppedPiece)
            }
            else {
                listPieces.add(dropIndex, droppedPiece)
            }
        }
    }

    fun clearGrid()
    {
        gridPieces.forEach {
            it.forEach { pieceMutableState ->
                val piece = pieceMutableState.value

                if (piece != null) {
                    listPieces.add(
                        Piece(
                            rowIndex = piece.rowIndex,
                            columnIndex = piece.columnIndex,
                            currentRowIndex = null,
                            currentColumnIndex = null,
                            bitmap = piece.bitmap
                        )
                    )
                    pieceMutableState.value = null
                }
            }
        }

        listPieces.shuffle()

        viewModelScope.launch {
            pieceDao.deleteAllPieces()
        }
    }
}
