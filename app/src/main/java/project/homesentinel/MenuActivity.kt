package project.homesentinel

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.database.FirebaseDatabase


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        navigationView.bringToFront()
        val toggle = ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        // untuk mengambil username dan email yg telah login untuk ke welcome
        auth = FirebaseAuth.getInstance()
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        val welcomeMessage : TextView = findViewById(R.id.welcomename)
        val welcomeEmail : TextView = findViewById(R.id.emailWelcome)

        // Ambil data username dan email dari Intent
        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")

        welcomeMessage.text = "Welcome, $username!"
        welcomeEmail.text = email
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {}
            R.id.nav_notif -> {
                startActivity(Intent(this, NotifActivity::class.java))
            }
            R.id.nav_about -> {}
            R.id.nav_logout -> {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, "Success Logout", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}
