package project.homesentinel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout

class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        val menuButton: ImageView = findViewById(R.id.menu_tombol)

        menuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView)
                menuButton.setImageResource(R.drawable.tombol_menu)
            } else {
                drawerLayout.openDrawer(navigationView)
                menuButton.setImageResource(R.drawable.backarrow)
            }
        }
    }
}