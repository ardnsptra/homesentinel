#include <SoftwareSerial.h>

#define IR_sensorPin 2

int IR_sensor_Value;
int ledPin = 13;
int pinSpeaker = 10;
int pirState = LOW;
int val = 0;

void setup() {
  pinMode(ledPin, OUTPUT);
  pinMode(IR_sensorPin, INPUT);
  pinMode(pinSpeaker, OUTPUT);
  Serial.begin(9600);
  delay(2000);
}

void loop() {
  IR_sensor_Value = digitalRead(IR_sensorPin);
  bool motionDetected = (IR_sensor_Value == HIGH);

  Serial.println(motionDetected);

  if (motionDetected) {
    digitalWrite(ledPin, HIGH);
    playTone(300, 160);
    delay(150);

    if (pirState == LOW) {
      pirState = HIGH;
    }
  } else {
    digitalWrite(ledPin, LOW);
    playTone(0, 0);
    delay(300);

    if (pirState == HIGH) {
      pirState = LOW;
    }
  }
}

void playTone(long duration, int freq) {
  duration *= 1000;
  int period = 1000000 / freq;  // Simplified calculation
  long elapsed_time = 0;

  while (elapsed_time < duration) {
    digitalWrite(pinSpeaker, HIGH);
    delayMicroseconds(period / 2);
    digitalWrite(pinSpeaker, LOW);
    delayMicroseconds(period / 2);
    elapsed_time += period;
  }
}