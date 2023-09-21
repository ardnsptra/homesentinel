package project.homesentinel

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val btBack : ImageView = findViewById(R.id.l_btBack)
        val txtRegister: TextView = findViewById(R.id.l_txtRegister)

        //tombol back di login (bagian kiri atas)
        btBack.setOnClickListener(){
            startActivity(Intent(this, TitleActivity::class.java))
        }

        txtRegister.setOnClickListener(){
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }
}