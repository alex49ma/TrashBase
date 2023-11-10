package com.trashbase;

//import java.util.ArrayList;
//import java.util.List;

import com.trashbase.entities.Device;
import com.trashbase.entities.Sensor;
import java.time.LocalDate;

//import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
//import io.vertx.mqtt.MqttEndpoint;
//import io.vertx.mqtt.MqttTopicSubscription;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class methods {

    public static void getBins(RoutingContext ctx){
        MySQLVerticle.mySqlClient.query("Select * from devices;").execute().onComplete(async -> {
			if (async.succeeded()) {
				RowSet<Row> resultSet = async.result();
				JsonArray result = new JsonArray();
				for (Row elem : resultSet) {
					result.add(JsonObject.mapFrom(new Device(elem.getInteger("idDevice"), elem.getString("name"))));
				}
				//System.out.println(result.toString());
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end(result.encodePrettily());
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
    }

    public static void getBin(RoutingContext ctx){
		String param = ctx.pathParam("idDev");
		String query = "SELECT d.idDevice AS device_id,	d.name AS device_name, s.idSensor AS sensor_actuator_id, 'Sensor' AS type, s.state AS state FROM devices AS d LEFT JOIN sensors AS s ON d.idDevice = s.idDevice WHERE d.idDevice = " + param + " UNION SELECT d.idDevice AS device_id, d.name AS device_name, a.idActuator AS sensor_actuator_id, 'Actuator' AS type, a.state AS state FROM devices AS d LEFT JOIN actuators AS a ON d.idDevice = a.idDevice WHERE d.idDevice = " + param + ";";
        MySQLVerticle.mySqlClient.query(query).execute().onComplete(async -> {
			if (async.succeeded()) {
				RowSet<Row> resultSet = async.result();
				JsonArray result = new JsonArray();
				for (Row elem : resultSet) {
					result.add(JsonObject.of("device_id", elem.getValue("device_id"), 
					"device_name", elem.getValue("device_name"), 
					"sensor_actuator_id", elem.getValue("sensor_actuator_id"), 
					"type", elem.getValue("type"), 
					"state", elem.getValue("state")));
				}
				//System.out.println(result.toString());
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end(result.encodePrettily());
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
    }

    public static void updateBin(RoutingContext ctx){
		String idDev = ctx.pathParam("idDev");
		JsonObject dev = ctx.body().asJsonObject();
		Device device = new Device(dev.getInteger("idDevice"), dev.getString("name"));

        MySQLVerticle.mySqlClient.query("UPDATE device set name = '" + device.getName() + "' WHERE idDevice = " + idDev + ";").execute().onComplete(async -> {
			if (async.succeeded()) {
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end("Device " + idDev + " updated succesfully");
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
    }

	public static void emptyBin(RoutingContext ctx){
		String idBin = ctx.pathParam("idDev");
        MySQLVerticle.mySqlClient.query("UPDATE sensors SET state = 'idle' WHERE idDevice = " + idBin + "; UPDATE actuators SET state = 'open' WHERE idDevice = " + idBin + ";").execute().onComplete(async -> {
			if (async.succeeded()) {
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end("Device " + idBin + " updated succesfully");
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
		});

	}
        
    public static void addBin(RoutingContext ctx){
		JsonObject dev = ctx.body().asJsonObject();
		Device device = new Device(dev.getInteger("idDevice"), dev.getString("name"));

        MySQLVerticle.mySqlClient.query("INSERT INTO devices (idDevice, name) VALUES (" + device.getIdDevice() + ", '" + device.getName() +"');").execute().onComplete(async -> {
			if (async.succeeded()) {
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end("Sensor " + device.getIdDevice() + " added succesfully");
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
    }

    public static void getAllSens(RoutingContext ctx){
        MySQLVerticle.mySqlClient.query("Select * from sensors;").execute().onComplete(async -> {
			if (async.succeeded()) {
				RowSet<Row> resultSet = async.result();
				JsonArray result = new JsonArray();
				for (Row elem : resultSet) {
					result.add(JsonObject.mapFrom(new Sensor(elem.getInteger("idSensor"), elem.getInteger("idDevice"), elem.getString("state"))));
				}
				//System.out.println(result.toString());
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end(result.encodePrettily());
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
    }
    
    public static void getSens(RoutingContext ctx){
		String idSens = ctx.pathParam("idSens");
        MySQLVerticle.mySqlClient.query("Select * from sensors Where idSensor = " + idSens + ";").execute().onComplete(async -> {
			if (async.succeeded()) {
				RowSet<Row> resultSet = async.result();
				JsonArray result = new JsonArray();
				for (Row elem : resultSet) {
					result.add(JsonObject.mapFrom(new Sensor(elem.getInteger("idSensor"), elem.getInteger("idDevice"), elem.getString("state"))));
				}
				//System.out.println(result.toString());
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end(result.encodePrettily());
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
    }
    
    public static void updateSens(RoutingContext ctx){
		String idSens = ctx.pathParam("idSens");
		JsonObject sens = ctx.body().asJsonObject();
		Sensor sensor = new Sensor(sens.getInteger("idSensor"), sens.getInteger("idDevice"), sens.getString("state"));

        MySQLVerticle.mySqlClient.query("UPDATE sensors set idDevice = " + sensor.getIdDevice() + ", state = '" + sensor.getState() + "' WHERE idSensor = " + idSens + ";").execute().onComplete(async -> {
			if (async.succeeded()) {
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end("Sensor " + idSens + " updated succesfully");
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
    }

	public static void updateSens(Sensor sensor){
		String idSens = Integer.toString(sensor.getIdSensor());

        MySQLVerticle.mySqlClient.query("UPDATE sensors set idDevice = " + sensor.getIdDevice() + ", state = '" + sensor.getState() + "' WHERE idSensor = " + idSens + ";").execute().onComplete(async -> {
			if (async.succeeded()) {
				System.out.println("Sensor " + idSens + " updated succesfully");
				String actState = (sensor.getState().equals("idle")) ? "open" :  "locked";
				MySQLVerticle.mySqlClient.query("UPDATE actuators set idDevice = " + sensor.getIdDevice() + ", state = '" + actState + "' WHERE idActuator = " + idSens + ";").execute().onComplete(async2 -> {
					if (async.succeeded()) {
						System.out.println("Actuator " + idSens + " updated succesfully");
					} else {
						System.out.println("Error: " + async2.cause().getLocalizedMessage());
					}
				});
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
    }

	
    
    public static void deleteSens(RoutingContext ctx){
		String idSens = ctx.pathParam("idSens");
        MySQLVerticle.mySqlClient.query("DELETE FROM sensors WHERE idSensor = " + idSens + ";").execute().onComplete(async -> {
			if (async.succeeded()) {
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end("Sensor " + idSens + " deleted succesfully");
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });        
    }
    
	public static void addSens(RoutingContext ctx){
		JsonObject sens = ctx.body().asJsonObject();
		Sensor sensor = new Sensor(sens.getInteger("idSensor"), sens.getInteger("idDevice"), sens.getString("state"));

        MySQLVerticle.mySqlClient.query("INSERT INTO sensors (idSensor, idDevice, state) VALUES (" + sensor.getIdSensor() + ", " + sensor.getIdDevice() + ", '" + sensor.getState() +"');").execute().onComplete(async -> {
			if (async.succeeded()) {
				ctx.response().setStatusCode(200)
						.putHeader("content-type", "application/json; charset=utf-8").end("Sensor " + sensor.getIdSensor() + " added succesfully");
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
        });
	}

	public static void newPeriod(Sensor sensor){			// We need a method that does the period from an object Sensor
		MySQLVerticle.mySqlClient.query("Select endDate, state from periods WHERE idDevice = " + sensor.getIdDevice() + " ORDER BY endDate DESC LIMIT 1;").execute().onComplete(async -> {
			LocalDate timeNow = LocalDate.now();
			String date = timeNow.toString();
			String strTimeNow = timeNow.toString();
			if (async.succeeded()) {
				RowSet<Row> resultSet = async.result();
				for (Row elem : resultSet) {
					date = elem.getValue("endDate").toString();
					String alterState = ((sensor.getState().equals("idle") ? "closed" : "open"));		// The previous state was the oposite of the new one detected by the sensor
					MySQLVerticle.mySqlClient.query("INSERT INTO periods (initDate, endDate, idDevice, state) VALUES (DATE('" + date + "'), DATE('"+ strTimeNow + "'), " + sensor.getIdDevice() + ", '" + alterState +"');").execute().onComplete(async2 -> {
						if (async.succeeded()) {
							System.out.println("Period inserted successfuly");
						} else {
							System.out.println("Error: " + async.cause().getLocalizedMessage());
						}
					}); /**/
				}
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
		});
	}

	public static void newPeriod(RoutingContext ctx){			// We need a method that does the period from an HTTP method
		String idDev = ctx.pathParam("idDev");
		MySQLVerticle.mySqlClient.query("Select endDate, state from periods WHERE idDevice = " + idDev + " ORDER BY endDate DESC LIMIT 1;").execute().onComplete(async -> {
			LocalDate timeNow = LocalDate.now();
			String date = timeNow.toString();
			String strTimeNow = timeNow.toString();
			String state = "";
			if (async.succeeded()) {
				RowSet<Row> resultSet = async.result();
				for (Row elem : resultSet) {
					date = elem.getValue("endDate").toString();
					state = elem.getValue("state").toString();
					String alterState = ((state.equals("open") ? "closed" : "open"));		// The previous state is the oposite of the one before
					MySQLVerticle.mySqlClient.query("INSERT INTO periods (initDate, endDate, idDevice, state) VALUES (DATE('" + date + "'), DATE('"+ strTimeNow + "'), " + idDev + ", '" + alterState +"');").execute().onComplete(async2 -> {
						if (async.succeeded()) {
							System.out.println("Period inserted successfuly");
						} else {
							System.out.println("Error: " + async.cause().getLocalizedMessage());
						}
					}); /**/
				}
			} else {
				System.out.println("Error: " + async.cause().getLocalizedMessage());
			}
		});
	}


	/* 
	public static void handleSubscription(MqttEndpoint endpoint) {
		endpoint.subscribeHandler(subscribe -> {
			List<MqttQoS> grantedQosLevels = new ArrayList<>();
			for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
				System.out.println("Suscripción al topic " + s.topicName());
				grantedQosLevels.add(s.qualityOfService());
			}
			endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);
		});
	}*/
}


