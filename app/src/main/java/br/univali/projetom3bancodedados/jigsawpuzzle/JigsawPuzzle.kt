package br.univali.projetom3bancodedados.jigsawpuzzle

import android.graphics.BitmapFactory
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
import androidx.lifecycle.viewmodel.compose.viewModel
import br.univali.projetom3bancodedados.R
import br.univali.projetom3bancodedados.database.AppDatabase

@Composable
fun JigsawPuzzle(drawableId: Int, rows: Int, columns: Int)
{
    val jigsawPuzzleViewModel: JigsawPuzzleViewModel = viewModel(
        factory = JigsawPuzzleViewModelFactory(
            pieceDao = AppDatabase.getDatabase(LocalContext.current).pieceDao(),
            puzzleBitmap = BitmapFactory.decodeResource(LocalContext.current.resources, drawableId),
            rows = rows,
            columns = columns
        )
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
                jigsawPuzzleViewModel.clearGrid()
            },
            modifier = Modifier.align(Alignment.End)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.restore),
                contentDescription = null
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .border(BorderStroke(5.dp, Color.Black))
                .weight(rows.toFloat())
        )
        {
            val pieceHeight = maxHeight / rows
            val pieceWidth = (pieceHeight * jigsawPuzzleViewModel.pieceBitmapWidth) / jigsawPuzzleViewModel.pieceBitmapHeight

            Column()
            {
                repeat(rows) { rowIndex ->
                    Row()
                    {
                        repeat(columns) { columnIndex ->
                            val gridPiece = jigsawPuzzleViewModel.gridPieces[rowIndex][columnIndex]

                            Piece(
                                piece = gridPiece.value,
                                acceptDragEvents = gridPiece.value != null,
                                acceptDropEvents = gridPiece.value == null,
                                onDrop = {
                                    jigsawPuzzleViewModel.dropPieceOnGrid(it, rowIndex, columnIndex)
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
            val pieceWidth = (maxHeight * jigsawPuzzleViewModel.pieceBitmapWidth) / jigsawPuzzleViewModel.pieceBitmapHeight

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            )
            {
                items(jigsawPuzzleViewModel.listPieces)
                {
                    Piece(
                        piece = it,
                        acceptDragEvents = true,
                        modifier = Modifier
                            .width(pieceWidth)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}
