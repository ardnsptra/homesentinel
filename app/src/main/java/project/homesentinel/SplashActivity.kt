package project.homesentinel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Cek apakah pengguna sudah login sebelumnya
        val username = sharedPreferences.getString("username", null)

        if (username != null) {
            // Jika pengguna sudah login, langsung lanjutkan ke MenuActivity setelah delay
            Handler().postDelayed({
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        } else {
            // Jika pengguna belum login, arahkan ke TitleActivity setelah delay
            Handler().postDelayed({
                val intent = Intent(this, TitleActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }

    }
}