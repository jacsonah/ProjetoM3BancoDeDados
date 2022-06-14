package br.univali.projetom3bancodedados.paint

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.univali.projetom3bancodedados.R
import br.univali.projetom3bancodedados.database.AppDatabase
import br.univali.projetom3bancodedados.database.DrawPath
import com.richpath.RichPathView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun Paint(drawableId: Int)
{
    val coroutineScope = rememberCoroutineScope()
    val drawName = LocalContext.current.resources.getResourceEntryName(drawableId)
    val database = AppDatabase.getDatabase(LocalContext.current)
    val drawPathDao = database.drawPathDao()

    Column()
    {
        var selectedColor = Color.Unspecified

        Button(
            onClick = {
              coroutineScope.launch {
                  drawPathDao.deleteAllPathsByDraw(drawName)
              }
            },
            modifier = Modifier.align(Alignment.End)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.eraser),
                contentDescription = null
            )
        }

        AndroidView(
            factory = { context ->
                RichPathView(context).apply()
                {
                    setVectorDrawable(drawableId)

                    coroutineScope.launch()
                    {
                        drawPathDao.getPathsByDraw(drawName).collect()
                        {
                            Log.d("teste", "Mudança nos dados")

                            if (it.isEmpty())
                            {
                                val allRichPaths = findAllRichPaths()

                                for (richPath in allRichPaths)
                                {
                                    richPath.fillColor = Color(0xff7c7c7c).toArgb()
                                }
                            }
                            else
                            {
                                for (path in it)
                                {
                                    findRichPathByName(path.name)?.fillColor = path.color
                                }
                            }
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
