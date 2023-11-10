#include <Servo.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <SPI.h>
#include <ArduinoJson.h>

IPAddress server(192, 168, 0, 174);
Servo servo;

//////////////////////    WIFI Settings
const char* ssid = "Your SSID";
const char* password = "Your Password";

// Replace with your MQTT broker information
const char* mqtt_server = "Your IP";
const char* mqtt_username = "mqttName";
const int mqtt_port = 1883;
const char* mqtt_password = "mqttPass";
const char* mqtt_topic = "actuator_order";

// Replace with the device settings
const char* device_name = "eagle";
const char* device_id = "1";
const char* actuator_id = "4";

// Actuator Settings
const int outPin = D1; // Define the output pin
const int outLed = D0; // Define the output pin
int state; // 0 will mean open and 180 will mean closed
char payload[10];

// Initialize the Wi-Fi client and MQTT client
WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
  // Start serial communication
  Serial.begin(9600);
  state = 0;

  // Set up the ports
  pinMode(outPin, OUTPUT); // Sets the pin as an Output
  pinMode(outLed, OUTPUT);

  // Connect to Wi-Fi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");

  // Set up MQTT
  client.setServer(server, mqtt_port);  
  client.setCallback(callback);
}

void openBin(){
  for (state = 0; state <= 180; state += 1) {
    servo.write(state);
    delay(15);
  }
  digitalWrite(outLed, HIGH);
  Serial.println("Done");
}

void closeBin(){
  for (state = 180; state >= 0; state -= 1) {
    servo.write(state);
    delay(15);
  }
  digitalWrite(outLed, LOW);
  Serial.println("Done");
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message received on topic: ");
  Serial.println(topic);

  // Convert the payload to a string
  String message = "";
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }

  // Print the received message
  Serial.print("Message: ");
  Serial.println(message);
	Serial.println();  
  if (message == "open"){
    openBin();
  } else if(message == "close"){
    closeBin();
  } else {
    Serial.println("Order not found");
  }
}

void reconnect() {  
	while (! client.connected()) {    
		Serial.println("Connecting to the MQTT server");    
		if (client.connect("eagle", mqtt_username, mqtt_password)) {      
			Serial.println("Connected");   
      client.subscribe(mqtt_topic); 
      
		} else {      
			Serial.print("Error, rc=");      
			Serial.print(client.state());      
			Serial.println(" Trying again in 5 seconds");         
		}  
	}
}



void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  delay(5000);
}