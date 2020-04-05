package com.example.habittracker.models.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.repository.RoomHabitsProvider

object ListViewModelProviderFactory : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListHabitsViewModel(
            RoomHabitsProvider
        ) as T
    }
}