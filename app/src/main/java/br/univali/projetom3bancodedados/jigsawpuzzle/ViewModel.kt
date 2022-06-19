package br.univali.projetom3bancodedados.jigsawpuzzle

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.univali.projetom3bancodedados.jigsawpuzzle.database.Piece as DatabasePiece
import br.univali.projetom3bancodedados.jigsawpuzzle.database.PieceDao
import kotlinx.coroutines.launch

class ViewModelFactory(
    private val pieceDao: PieceDao,
    private val puzzleBitmap: Bitmap,
    private val rows: Int,
    private val columns: Int
) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ViewModel(
            pieceDao = pieceDao,
            puzzleBitmap = puzzleBitmap,
            rows = rows,
            columns = columns
        ) as T
}

class ViewModel(
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

            for (row in 0 until rows)
            {
                for (column in 0 until columns)
                {
                    val pieceBitmap = Bitmap.createBitmap(
                        puzzleBitmap,
                        (pieceBitmapWidth * column),
                        (pieceBitmapHeight * row),
                        pieceBitmapWidth,
                        pieceBitmapHeight
                    )

                    if (databasePiece?.rowIndex == row && databasePiece.columnIndex == column) {
                        gridPieces[databasePiece.currentRowIndex][databasePiece.currentColumnIndex].value = Piece(
                            rowIndex = row,
                            columnIndex = column,
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
                                rowIndex = row,
                                columnIndex = column,
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
        gridPieces[dropRowIndex][dropColumnIndex].value = Piece(
            rowIndex = droppedPiece.rowIndex,
            columnIndex = droppedPiece.columnIndex,
            currentRowIndex = dropRowIndex,
            currentColumnIndex = dropColumnIndex,
            bitmap = droppedPiece.bitmap
        )

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

        if (droppedPiece.currentRowIndex != null && droppedPiece.currentColumnIndex != null) {
            gridPieces[droppedPiece.currentRowIndex][droppedPiece.currentColumnIndex].value = null
        }
        else {
            listPieces.remove(droppedPiece)
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
