package com.example.habittracker

import android.app.Application
import com.example.habittracker.database.HabitsDatabase

class App : Application() {
    companion object {
        lateinit var instance: App private set
        lateinit var database: HabitsDatabase private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = HabitsDatabase.getInstance(applicationContext)
    }
}

