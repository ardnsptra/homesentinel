#include <Arduino.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>


#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"



// Insert your network credentials
#define WIFI_SSID "INDOLOGIC"
#define WIFI_PASSWORD "myfox5054idthv"

// Insert Firebase project API Key
#define API_KEY "Wylt9lOwZY4svvLgNe8E4QAXYmAMMhpjUgVMT8It"

// Insert RTDB URL
#define DATABASE_URL "homesentinelmotion-ea522-default-rtdb.firebaseio.com"

#define IR_SENSOR_PIN 2 // Connect the IR sensor to digital pin 2

// #define USER_EMAIL "USER_EMAIL"
// #define USER_PASSWORD "USER_PASSWORD"

// Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
int count = 0;
bool signupOK = false;

void setup() {
  Serial.begin(115200);
  pinMode(IR_SENSOR_PIN, INPUT);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  /* Assign the API key (required) */
  config.api_key = API_KEY;

  /* Assign the user sign in credentials */
  // auth.user.email = USER_EMAIL;
  // auth.user.password = USER_PASSWORD;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("Sign-up successful");
    signupOK = true;
  } else {
    Serial.printf("Sign-up failed: %s\n", config.signer.signupError.message.c_str());
  }

  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h


  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  int irSensorValue = digitalRead(IR_SENSOR_PIN);

  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 15000 || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();

    // Send IR sensor data to Firebase
    if (Firebase.RTDB.setInt(&fbdo, "ir_sensor/data", irSensorValue)) {
      Serial.println("IR sensor data sent to Firebase");
    } else {
      Serial.println("Failed to send IR sensor data to Firebase");
      Serial.println("REASON: " + fbdo.errorReason());
    }
  }
}
