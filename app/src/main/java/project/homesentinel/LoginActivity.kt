package project.homesentinel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import project.homesentinel.databinding.LoginBinding

class LoginActivity: AppCompatActivity() {

    lateinit var binding: LoginBinding
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = LoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        //tombol back di login (bagian kiri atas)
        binding.lBtBack.setOnClickListener(){
            startActivity(Intent(this, TitleActivity::class.java))
        }
        //tulisan register di bawah
        binding.lTxtRegister.setOnClickListener(){
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //tombol login
        binding.lBtlogin.setOnClickListener(){
            val email = binding.lEmail.text.toString()
            val password = binding.lPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){
                    if(it.isSuccessful){
                        startActivity(Intent(this, MenuActivity::class.java))
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}