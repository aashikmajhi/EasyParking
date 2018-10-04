#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <Servo.h>

#define FIREBASE_HOST "easypark-fddcc.firebaseio.com"
#define FIREBASE_AUTH "yM7SR2kyHMFmJ5pFsocsEpeqiOzRa148Ni0mGqzL"
#define WIFI_SSID "ADYYTmFpeWE"
//#define WIFI_PASSWORD ""

Servo myservo; 

void setup() {
  Serial.begin(9600);
  myservo.attach(D0);
  // connect to wifi.
  WiFi.begin(WIFI_SSID);
  
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.setInt("motor_status", 0);
  myservo.write(0);
 
}

int n;
//float x;
void loop() {
 
  
  n=Firebase.getInt("motor_status");
  //x=Firebase.getFloat("number");
  Serial.println(n); 
  if (n==1) {
    Serial.println("Gate is open for 5 seconds");
    myservo.write(90);
    delay(5000);
    myservo.write(0);
    delay(1000);
    Firebase.setInt("motor_status", 0);
    delay(1000);
  }
  
  else {
  Serial.println("Gate is closed");
  
  }
  
  }
