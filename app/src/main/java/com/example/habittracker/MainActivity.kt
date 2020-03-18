package com.example.habittracker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class MainActivity :
    AppCompatActivity(),
    RecyclerViewFragment.RecyclerViewCallback,
    MainFragment.MainFragmentCallback,
    HabitManagementFragment.HabitManagementCallback{
    private lateinit var mainFragment: MainFragment
    private lateinit var habitManagementFragment: HabitManagementFragment

    companion object {
        private const val MAIN_FRAGMENT = "MAIN_FRAGMENT"
        private const val HABIT_MANAGEMENT_FRAGMENT = "HABIT_MANAGEMENT_FRAGMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        configureNavigationDrawer()
        if (!tryConfigureToolbarWithHomeButton())
            configureToolbarWithBackButton()
        instantiateSavedInstance(savedInstanceState)
    }

    private fun tryConfigureToolbarWithHomeButton(): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainFragment)
        if (isMenuFragment(currentFragment)){
            configureToolbarWithHomeButton()
            return true
        }
        return false
    }

    private fun configureToolbarWithHomeButton(){
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_menu_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureToolbarWithBackButton() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.home -> setHomeFragment()
                R.id.about -> setAboutFragment()
                else -> false
            }
        }
    }

    private fun setHomeFragment(): Boolean{
        supportFragmentManager.popBackStack()
        configureToolbarWithHomeButton()
        drawerLayout.closeDrawers()
        return true
    }

    private fun setAboutFragment(): Boolean{
        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainFragment)
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragment, AboutFragment())
            .addToBackStack(null)

        if (currentFragment !is AboutFragment)
            transaction.commit()

        configureToolbarWithHomeButton()
        drawerLayout.closeDrawers()
        return true
    }

    private fun isMenuFragment(fragment: Fragment?) =
        fragment == null || fragment is MainFragment || fragment is AboutFragment

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId;
        if (itemId == android.R.id.home)
        {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.mainFragment)
                if (isMenuFragment(currentFragment))
                    drawerLayout.openDrawer(GravityCompat.START)
            else {
                supportFragmentManager.popBackStackImmediate()
                if (!tryConfigureToolbarWithHomeButton())
                    configureToolbarWithBackButton()
            }
            return true
        }
        return true;
    }

    private fun instantiateSavedInstance(savedInstanceState: Bundle?){
        if (savedInstanceState == null) {
            mainFragment = MainFragment()
            habitManagementFragment = HabitManagementFragment()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFragment, mainFragment)
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, MAIN_FRAGMENT, mainFragment)
        if (habitManagementFragment.isAdded) {
            supportFragmentManager.putFragment(
                outState,
                HABIT_MANAGEMENT_FRAGMENT,
                habitManagementFragment
            )
        }
    }

    override fun onBackPressed() {
        if (mainFragment.isAdded && mainFragment.childFragmentManager.backStackEntryCount != 0){
            mainFragment.childFragmentManager.popBackStackImmediate()
            mainFragment.bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            return
        }
        if (supportFragmentManager.backStackEntryCount != 0) {
            supportFragmentManager.popBackStackImmediate()
            if (!tryConfigureToolbarWithHomeButton())
                configureToolbarWithBackButton()
        } else {
            super.onBackPressed();
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreFragments(savedInstanceState)
    }

    private fun restoreFragments(savedInstanceState: Bundle){
        mainFragment = supportFragmentManager.getFragment(
            savedInstanceState,
            MAIN_FRAGMENT
        ) as MainFragment

        val savedHabitManagementFragment = supportFragmentManager.getFragment(
            savedInstanceState,
            HABIT_MANAGEMENT_FRAGMENT
        )
        habitManagementFragment =
            if (savedHabitManagementFragment != null)
                savedHabitManagementFragment as HabitManagementFragment
            else HabitManagementFragment()
    }

    override fun onEditClickListener(habitId: UUID) {
        supportFragmentManager.beginTransaction().remove(habitManagementFragment)
        habitManagementFragment = HabitManagementFragment.newInstance(habitId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragment, habitManagementFragment)
            .addToBackStack(null)
            .commit()
        configureToolbarWithBackButton()
    }

    override fun onSaveClickListener() {
        supportFragmentManager.popBackStack()
        configureToolbarWithHomeButton()
    }

    override fun setFabOnClickListener() {
        if (habitManagementFragment.isAdded) {
            supportFragmentManager.beginTransaction().remove(habitManagementFragment)
        }
        habitManagementFragment = HabitManagementFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragment, habitManagementFragment)
            .addToBackStack(null)
            .commit()

        configureToolbarWithBackButton()
    }
}

