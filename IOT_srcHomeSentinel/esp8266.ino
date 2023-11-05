#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_AUTH "Your secret"
#define FIREBASE_HOST "your FIREBASE HOST"
#define WIFI_SSID "your WIFI SSID"
#define WIFI_PASSWORD "your WIFI PASSWORD"

String values, sensor_data;

void setup() {
  // Initializes the serial connection at 9600 to get sensor data from Arduino.
  Serial.begin(9600);
  delay(1000);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {
  bool Sr = false;

  while (Serial.available()) {
    // Get sensor data from serial and put it in sensor_data.
    sensor_data = Serial.readString();
    Sr = true;
  }

  delay(1000);

  if (Sr == true) {
    values = sensor_data;

    // Get comma index from the values variable
    int firstCommaIndex = values.indexOf(',');

    // Get sensor data from values variable by splitting it by commas and store in variables
    String IR_sensor_value = values.substring(0, firstCommaIndex);

    // Store IR sensor  data as a string in Firebase
    Firebase.setString("IR_sensor_value", IR_sensor_value);
    delay(10);

    // Store previous sensors data as a string in Firebase
    Firebase.pushString("previous_IR_sensor_value", IR_sensor_value);
    delay(10);

    delay(1000);

    if (Firebase.failed()) {
      return;
    }
  }
}
