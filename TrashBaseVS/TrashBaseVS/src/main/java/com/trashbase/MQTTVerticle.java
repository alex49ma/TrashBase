package com.trashbase;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.trashbase.entities.Sensor;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class MQTTVerticle extends AbstractVerticle {

    Gson gson;

    public void start(Promise<Void> start) {

        gson = new Gson();
        MqttClient mqttClient = MqttClient.create(vertx, new MqttClientOptions().setAutoKeepAlive(true));
        mqttClient.connect(1883, "localhost",  S -> {
            mqttClient.subscribe("sensor_values", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
                System.out.println("Suscription " + mqttClient.clientId());
            });
            mqttClient.publishHandler(handler -> {
                System.out.println("Message received:");
                System.out.println("    Topic: " + handler.topicName().toString());
                System.out.println("    Message ID: "+ handler.messageId());
                System.out.println("    Content: " + handler.payload().toString());
                try{
                    Sensor sc = gson.fromJson(handler.payload().toString(), Sensor.class);
                    methods.updateSens(sc);
                    methods.newPeriod(sc);
                    if(sc.getState().equals("active")){
                        mqttSender("close");
                    } else {
                        mqttSender("open");
                    }
                    System.out.println("    Sensor: \n" + sc.toString());
                }catch(JsonSyntaxException e){
                    System.out.println("    It is not a sensor");
                }

            });
        });

        getVertx().eventBus().consumer("device_instruction", message -> {
            String custom = (String) message.body();
            mqttSender(custom);
        });

    }

    private void mqttSender(String body){
        MqttClientOptions options = new MqttClientOptions()         // Substitute with your settings
                .setClientId("serverPublisher")
                .setUsername("clientUser")
                .setPassword("password");

        MqttClient client = MqttClient.create(vertx, options);

        client.connect(1883, "localhost", connection -> {
            if (connection.succeeded()) {
                System.out.println("Connected to the MQTT broker");

                String topic = "actuator_order";

                client.publish(topic, Buffer.buffer(body), MqttQoS.AT_MOST_ONCE, false, false, publish -> {
                    if (publish.succeeded()) {
                        System.out.println("Message published successfully: " + body);
                    } else {
                        System.err.println("Failed to publish message: " + publish.cause().getMessage());
                    }

                    client.disconnect();
                });
            } else {
                System.err.println("Failed to connect to the MQTT broker: " + connection.cause().getMessage());
            }
        });
    }

}
