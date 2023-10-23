package project.homesentinel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import project.homesentinel.databinding.RegisterBinding
import android.widget.Toast
import android.os.Handler
import android.util.Patterns
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class RegisterActivity: AppCompatActivity() {

    private lateinit var binding : RegisterBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = RegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://homesentinel-f0358-default-rtdb.firebaseio.com/")

        //text login di login (dibagian pling bawah)
        binding.rTxtLogin.setOnClickListener(){
            startActivity(Intent(this,LoginActivity::class.java))
        }

        //tombol back di login (bagian kiri atas)
        binding.rBtBack.setOnClickListener(){
            startActivity(Intent(this, TitleActivity::class.java))
        }

        //tombol register
        binding.rBtRegister.setOnClickListener(){
            val name = binding.rName.text.toString()
            val email = binding.rEmail.text.toString()
            val password = binding.rPassword.text.toString()

            //cek email valid apa kaga
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.rEmail.error = "Email Not Valid"
                binding.rEmail.requestFocus()
                return@setOnClickListener
            }

            //ngecek apakah ada data
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Data cannot be empty", Toast.LENGTH_SHORT).show()
                if (name.isEmpty()){
                    binding.rName.error = "Cannot be empty"
                    binding.rName.requestFocus()
                    return@setOnClickListener
                }
                if (email.isEmpty()){
                    binding.rEmail.error = "Cannot be empty"
                    binding.rEmail.requestFocus()
                    return@setOnClickListener
                }
                if (password.isEmpty()){
                    binding.rPassword.error = "Cannot be empty"
                    binding.rPassword.requestFocus()
                    return@setOnClickListener
                }

            }else{
                database = FirebaseDatabase.getInstance().getReference("users")
                database.child(name).child("username").setValue(name)
                database.child(name).child("email").setValue(email)
                database.child(name).child("password").setValue(password)
                Toast.makeText(this, "Success Register", Toast.LENGTH_SHORT).show()

                        Handler().postDelayed({
                            startActivity(Intent(this, LoginActivity::class.java))
                        }, 2000)

            }
        }
    }
}