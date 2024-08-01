package alvin.note

import alvin.note.core.service.AuthService
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var authService: AuthService
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
        setupToolbar()
        setupDestination()
        setupLogout()
    }

    private fun setupNavigation() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        navController = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.profileFragment), drawerLayout
        )

        val navView = findViewById<NavigationView>(R.id.navigationView)
        navView.setupWithNavController(navController)

        if (authService.getUid() != null) {
            navController.navigate(
                R.id.homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
            )
            findNavController(R.id.navHostFragment).navigate(R.id.homeFragment)
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    private fun setupDestination() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        navController.addOnDestinationChangedListener { _, dest, _ ->
            if (dest.id == R.id.loginFragment || dest.id == R.id.registerFragment) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
            }
        }
    }

    private fun setupLogout() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navigationView)
        navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            showLogoutConfirmationDialog()
            drawerLayout.close()
            true
        }
    }

    private fun showLogoutConfirmationDialog() {
        val alertView = layoutInflater.inflate(R.layout.alert_logout, null)
        val alertDialog = AlertDialog.Builder(this).setView(alertView).create()
        val btnCancel = alertView.findViewById<Button>(R.id.btnCancel)
        val btnLogout = alertView.findViewById<Button>(R.id.btnLogout)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnLogout.setOnClickListener {
            authService.logout()
            navController.navigate(
                R.id.loginFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, true)
                    .build()
            )
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onNavigateUp()
    }
}