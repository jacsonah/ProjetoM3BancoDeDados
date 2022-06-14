package br.univali.projetom3bancodedados.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "draw_path")
data class DrawPath(
    @PrimaryKey
    @ColumnInfo(name = "path_name")
    val pathName: String,

    @ColumnInfo(name = "argb_color")
    val argbColor: Int,

    @ColumnInfo(name = "draw_name")
    val drawName: String
)

data class Path(
    @ColumnInfo(name = "path_name")
    val name: String,

    @ColumnInfo(name = "argb_color")
    val color: Int
)
