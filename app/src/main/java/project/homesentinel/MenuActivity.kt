package project.homesentinel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import project.homesentinel.databinding.MenuBinding
import project.homesentinel.databinding.NotifBinding
import java.util.Random


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference

    // untuk notifikasi dan RecyclerView
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val drawerLayout :DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView :NavigationView = findViewById(R.id.nav_view)
        val toolbar :Toolbar= findViewById(R.id.toolbar)
        val btOn :Button = findViewById(R.id.btNotifOn)
        val btOff :Button = findViewById(R.id.btNotifOff)

        setSupportActionBar(toolbar)
        auth = FirebaseAuth.getInstance()

        // untuk mengambil username dan email yg telah login untuk ke welcome
//        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        val welcomeMessage: TextView = findViewById(R.id.welcomename)
        val welcomeEmail: TextView = findViewById(R.id.emailWelcome)

        // Ambil data username dan email
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        val email = sharedPreferences.getString("email", "") // Ambil email dari SharedPreferences

        welcomeMessage.text = "Welcome, $username!"
        welcomeEmail.text = email

        // Cek apakah udah login
        if (username.isNotEmpty()) {
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
                    Toast.makeText(
                        this@MenuActivity,
                        "Database Error: " + databaseError.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            // Inisialisasi RecyclerView
            val notificationRecyclerView : RecyclerView= findViewById(R.id.historyNotif)
            notificationRecyclerView.layoutManager = LinearLayoutManager(this)
            notificationAdapter = NotificationAdapter(notificationList)
            notificationRecyclerView.adapter = notificationAdapter

            btOff.setOnClickListener {
                // Ketika tombol "Notif Off" ditekan, ubah notifStatus pengguna menjadi 0 di Firebase Realtime Database.
                database.setValue(0)
            }

            btOn.setOnClickListener {
                // Ketika tombol "Notif On" ditekan, ubah notifStatus pengguna menjadi 1 di Firebase Realtime Database.
                database.setValue(1)
            }
            handler.postDelayed(updateTimeRunnable, updateTimeDelay)

            // navigasi menu
            navigationView.bringToFront()
            val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navigationView.setNavigationItemSelectedListener(this)
        }
    }

    // update notid
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

    // send notif
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

    // pilihan navigasi menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {}
            R.id.nav_notif -> {
                startActivity(Intent(this, NotifActivity::class.java))
            }
            R.id.nav_about -> {}
            R.id.nav_logout -> {
                auth.signOut()
                // Hapus data dari SharedPreferences saat logout
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().remove("username").apply()

                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, "Success Logout", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}
