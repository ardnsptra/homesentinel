#define IR_sensorPin 2

int IR_sensor_Value;
int ledPin = 13;            // Pilih pin untuk LED
int pinSpeaker = 10;        // Menyiapkan speaker di pin PWM (digital 9, 10, atau 11)
int pirState = LOW;         // Kita mulai dengan asumsi tidak ada gerakan yang terdeteksi
int val = 0;                // Variabel untuk membaca status pin

void setup() {
  pinMode(ledPin, OUTPUT);      // Mendeklarasikan LED sebagai output
  pinMode(IR_sensorPin, INPUT); // Mendeklarasikan sensor sebagai input
  pinMode(pinSpeaker, OUTPUT);

  // Menginisialisasi koneksi serial pada 9600 untuk mengirimkan data sensor ke ESP8266 dan untuk Serial Monitor.
  Serial.begin(9600);
  delay(2000);
}

void loop() {
  IR_sensor_Value = digitalRead(IR_sensorPin); // Membaca nilai input dari sensor IR

  // Mengonversi nilai sensor IR menjadi boolean (0 atau 1).
  bool motionDetected = (IR_sensor_Value == HIGH);

  // Mengirim data sensor ke ESP8266 sebagai 0 atau 1.
  Serial.print(motionDetected);

  // Mengirim karakter baris baru untuk memisahkan data untuk ESP8266.
  Serial.println();

  if (motionDetected) {
    digitalWrite(ledPin, HIGH);   // Menghidupkan LED
    playTone(300, 160);
    delay(150);

    if (pirState == LOW) {
      // Kita baru saja menghidupkan
      pirState = HIGH;
    }
  } else {
    digitalWrite(ledPin, LOW); // Mematikan LED
    playTone(0, 0);
    delay(300);

    if (pirState == HIGH) {
      // Kita baru saja mematikan
      pirState = LOW;
    }
  }
}

// Durasi dalam mSek, frekuensi dalam hertz
void playTone(long duration, int freq) {
  duration *= 1000;
  int period = (1.0 / freq) * 1000000;
  long elapsed_time = 0;
  while (elapsed_time < duration) {
    digitalWrite(pinSpeaker, HIGH);
    delayMicroseconds(period / 2);
    digitalWrite(pinSpeaker, LOW);
    delayMicroseconds(period / 2);
    elapsed_time += period;
  }
}
