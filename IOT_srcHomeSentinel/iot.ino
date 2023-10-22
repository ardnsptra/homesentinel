#define IR_sensorPin 2

int IR_sensor_Value;
int ledPin = 13;            // choose the pin for the LED
int pinSpeaker = 10;        // Set up a speaker on a PWM pin (digital 9, 10, or 11)
int pirState = LOW;         // We start, assuming no motion detected
int val = 0;                // Variable for reading the pin status
String values;

void setup() {
  pinMode(ledPin, OUTPUT);      // Declare LED as output
  pinMode(IR_sensorPin, INPUT); // Declare sensor as input
  pinMode(pinSpeaker, OUTPUT);

  // Initializes the serial connection at 9600 to send sensor data to ESP8266.
  Serial.begin(9600);
  delay(2000);
}

void loop() {
  // Get sensor data and put it into the 'values' variable as a string.
  values = get_IR_sensor_Value();
  delay(1000);

  // Remove any buffered previous serial data.
  Serial.flush();
  delay(1000);

  // Send sensor data to serial (send sensor data to ESP8266).
  Serial.print(values);
  delay(2000);

  val = digitalRead(IR_sensorPin); // Read input value
  if (val == HIGH) {              // Check if the input is HIGH
    digitalWrite(ledPin, HIGH);   // Turn LED ON
    playTone(300, 160);
    delay(150);

    if (pirState == LOW) {
      // We have just turned on
      Serial.println("Motion detected!");
      // We only want to print on the output change, not state
      pirState = HIGH;
    }
  } else {
    digitalWrite(ledPin, LOW); // Turn LED OFF
    playTone(0, 0);
    delay(300);

    if (pirState == HIGH) {
      // We have just turned off
      Serial.println("Motion ended!");
      // We only want to print on the output change, not state
      pirState = LOW;
    }
  }
}

// Duration in mSecs, frequency in hertz
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

// Get IR sensor data
String get_IR_sensor_Value() {
  IR_sensor_Value = analogRead(IR_sensorPin);
  delay(1000);
  return String(IR_sensor_Value);
}
