package br.univali.projetom3bancodedados.jigsawpuzzle

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import br.univali.projetom3bancodedados.R
import br.univali.projetom3bancodedados.database.AppDatabase

@Composable
fun JigsawPuzzle(drawableId: Int, rows: Int, columns: Int)
{
    val viewModel: ViewModel = viewModel(
        factory = LocalContext.current.getDrawable(drawableId)?.toBitmap()?.let {
            ViewModelFactory(
                pieceDao = AppDatabase.getDatabase(LocalContext.current).pieceDao(),
                puzzleBitmap = it,
                rows = rows,
                columns = columns
            )
        }

    )

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    )
    {
        Button(
            onClick = {
                viewModel.clearGrid()
            },
            modifier = Modifier.align(Alignment.End)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.restore),
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .border(BorderStroke(2.dp, Color.Black))
                .weight(rows.toFloat())
        )
        {
            repeat(rows) { rowIndex ->
                BoxWithConstraints(
                    modifier = Modifier.weight(1f)
                )
                {
                    val pieceHeight = maxHeight
                    val pieceWidth = (pieceHeight * viewModel.pieceBitmapWidth) / viewModel.pieceBitmapHeight

                    Row()
                    {
                        repeat(columns) { columnIndex ->
                            val gridPiece = viewModel.gridPieces[rowIndex][columnIndex]

                            Piece(
                                piece = gridPiece.value,
                                acceptDragEvents = gridPiece.value != null,
                                acceptDropEvents = gridPiece.value == null,
                                onDrop = {
                                    viewModel.dropPieceOnGrid(it, rowIndex, columnIndex)
                                },
                                modifier = Modifier.size(width = pieceWidth, height = pieceHeight)
                            )
                        }
                    }
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier.weight(1f)
        )
        {
            val pieceHeight = maxHeight
            val pieceWidth = (pieceHeight * viewModel.pieceBitmapWidth) / viewModel.pieceBitmapHeight

            LazyRow()
            {
                items(viewModel.listPieces)
                {
                    Piece(
                        piece = it,
                        acceptDragEvents = true,
                        modifier = Modifier.size(width = pieceWidth, height = pieceHeight)
                    )
                }
            }
        }
    }
}
