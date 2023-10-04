package project.homesentinel

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import project.homesentinel.databinding.RegisterBinding
import android.widget.Toast
import android.os.Handler
import android.util.Patterns

@Suppress("DEPRECATION")
class RegisterActivity: AppCompatActivity() {

    lateinit var binding : RegisterBinding
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = RegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

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
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(){
                    if(it.isSuccessful){
                        Toast.makeText(this, "Success Register", Toast.LENGTH_SHORT).show()

                        Handler().postDelayed({
                            startActivity(Intent(this, LoginActivity::class.java))
                        }, 2000)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}