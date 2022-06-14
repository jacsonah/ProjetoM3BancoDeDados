package br.univali.projetom3bancodedados.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DrawPathDao {
    @Query("SELECT path_name, argb_color FROM draw_path WHERE draw_name = :drawName")
    fun getPathsByDraw(drawName: String): Flow<List<Path>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPath(path: DrawPath)

    @Query("DELETE FROM draw_path WHERE draw_name = :drawName")
    suspend fun deleteAllPathsByDraw(drawName: String)
}
