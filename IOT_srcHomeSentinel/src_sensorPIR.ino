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
