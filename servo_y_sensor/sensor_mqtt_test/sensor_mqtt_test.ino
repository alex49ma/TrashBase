#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <SPI.h>
#include <ArduinoJson.h>

IPAddress server(192, 168, 0, 174);

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
const char* sensor_id = "4";
int state;

// Ultrasonic sensor settings
const int trigPin = D0; // Define the trigger pin
const int echoPin = D1; // Define the echo pin

// Variables to store distance and previous distance
int duration;
long distance = 0;
long prevDistance = 0;
char payload[10];

// Initialize the Wi-Fi client and MQTT client
WiFiClient espClient;
PubSubClient client(espClient);

void setup() {
  // Start serial communication
  Serial.begin(9600);
  state = 0;

  // Set up the ports
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input


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

void callback(char* topic, byte* payload, unsigned int length) {
	Serial.print("Mensage received [");  
	Serial.print(topic);  
	Serial.print("] ");
	String message = String((char *) payload);  
	Serial.print(message);  
	Serial.println();  
}

void reconnect() {  
	while (! client.connected()) {    
		Serial.println("Connecting to the MQTT server");    
		if (client.connect("eagle", mqtt_username, mqtt_password)) {      
			Serial.println("Connected");      
		} else {      
			Serial.print("Error, rc=");      
			Serial.print(client.state());      
			Serial.println(" Trying again in 5 seconds");         
		}  
	}
}


void loop() {
  // Read distance from the ultrasonic sensor
  long newDistance = getDistance();

  // Check if the distance has changed
    // Publish the new distance to the MQTT topic

  sprintf(payload, "%ld", newDistance);
  if (!client.connected()) {
    reconnect();
  }
  // Create a JSON object
  String jsonString = "{";


  // Add data to the JSON object
  jsonString += "\'idSensor': '4',";
  jsonString += "\'idDevice': '1',";
  if (distance < 10 && state == 0){
    jsonString += "\'state': 'active'\}";
    client.publish(mqtt_topic, jsonString.c_str());    
    Serial.println("Published to MQTT: " + jsonString);
    state = 1;
  }
  if (distance > 10 && state == 1){
    jsonString += "\'state': 'idle'\}";
    client.publish(mqtt_topic, jsonString.c_str());      
    Serial.println("Published to MQTT: " + jsonString);
    state = 0;
  }


    // Update the previous distance

  // Add a delay to avoid sending too many messages
  delay(5000);
}

long getDistance() {
  // Clears the trigPin
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);
  // Calculating the distance
  distance = duration * 0.034 / 2;
  // Prints the distance on the Serial Monitor
  Serial.print("Distance: ");
  Serial.println(distance);
  return distance;
}