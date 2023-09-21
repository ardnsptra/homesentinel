package project.homesentinel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        

        val btRegister: Button = findViewById(R.id.r_btRegister)
        val txtLogin: TextView = findViewById(R.id.r_txtLogin)
        val btBack: ImageView = findViewById(R.id.r_btBack)

        //text login di login (dibagian pling bawah)
        txtLogin.setOnClickListener(){
            startActivity(Intent(this,LoginActivity::class.java))
        }

        //tombol back di login (bagian kiri atas)
        btBack.setOnClickListener(){
            startActivity(Intent(this, TitleActivity::class.java))
        }
    }
}