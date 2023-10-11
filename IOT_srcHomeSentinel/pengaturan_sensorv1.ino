#include <Arduino.h>
#include <SoftwareSerial.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

int buzz = 13;      // Pin buzzer
int pirPin = 8;     // Pin sensor PIR
int val;            // Variabel untuk menyimpan nilai sensor PIR
bool motionDetected = false; // Variabel untuk melacak deteksi gerakan

SoftwareSerial espSerial(2, 3); // RX, TX (sesuaikan pinnya)

#define FIREBASE_HOST "https://homesentinel-f0358-default-rtdb.firebaseio.com/" // Ganti dengan URL Firebase Anda
#define FIREBASE_AUTH "AIzaSyCXSQqn1Ir2rpd5pW13qp1nFNIWluDQBPg" // Ganti dengan kunci otentikasi Firebase Anda

void setup() {
  pinMode(buzz, OUTPUT); // Mengatur pin buzzer sebagai OUTPUT
  pinMode(pirPin, INPUT); // Mengatur pin sensor PIR sebagai INPUT

  Serial.begin(9600); // Inisialisasi komunikasi serial dengan kecepatan 9600 bps
  espSerial.begin(9600); // Inisialisasi komunikasi serial dengan ESP8266

  WiFi.begin("your-ssid", "your-password"); // Menghubungkan ESP8266 ke jaringan WiFi

  // Tunggu hingga koneksi WiFi berhasil
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH); // Inisialisasi koneksi Firebase
}

void loop() {
  val = digitalRead(pirPin); // Membaca nilai dari sensor PIR

  if (val == HIGH) { // Jika sensor PIR mendeteksi gerakan
    motionDetected = true; // Set variabel motionDetected menjadi true
    Serial.println("Motion detected!"); // Cetak pesan ke monitor serial
    digitalWrite(buzz, HIGH); // Hidupkan buzzer
  } else {
    motionDetected = false; // Set variabel motionDetected menjadi false
    digitalWrite(buzz, LOW); // Matikan buzzer
  }

  if (motionDetected) { // Jika gerakan terdeteksi
    espSerial.println("Motion detected!"); // Kirim data ke ESP8266
    delay(1000); // Hindari pengiriman berulang dalam waktu singkat
  }

  if (Serial.available()) { // Jika ada data yang tersedia di komunikasi serial
    String data = Serial.readStringUntil('\n'); // Baca data dari komunikasi serial hingga karakter '\n'
    Firebase.setString("motion", data); // Kirim data ke Firebase dengan nama 'motion'
    Serial.println("Data sent to Firebase: " + data); // Cetak pesan ke monitor serial
  }
}
