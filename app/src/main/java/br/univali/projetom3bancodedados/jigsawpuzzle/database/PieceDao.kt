package br.univali.projetom3bancodedados.jigsawpuzzle.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface PieceDao {
    @Query("SELECT * FROM jigsaw_puzzle_pieces ORDER BY row_index, column_index")
    suspend fun getPieces(): List<Piece>
}
