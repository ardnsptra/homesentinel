package project.homesentinel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import project.homesentinel.databinding.LoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError


class LoginActivity: AppCompatActivity() {

    lateinit var binding: LoginBinding
    lateinit var auth : FirebaseAuth
    lateinit var database : DatabaseReference

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
            val name = binding.lName.text.toString()
            val password = binding.lPassword.text.toString()

            if (name.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Data cannot be empty", Toast.LENGTH_SHORT).show()
                if (name.isEmpty()){
                    binding.lName.error = "Cannot be empty"
                    binding.lName.requestFocus()
                    return@setOnClickListener
                }
                if (password.isEmpty()){
                    binding.lPassword.error = "Cannot be empty"
                    binding.lPassword.requestFocus()
                    return@setOnClickListener
                }
            }else{
                database = FirebaseDatabase.getInstance().getReference("users")
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.child(name).exists()) {
                            if (dataSnapshot.child(name).child("password").getValue(String::class.java) == password){
                                Toast.makeText(applicationContext, "Success Login", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(applicationContext, MenuActivity::class.java))
                                return
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "Data Not Found", Toast.LENGTH_SHORT).show()
                    }
                })

//                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(this, "Success Login", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this, MenuActivity::class.java))
//                    }else{
//                        Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
//                    }
//                }
            }
        }

    }
}