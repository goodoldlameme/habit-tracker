package com.example.habittracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habittracker.models.database.HabitEntity

@Database(entities = [HabitEntity::class], version = 1)
abstract class HabitsDatabase: RoomDatabase(){
    companion object{
        private const val DB_NAME = "HabitsDatabase"

        fun getInstance(context: Context): HabitsDatabase =
            Room.databaseBuilder(context, HabitsDatabase::class.java, DB_NAME)
                .build()
    }
    abstract fun getHabitsDao(): HabitsDao
}