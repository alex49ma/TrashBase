# TrashBase

## Description

This project aims to automatize the work of the cleaning services and improve higene in streets.

## Getting Started

### Prerequisites

For this project we just need to have installed MQTT Mosquitto, MySQL and Maven.

### Installation

We need to change the values to the connections to the MQTT Broker in the servo and the sensors, and upload to every sensor and every servo
a different version of the c++ code, giving to every device a unique identifier. It is also important to update the wifi connection settings and the MySQL connection settings depending on our configuration.
Finally we can use the comand line on our project to use maven, install and run our project.

## Usage

### HTTP Methods for Devices:

#### Get All Devices
Method: GET
Path: "/api/v1/devices"
Description: This method allows retrieving a list of all devices registered in the system.
Example Request:

GET /api/v1/devices

#### Get a Specific Device
Method: GET
Path: "/api/v1/devices/:idDev"
Description: This method enables retrieving detailed information about a specific device identified by its ID.
Example Request:

GET /api/v1/devices/123

#### Empty a Device
Method: PUT
Path: "/api/v1/devices/empty/:idDev"
Description: This method allows emptying a specific device, involving the restart of all sensors and unlocking its associated locks on that device.
Example Request:

PUT /api/v1/devices/empty/456

#### Register a New Device
Method: POST
Path: "/api/v1/devices"
Description: This method enables registering a new device in the system.
Example Request:

POST /api/v1/devices
{
    "idDevice": 330,
    "name": "koala"
}

#### Update an Existing Device
Method: POST
Path: "/api/v1/devices/:idDev"
Description: This method allows updating the name of an existing device identified by its ID.
Example Request:

POST /api/v1/devices/2
{
    "name": "fox"
}

### HTTP Methods for Sensors:

#### Get All Sensors
Method: GET
Path: "/api/v1/sensors"
Description: This method allows obtaining a list of all sensors registered in the system.
Example Request:

GET /api/v1/sensors

#### Get a Specific Sensor
Method: GET
Path: "/api/v1/sensors/:idSen"
Description: This method enables retrieving detailed information about a specific sensor identified by its ID.
Example Request:

GET /api/v1/sensors/456

#### Register a New Sensor
Method: PUT
Path: "/api/v1/sensors"
Description: This method allows registering a new sensor in the system and associating it with a specific device.
Example Request:

PUT /api/v1/sensors
{
    "idDevice": 134,
    "idSensor": 2,
    "state": "idle"
}

#### Update an Existing Sensor
Method: POST
Path: "/api/v1/sensors/:idSen"
Description: This method allows updating the information of an existing sensor in the system.
Example Request:

POST /api/v1/sensors/123 
{
    "idDevice": 2,
    "state": "idle"
}

#### Delete a Sensor
Method: DELETE
Path: "/api/v1/sensores/:idSen"
Description: This method allows deleting a specific sensor identified by its ID from the system.
Example Request:

DELETE /api/v1/sensors/123

## Code Structure

TrashBase
├── SQL_Completo
├── TrashBaseVS
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── trashbase
│   │   │   │           ├── controllers
│   │   │   │           ├── models
│   │   │   │           ├── services
│   │   │   │           └── TrashBaseVerticle.java
│   │   │   └── resources
│   │   │       ├── db
│   │   │       │   └── sql
│   │   │       │       ├── create.sql
│   │   │       │       ├── data.sql
│   │   │       │       └── drop.sql
│   │   │       ├── log4j2.xml
│   │   │       └── META-INF
│   │   │           └── MANIFEST.MF
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── trashbase
│   │                   └── TrashBaseVerticleTest.java
│   ├── target
│   ├── .classpath
│   ├── .project
│   ├── pom.xml
│   └── README.md
└── servo_y_sensor
    ├── Prototipo_Sensor.jpg
    ├── Prototipo_Servo.jpg
    ├── servo_y_sensor.ino
    └── servo_y_sensor.png

## Built With

Maven, MQTT, Vertx, JSON, Java, ESP8266, MySQL

## Authors

Alejandro Martín Auden
