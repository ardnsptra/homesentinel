#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

// Set these to run example.
#define FIREBASE_HOST "homesentinel-f0358-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "rGzyn5kaKuJLq7NH6Sl913Rt33yaXp7tobpXdhzg"
#define WIFI_SSID "INDOLOGIC"
#define WIFI_PASSWORD "myfox5054idthv"

#define IR_SENSOR_PIN 4

// Array of user names
const char* users[] = {"toni"};

void setup() {
  Serial.begin(9600);
  pinMode(IR_SENSOR_PIN, INPUT);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {
  bool irSensorValue = digitalRead(IR_SENSOR_PIN);

  // Invert the irSensorValue (1 if not detected, 0 if detected)
  irSensorValue = !irSensorValue;

  int firebaseValue = irSensorValue ? 0 : 1; // Convert boolean to 0 or 1
  
  for (int i = 0; i < sizeof(users) / sizeof(users[0]); i++) {
    // Set the status under the current user in the Firebase database
    Firebase.setInt(String("users/") + users[i] + "/status", firebaseValue);

    // Handle error for the current user
    if (Firebase.failed()) {
      Serial.print("Setting /users/");
      Serial.print(users[i]);
      Serial.print("/status failed: ");
      Serial.println(Firebase.error());
      return;
    }
  }

  delay(1000);  // Adjust delay as needed
}
