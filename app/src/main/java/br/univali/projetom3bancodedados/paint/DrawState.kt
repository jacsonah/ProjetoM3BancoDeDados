package br.univali.projetom3bancodedados.paint

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import br.univali.projetom3bancodedados.database.AppDatabase
import br.univali.projetom3bancodedados.database.DrawPath
import com.richpath.RichPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DrawState(
    drawableId: Int,
    private val coroutineScope: CoroutineScope,
    context: Context
)
{
    private val drawPathDao = AppDatabase.getDatabase(context).drawPathDao()
    private val drawName = context.resources.getResourceEntryName(drawableId)
    private val coloredRichPaths = mutableSetOf<RichPath>()

    fun loadColors(richPaths: Array<RichPath>)
    {
        coroutineScope.launch {
            val paths = drawPathDao.getPathsByDraw(drawName)

            if (paths.isNotEmpty())
            {
                val richPathsMap = mutableMapOf<String, RichPath>()

                richPaths.forEach {
                    richPathsMap[it.name] = it
                }

                paths.forEach {
                    val richPath = richPathsMap[it.pathName]

                    if (richPath != null)
                    {
                        richPath.fillColor = it.argbColor
                        coloredRichPaths.add(richPath)
                    }
                }
            }
        }
    }

    fun colorPath(richPath: RichPath, argbColor: Int)
    {
        richPath.fillColor = argbColor
        coloredRichPaths.add(richPath)

        coroutineScope.launch {
            drawPathDao.insertPath(DrawPath(richPath.name, argbColor, drawName))
        }
    }

    fun eraseColors()
    {
        coloredRichPaths.forEach {
            it.fillColor = Color(0xff7c7c7c).toArgb()
        }
        coloredRichPaths.clear()
        coroutineScope.launch {
            drawPathDao.deleteAllPathsByDraw(drawName)
        }
    }
}
