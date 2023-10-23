int ledPin = 13;
int inputPin = 2;
int pirState = LOW;
int val = 0;
int pinSpeaker = 10;

void setup() {
  pinMode(ledPin, OUTPUT);
  pinMode(inputPin, INPUT);
  pinMode(pinSpeaker, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  val = digitalRead(inputPin);
  if (val == HIGH) {
    digitalWrite(ledPin, HIGH);
    playTone(300, 160);
    delay(100); // Kurangi delay setelah mendeteksi gerakan

    if (pirState == LOW) {
      Serial.println("Intruder detected! Secure your home."); // Pesan sesuai dengan pengamanan rumah
      pirState = HIGH;
    }
  } else {
    digitalWrite(ledPin, LOW);
    playTone(0, 0);
    delay(100); // Kurangi delay setelah gerakan berakhir

    if (pirState == HIGH) {
      Serial.println("No intruder detected. Home secure."); // Pesan sesuai dengan pengamanan rumah
      pirState = LOW;
    }
  }
}

void playTone(long duration, int freq) {
  duration *= 1000;
  int period = (1.0 / freq) * 1000000;
  long elapsed_time = 0;
  while (elapsed_time < duration) {
    digitalWrite(pinSpeaker, HIGH);
    delayMicroseconds(period / 2);
    digitalWrite(pinSpeaker, LOW);
    delayMicroseconds(period / 2);
    elapsed_time += (period);
  }
}
