package edu.josakapp.proyectoJosakapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.josakapp.proyectoJosakapp.data.local.LocalDatasource

class RankingViewModelFactory(
    private val localDatasource: LocalDatasource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RankingViewModel(
            localDatasource = localDatasource
        ) as T
    }
}
