package com.example.tga.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tga.data.Result
import com.example.tga.data.GamesRepository
import com.example.tga.data.model.Game
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GamesViewModel(
 private val gamesRepository: GamesRepository
): ViewModel()
{
 private val _games = MutableStateFlow<List<Game>>(emptyList())
 val games = _games.asStateFlow()

 private val _showErrorToastChannel = Channel<Boolean>()
 val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

 init {
     viewModelScope.launch {
      gamesRepository.getGamesList().collectLatest { result ->
       when(result) {
        is Result.Error -> {
         _showErrorToastChannel.send(true)
        }
        is Result.Success -> {
         result.data?.let { games ->
          _games.update { games }
         }
        }
       }
      }
     }
 }
}