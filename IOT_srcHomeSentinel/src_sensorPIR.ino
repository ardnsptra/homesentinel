int buzz = 13;
int pir = 8;
int val;

void setup() {
  pinMode (buzz, OUTPUT);
  pinMode (pir, INPUT);
  // put your setup code here, to run once:

}

void loop() {
  val = digitalREad(pir);
  if (val == 1){
    digitalWrite(buzz,1);
  }
  else{
    digitalWrite(buzz,0);
  }
  // put your main code here, to run repeatedly:

}

#include <SoftwareSerial.h>  // Mengimpor library SoftwareSerial untuk komunikasi serial dengan ESP8266

SoftwareSerial espSerial(2, 3); // RX, TX (sesuaikan pinnya)
int pirPin = 8; // Pin sensor PIR
bool motionDetected = false; // Variabel untuk melacak deteksi gerakan

void setup() {
  pinMode(pirPin, INPUT); // Mengatur pin sensor PIR sebagai input
  Serial.begin(9600); // Inisialisasi komunikasi serial dengan PC
  espSerial.begin(9600); // Inisialisasi komunikasi serial dengan ESP8266
}

void loop() {
  int pirValue = digitalRead(pirPin); // Membaca nilai dari sensor PIR

  if (pirValue == HIGH) { // Jika sensor PIR mendeteksi gerakan
    motionDetected = true; // Set variabel motionDetected menjadi true
    Serial.println("Motion detected!"); // Mencetak pesan ke monitor serial
  } else {
    motionDetected = false; // Set variabel motionDetected menjadi false
  }

  if (motionDetected) { // Jika gerakan terdeteksi
    espSerial.println("Motion detected!"); // Kirim data ke ESP8266
    delay(1000); // Hindari pengiriman berulang dalam waktu singkat
  }

  // Lakukan pengiriman data ke database di ESP8266 di sini (gunakan AT command atau protokol lainnya)
}

#include <ESP8266WiFi.h>  // Mengimpor library ESP8266WiFi untuk mengelola koneksi WiFi
#include <FirebaseArduino.h>  // Mengimpor library FirebaseArduino untuk mengirim data ke Firebase

#define FIREBASE_HOST "your-firebase-url.firebaseio.com"  // Ganti dengan URL Firebase Anda
#define FIREBASE_AUTH "your-firebase-auth-key"  // Ganti dengan kunci otentikasi Firebase Anda

void setup() {
  Serial.begin(9600);  // Inisialisasi komunikasi serial dengan kecepatan 9600 bps
  WiFi.begin("your-ssid", "your-password");  // Menghubungkan ESP8266 ke jaringan WiFi

  // Tunggu hingga koneksi WiFi berhasil
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);  // Inisialisasi koneksi Firebase
}

void loop() {
  if (Serial.available()) {  // Jika ada data yang tersedia di komunikasi serial
    String data = Serial.readStringUntil('\n');  // Baca data dari komunikasi serial hingga karakter '\n'
    Firebase.setString("motion", data);  // Kirim data ke Firebase dengan nama 'motion'
    Serial.println("Data sent to Firebase: " + data);  // Cetak pesan ke monitor serial
  }
}


