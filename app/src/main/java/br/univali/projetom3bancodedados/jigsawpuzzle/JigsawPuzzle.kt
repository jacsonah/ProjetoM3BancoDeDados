package br.univali.projetom3bancodedados.jigsawpuzzle

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

private const val ROWS = 4
private const val COLUMNS = 3

@Composable
fun rememberPieces(drawableId: Int, context: Context) = remember()
{
    val pieces = mutableStateListOf<Bitmap>()
    val imageBitmap = context.getDrawable(drawableId)?.toBitmap()

    if (imageBitmap != null)
    {
        val pieceWidth = imageBitmap.width / COLUMNS
        val pieceHeight = imageBitmap.height / ROWS

        for (row in 0 until ROWS)
        {
            for (column in 0 until COLUMNS)
            {
                pieces.add(Bitmap.createBitmap(imageBitmap, (pieceWidth * column), (pieceHeight * row), pieceWidth, pieceHeight))
            }
        }
    }

    pieces
}

@Composable
fun JigsawPuzzle(drawableId: Int)
{
    val pieces = rememberPieces(drawableId = drawableId, context = LocalContext.current)

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    )
    {
        Column(
            modifier = Modifier
                .border(BorderStroke(2.dp, Color.Black))
                .weight(ROWS.toFloat())
        )
        {
            repeat(ROWS)
            {
                BoxWithConstraints(
                    modifier = Modifier.weight(1f)
                )
                {
                    val pieceHeight = maxHeight
                    val pieceWidth = (pieceHeight * pieces[0].width) / pieces[0].height

                    Row()
                    {
                        repeat(COLUMNS)
                        {
                            GridPiece(
                                imageBitmap = null,
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
            val pieceWidth = (pieceHeight * pieces[0].width) / pieces[0].height

            LazyRow()
            {
                items(pieces)
                {
                    Piece(
                        imageBitmap = it,
                        modifier = Modifier.size(width = pieceWidth, height = pieceHeight)
                    )
                }
            }
        }
    }
}
