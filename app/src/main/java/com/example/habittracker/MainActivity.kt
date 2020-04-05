package com.example.habittracker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.habittracker.fragments.AboutFragment
import com.example.habittracker.fragments.HabitManagementFragment
import com.example.habittracker.fragments.MainFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class MainActivity :
    AppCompatActivity(),
    RecyclerViewFragment.RecyclerViewCallback,
    MainFragment.MainFragmentCallback,
    HabitManagementFragment.HabitManagementCallback{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        instantiateFragments()
        configureNavigationDrawer()
        if (!tryConfigureToolbarWithHomeButton())
            configureToolbarWithBackButton()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
        {
            if (isNullOrMenuFragment(supportFragmentManager.findFragmentById(R.id.mainFragment)))
                drawerLayout.openDrawer(GravityCompat.START)
            else {
                supportFragmentManager.popBackStackImmediate()
                if (!tryConfigureToolbarWithHomeButton())
                    configureToolbarWithBackButton()
            }
        }
        return true;
    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.mainFragment)?.let{ fragment ->
            if (fragment is MainFragment && fragment.childFragmentManager.backStackEntryCount != 0){
                fragment.childFragmentManager.popBackStackImmediate()
                fragment.bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                return
            }
        }
        super.onBackPressed();
        if (!tryConfigureToolbarWithHomeButton())
            configureToolbarWithBackButton()
    }

    override fun onEditClickListener(habitId: UUID) =
        updateAndConfigureToolBarWithBackButton(HabitManagementFragment.newInstance(habitId))

    override fun onSaveClickListener() {
        supportFragmentManager.popBackStack()
        configureToolbarWithHomeButton()
    }

    override fun setFabOnClickListener() =
        updateAndConfigureToolBarWithBackButton(HabitManagementFragment())

    private fun tryConfigureToolbarWithHomeButton(): Boolean {
        if (isNullOrMenuFragment(supportFragmentManager.findFragmentById(R.id.mainFragment))){
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
        if (supportFragmentManager.findFragmentById(R.id.mainFragment) !is AboutFragment)
            updateFragment(AboutFragment(), true)

        configureToolbarWithHomeButton()
        drawerLayout.closeDrawers()
        return true
    }

    private fun isMenuFragment(fragment: Fragment?): Boolean =
        fragment is MainFragment || fragment is AboutFragment

    private fun isNullOrMenuFragment(fragment: Fragment?): Boolean =
        fragment == null || isMenuFragment(fragment)

    private fun instantiateFragments(){
        if (supportFragmentManager.findFragmentById(R.id.mainFragment) == null)
            updateFragment(MainFragment())
    }

    private fun updateAndConfigureToolBarWithBackButton(fragment: Fragment){
        updateFragment(fragment, true)
        configureToolbarWithBackButton()
    }

    private fun updateFragment(fragment: Fragment, addToBackStack: Boolean = false){
        var transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainFragment, fragment)

        if (addToBackStack)
            transaction = transaction.addToBackStack(null)

        transaction.commit()
    }
}

