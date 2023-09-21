package project.homesentinel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TitleActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.title)

        val btlogin: Button = findViewById(R.id.btlogin)
        val btRegister: Button = findViewById(R.id.btResgiter)

        //tombol register di first_view ( menu awal )
        btRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //tombol login di first_view ( menu awal )
        btlogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}