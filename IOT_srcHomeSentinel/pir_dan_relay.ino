/*=======================================


relay di pin 3
PIR di pin 2



================================================================*/ 


#define RELAY_ON 0
#define RELAY_OFF 1
#define RELAY_1  3   // pin

int indikator = 13; // buat indikator LED
int inputVout = 2; // Vout PIR
int statusPIR = 0; // status logical
int data = 0; // variabel temporary utk menampung data PIR 


 
 
void setup() {
pinMode(indikator, OUTPUT); //set pin 13 sbg output
pinMode(inputVout, INPUT); // set pin 2 sbg input
Serial.begin(9600); //serial monitor


    

  // Set pin as output.
  pinMode(RELAY_1, OUTPUT); 
  // Initialize relay one as off so that on reset it would be off by default
  digitalWrite(RELAY_1, RELAY_OFF);
} 
 
void loop(){
data = digitalRead(inputVout); // baca input dr Vout
if ((data == HIGH) && (statusPIR == LOW)) { // cek jika ada pergerakan
       digitalWrite(indikator, HIGH); // nyalakan led indikator di board Arduino
        Serial.println("Motion detected!"); //buat monitor ke laptop
         statusPIR = HIGH; //diset high spy tdk mendeteksi terus
         
         digitalWrite(RELAY_1, RELAY_ON);
         delay(100);
         digitalWrite(RELAY_1, RELAY_OFF);
         delay(500);
         
 
} else {
  if ((data == LOW) && (statusPIR == HIGH)){
   digitalWrite(indikator, LOW); // matikan led indikator
   Serial.println("Motion ended!"); //buat monitor ke laptop
   statusPIR = LOW;
   }
 }
}

