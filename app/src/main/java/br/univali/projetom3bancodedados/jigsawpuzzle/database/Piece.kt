package br.univali.projetom3bancodedados.jigsawpuzzle.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "jigsaw_puzzle_pieces", primaryKeys = ["row_index", "column_index"])
data class Piece(
    @ColumnInfo(name = "row_index")
    val rowIndex: Int,

    @ColumnInfo(name = "column_index")
    val columnIndex: Int,

    @ColumnInfo(name = "current_row_index")
    val currentRowIndex: Int,

    @ColumnInfo(name = "current_column_index")
    val currentColumnIndex: Int
)
