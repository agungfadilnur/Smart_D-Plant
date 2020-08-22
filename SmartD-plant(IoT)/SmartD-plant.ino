#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <DHT.h>

//Firebase
#define FIREBASE_HOST "findit-d7219.firebaseio.com"
#define FIREBASE_AUTH "Ui94xxAIJb0MDzqmTUVQ3IrruevCTGLfE4ACT72q"

//set Wifi
#define WIFI_SSID "Jet Food"
#define WIFI_PASSWORD "agungnf16"

//value tanah
#define Tanah

//set sensor DHT22
#define DHTPIN D4
#define DHTTYPE DHT22
DHT dht(DHTPIN, DHTTYPE);

//Pin Led (Dry=basah-Wet=Kering)
int Dry = D6;
int Wet = D8;

void setup() {
  Serial.begin(115200);
  delay(500);

  //Mode Pin  
  pinMode(Dry, OUTPUT);
  pinMode(Wet, OUTPUT);

  //Koneksi Wifi
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
  dht.begin();
}
double analogValue = 0.0;
double analogVolts = 0.0;
unsigned long timeHolder = 0;

void loop() {
  //Sensor DHT 22
  //Baca data
  float h = dht.readHumidity();
  float t = dht.readTemperature();

  //cek sensor  
 

  Serial.print("Humidity: ");
  Serial.print(h);
  int fireHumid = int(h) + int("%"); //convert integer humidity to string humidity 
  Serial.print("%  Temperature: ");
  Serial.print(t);
  Serial.println(" °C ");
  int fireTemp = int(t) + int(" °C"); //convert integer temperature to string temperature
  delay(100);

  Firebase.setInt("Monitoring/SensorSuhu/Humidity", h); //setup path and send readings
  Firebase.setInt("Monitoring/SensorSuhu/Temperature", t); //setup path and send readings

  //Sensor Tanah
  //Baca sinyal sensor
  analogValue = analogRead(A0);
  analogVolts = (analogValue * 3.08) / 1024;

  //Konversi data analog ke persen
  int chartValue = (analogValue * 100) / 1024;
  chartValue = 100 - chartValue;
  if (millis() - 100 > timeHolder) {
    timeHolder = millis();

    if (analogValue > 700) {
      digitalWrite(Dry, HIGH);
      digitalWrite(Wet, LOW);
      Firebase.setInt("Monitoring/SensorTanah/Kondisi", LOW);
      Serial.println("Tanah Kering");

    } else if (analogValue < 650) {
      digitalWrite(Dry, LOW);
      digitalWrite(Wet, HIGH);
      Firebase.setInt("Monitoring/SensorTanah/Kondisi", HIGH);
      Serial.println("Tanah Lembab");

    }

    delay(100);
    Firebase.setInt("Monitoring/SensorTanah/Kelembaban", chartValue);
    Serial.print("Data: ");
    Serial.println(analogValue);
    Serial.print("Kelembaban: ");
    Serial.println(chartValue);
    Serial.println(" ");
    delay(100);
  }
}
