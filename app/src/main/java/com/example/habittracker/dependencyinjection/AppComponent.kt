package com.example.habittracker.dependencyinjection

import androidx.fragment.app.Fragment
import androidx.lifecycle.ReportFragment
import androidx.lifecycle.ViewModel
import com.example.habittracker.MainActivity
import com.example.habittracker.repository.HabitsProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(fragment: Fragment)
}