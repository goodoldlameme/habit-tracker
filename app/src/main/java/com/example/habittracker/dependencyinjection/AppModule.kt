package com.example.habittracker.dependencyinjection

import android.content.Context
import com.example.habittracker.database.HabitsDatabase
import com.example.habittracker.model.ListHabitsViewModel
import com.example.habittracker.repository.HabitsProvider
import com.example.habittracker.repository.RoomHabitsProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule constructor(private val context: Context){
    @Provides
    @Singleton
    fun providesDatabase(): HabitsDatabase =
        HabitsDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providesHabitsProvider(roomHabitsProvider: RoomHabitsProvider): HabitsProvider = roomHabitsProvider
}