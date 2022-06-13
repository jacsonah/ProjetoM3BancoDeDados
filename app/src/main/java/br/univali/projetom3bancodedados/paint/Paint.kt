package br.univali.projetom3bancodedados.paint

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import br.univali.projetom3bancodedados.database.AppDatabase
import br.univali.projetom3bancodedados.database.DrawPath
import com.richpath.RichPathView
import kotlinx.coroutines.launch

@Composable
fun Paint(drawableId: Int)
{
    val coroutineScope = rememberCoroutineScope()

    Column()
    {
        var selectedColor = Color.Unspecified

        AndroidView(
            factory = { context ->
                RichPathView(context).apply()
                {
                    setVectorDrawable(drawableId)

                    val drawName = context.resources.getResourceEntryName(drawableId)
                    val database = AppDatabase.getDatabase(context)
                    val drawPathDao = database.drawPathDao()

                    coroutineScope.launch()
                    {
                        val paths = drawPathDao.getPathsByDraw(drawName)

                        for (path in paths)
                        {
                            findRichPathByName(path.pathName)?.fillColor = path.argbColor
                        }
                    }

                    setOnPathClickListener()
                    {
                        val argbColor = selectedColor.toArgb()
                        it.fillColor = argbColor

                        coroutineScope.launch()
                        {
                            drawPathDao.insertPath(DrawPath(it.name, it.fillColor, drawName))
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Pencils(
            onChangeColorSelection = {
                selectedColor = it
            }
        )
    }
}
