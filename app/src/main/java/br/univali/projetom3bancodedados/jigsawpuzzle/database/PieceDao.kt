package br.univali.projetom3bancodedados.jigsawpuzzle.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PieceDao {
    @Query("SELECT * FROM jigsaw_puzzle_pieces ORDER BY row_index, column_index")
    suspend fun getPieces(): List<Piece>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPiece(piece: Piece)

    @Query("DELETE FROM jigsaw_puzzle_pieces WHERE row_index = :rowIndex AND column_index = :columnIndex")
    suspend fun deletePiece(rowIndex: Int, columnIndex: Int)

    @Query("DELETE FROM jigsaw_puzzle_pieces")
    suspend fun deleteAllPieces()
}
