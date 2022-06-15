package br.univali.projetom3bancodedados.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DrawPathDao {
    @Query("SELECT * FROM draw_path WHERE draw_name = :drawName")
    suspend fun getPathsByDraw(drawName: String): List<DrawPath>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPath(path: DrawPath)

    @Query("DELETE FROM draw_path WHERE draw_name = :drawName")
    suspend fun deleteAllPathsByDraw(drawName: String)
}
