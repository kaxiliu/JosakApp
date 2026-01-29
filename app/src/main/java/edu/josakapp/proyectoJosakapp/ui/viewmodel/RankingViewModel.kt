package edu.josakapp.proyectoJosakapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.josakapp.proyectoJosakapp.data.model.UserRanking
import edu.josakapp.proyectoJosakapp.data.repository.RankingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RankingViewModel(
    private val repository: RankingRepository = RankingRepository()
) : ViewModel() {

    private val _ranking = MutableStateFlow<List<UserRanking>>(emptyList())
    val ranking: StateFlow<List<UserRanking>> = _ranking

    fun loadRanking() {
        viewModelScope.launch {
            val lista = repository.getRanking()
            _ranking.value = lista.sortedByDescending { it.puntos }
        }
    }

}
