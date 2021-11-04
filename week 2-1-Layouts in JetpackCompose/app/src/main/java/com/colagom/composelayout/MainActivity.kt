package com.colagom.composelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.colagom.composelayout.ui.theme.ComposeLayoutTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLayoutTheme {
                LayoutsCodelab()
            }
        }
    }
}

@Composable
fun LayoutsCodelab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LayoutCodelab")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
        )
    }
}

//@Composable
//fun BodyContent(modifier: Modifier = Modifier) {
//    Column(modifier = modifier.padding(8.dp)) {
//        ImageList()
//    }
//}

@Composable
fun LayoutCodelabPreview() {
    ComposeLayoutTheme {
        LayoutsCodelab()
    }
}

@Composable
fun ImageListItem(idx: Int) {
    val imageUrl = "https://developer.android.com/images/brand/Android_Robot.png"
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "Item #$idx", style = MaterialTheme.typography.subtitle1)
    }
}

@Composable
fun ImageList() {
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text(text = "scroll to top")
            }

            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text(text = "scroll to bottom")
            }
        }
    }

    LazyColumn(state = scrollState) {
        items(listSize) {
            ImageListItem(it)
        }
    }
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(
        modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { }
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = .2f)
        ) {

        }

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)

            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
fun PhotographerCardPreview() {
    ComposeLayoutTheme {
        PhotographerCard()
    }
}

fun Modifier.firstBaselistToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Preview
@Composable
fun TextWithPaddingBaselinePreview() {
    ComposeLayoutTheme {
        Text(text = "Hi there!", Modifier.firstBaselistToTop(32.dp))
    }
}

@Preview
@Composable
fun TextWithPaddingNormalPreview() {
    ComposeLayoutTheme {
        Text(text = "Hi there!", Modifier.padding(top = 32.dp))
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measureables, constraints ->

        val placeables = measureables.map {
            it.measure(constraints)
        }

        var yPosition = 0

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach {
                it.placeRelative(x = 0, y = yPosition)
                yPosition += it.height
            }
        }
    }
}

@Preview
@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    MyOwnColumn(modifier.padding(8.dp)) {
        Text(text = "AAAAA")
        Text(text = "BBBB")
        Text(text = "CCCC")
        Text(text = "DDDD")
    }
}