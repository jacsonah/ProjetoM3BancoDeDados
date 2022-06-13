package br.univali.projetom3bancodedados.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@ExperimentalPagerApi
@Composable
fun Screen()
{
    val pages = listOf(Pages.BelgiumPaint)

    HorizontalPager(
        count = pages.size,
        contentPadding = PaddingValues(2.dp),
        modifier = Modifier.fillMaxSize()
    )
    {
        page -> pages[page].screen()
    }
}
