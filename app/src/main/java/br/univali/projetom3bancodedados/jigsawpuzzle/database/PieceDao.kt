package br.univali.projetom3bancodedados.jigsawpuzzle.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PieceDao {
    @Query("SELECT * FROM jigsaw_puzzle_pieces ORDER BY row_index, column_index")
    suspend fun getPieces(): List<Piece>

    /**  INSERT INTO jigsaw_puzzle_pieces (row_index, column_index, current_row_index, current_column_index)
     *   VALUES (piece.rowIndex, piece.columnIndex, piece.currentRowIndex, piece.currentColumnIndex)
     *
     *   UPDATE jigsaw_puzzle_pieces
     *   SET current_row_index = piece.currentRowIndex, current_column_index = piece.currentColumnIndex
     *   WHERE row_index = piece.rowIndex AND column_index = piece.columnIndex
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPiece(piece: Piece)

    @Query("DELETE FROM jigsaw_puzzle_pieces WHERE row_index = :rowIndex AND column_index = :columnIndex")
    suspend fun deletePiece(rowIndex: Int, columnIndex: Int)

    @Query("DELETE FROM jigsaw_puzzle_pieces")
    suspend fun deleteAllPieces()
}
