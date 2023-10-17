#define IR_sensor1Pin A1 
#define IR_sensor2Pin A2 

int trigger_pin = 2;
int echo_pin = 3;
 
int IR_sensor1_Value;
int IR_sensor2_Value;
int time;
int distance; 
String values;

 void setup() { 
  
 pinMode (trigger_pin, OUTPUT); 
 
 pinMode (echo_pin, INPUT);
 
 //Initializes the serial connection at 9600 to sent sensor data to ESP8266.
 Serial.begin(9600); 
 
delay(2000);  
  
 }

 
void loop() {

  // get sensors data and put in to values variables as a string.
   values= (get_distance_Value()+','+get_IR_sensor1_Value()+','+get_IR_sensor2_Value());
       delay(1000);
       // removed any buffered previous serial data.
       Serial.flush();
       delay(1000);
       // sent sensors data to serial (sent sensors data to ESP8266)
       Serial.print(values);
       delay(2000);
 

}

// get Ultrasonic sensor data
String get_distance_Value(){  
 
 
    digitalWrite (trigger_pin, HIGH);

    delayMicroseconds (10);

    digitalWrite (trigger_pin, LOW);

    time = pulseIn (echo_pin, HIGH);

    distance = (time * 0.034) / 2;
      return String(distance);  
}

// get IR sensor 1 data
String get_IR_sensor1_Value(){ 
   
IR_sensor1_Value =analogRead(IR_sensor1Pin);
    delay(1000);
    return String(IR_sensor1_Value);  
    
}

// get IR sensor 2 data
String get_IR_sensor2_Value(){ 
   
IR_sensor2_Value =analogRead(IR_sensor2Pin);
delay(1000);
    return String(IR_sensor2_Value);  
}
 
  
