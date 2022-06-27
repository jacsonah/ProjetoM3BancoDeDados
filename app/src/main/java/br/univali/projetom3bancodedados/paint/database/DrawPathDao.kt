package br.univali.projetom3bancodedados.paint.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DrawPathDao {
    @Query("SELECT * FROM draw_path WHERE draw_name = :drawName")
    suspend fun getPathsByDraw(drawName: String): List<DrawPath>

    /**  INSERT INTO draw_path (path_name, argb_color, draw_name)
     *   VALUES (path.pathName, path.argbColor, path.drawName)
     *
     *   UPDATE draw_path SET path_name = path.pathName, argb_color = path.argbColor, draw_name = path.drawName
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPath(path: DrawPath)

    @Query("DELETE FROM draw_path WHERE draw_name = :drawName")
    suspend fun deleteAllPathsByDraw(drawName: String)
}
