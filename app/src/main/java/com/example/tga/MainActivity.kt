package com.example.tga

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.tga.data.GamesRepositoryImpl
import com.example.tga.data.model.Game
import com.example.tga.presentation.GamesViewModel
import com.example.tga.ui.theme.TGATheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<GamesViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GamesViewModel(GamesRepositoryImpl(RetrofitInstance.api)) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TGATheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    val gameList = viewModel.games.collectAsState().value
                    val context = LocalContext.current

                    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
                        viewModel.showErrorToastChannel.collectLatest { show ->
                            if (show) {
                                Toast.makeText(
                                    context, "Error", Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    if (gameList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(gameList.size) { index ->
                                Game(gameList[index])
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                        }
                    }

                }

            }
        }
    }
}

@Composable
fun Game(game: Game) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(game.thumbnail)
            .size(Size.ORIGINAL).build()
    ).state

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .height(300.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (imageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                painter = imageState.painter,
                contentDescription = game.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "${game.title} - Genre: ${game.genre}",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            text = "${game.short_description}",
            fontSize = 13.sp,
        )
    }
}