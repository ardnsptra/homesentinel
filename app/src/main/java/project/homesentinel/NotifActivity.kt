package project.homesentinel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import project.homesentinel.databinding.NotifBinding

class NotifActivity : AppCompatActivity() {

    private lateinit var binding: NotifBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = NotifBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //tombol back di login (bagian kiri atas)
        binding.btBackNotif.setOnClickListener(){
            startActivity(Intent(this, MenuActivity::class.java))
        }

        // Mengambil nama pengguna dari SharedPreferences.
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        if (username.isNotEmpty()) {
            // Menggunakan nama pengguna untuk mengidentifikasi node pengguna di Firebase Realtime Database.
            database = FirebaseDatabase.getInstance().getReference("users").child(username).child("status")


            binding.btNotifOff.setOnClickListener {
                // Ketika tombol "Notif Off" ditekan, ubah notifStatus pengguna menjadi 0 di Firebase Realtime Database.
                database.setValue(0)
            }

            binding.btNotifOn.setOnClickListener {
                // Ketika tombol "Notif On" ditekan, ubah notifStatus pengguna menjadi 1 di Firebase Realtime Database.
                database.setValue(1)
            }
        }
    }
}
