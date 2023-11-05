package project.homesentinel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import project.homesentinel.databinding.NotifBinding
import java.util.Random
import java.util.Timer
import java.util.TimerTask

class NotifActivity : AppCompatActivity() {

    private lateinit var binding: NotifBinding
    private lateinit var database: DatabaseReference

    // Penandaan untuk notifikasi dan RecyclerView
    private val notificationList = mutableListOf<NotificationItem>()
    private lateinit var notificationAdapter: NotificationAdapter
    private val handler = Handler()
    private val updateTimeDelay = 5000L // Perbarui setiap 5 detik (5000 ms)

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            updateNotificationTime()
            handler.postDelayed(this, updateTimeDelay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = NotifBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Mengambil nama pengguna dari SharedPreferences.
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        if (username.isNotEmpty()) {
            // Menggunakan nama pengguna untuk mengidentifikasi node pengguna di Firebase Realtime Database.
            database = FirebaseDatabase.getInstance().getReference("users").child(username).child("status")

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val status = dataSnapshot.getValue(Int::class.java)
                    if (status == 1) {
                        // Status berubah menjadi 1, buat dan tampilkan notifikasi
                        sendNotification("The sensor detects something")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error here
                    Toast.makeText(this@NotifActivity, "Database Error: " + databaseError.message, Toast.LENGTH_SHORT).show()
                }
            })

            // Inisialisasi RecyclerView
            val notificationRecyclerView = findViewById<RecyclerView>(R.id.historyNotif)
            notificationRecyclerView.layoutManager = LinearLayoutManager(this)
            notificationAdapter = NotificationAdapter(notificationList)
            notificationRecyclerView.adapter = notificationAdapter

            binding.btNotifOff.setOnClickListener {
                // Ketika tombol "Notif Off" ditekan, ubah notifStatus pengguna menjadi 0 di Firebase Realtime Database.
                database.setValue(0)
            }

            binding.btNotifOn.setOnClickListener {
                // Ketika tombol "Notif On" ditekan, ubah notifStatus pengguna menjadi 1 di Firebase Realtime Database.
                database.setValue(1)
            }
            handler.postDelayed(updateTimeRunnable, updateTimeDelay)
        }
    }
    private fun updateNotificationTime() {
        val currentTime = System.currentTimeMillis()

        for (notification in notificationList) {
            val timeAgo = DateUtils.getRelativeTimeSpanString(
                notification.time,
                currentTime,
                DateUtils.MINUTE_IN_MILLIS
            )
            notification.timeAgo = timeAgo.toString()
        }

        notificationAdapter.notifyDataSetChanged()
    }

    private fun sendNotification(message: String) {
        val channelId = "Notif"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Home Sentinel")
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = Random().nextInt(1000)
        notificationManager.notify(notificationId, notificationBuilder.build())

        // Tambahkan notifikasi ke daftar notifikasi bersama dengan waktu notifikasi
        val currentTime = System.currentTimeMillis()
        val newNotification = NotificationItem(notificationId, "Sesuatu Terdeteksi", "Sensor Mendeteksi Sesuatu, Segera di Cek", currentTime, "")
        notificationList.add(0, newNotification) // Menambahkannya di awal daftar untuk menampilkan yang terbaru pertama
        notificationAdapter.notifyItemInserted(0)
    }
}
