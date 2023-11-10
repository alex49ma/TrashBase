package com.trashbase;

import io.vertx.core.AbstractVerticle;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.core.Promise;

public class Launcher extends AbstractVerticle {

    public static MySQLPool mySqlClient;

    public void start(Promise<Void> start) {

        getVertx().deployVerticle(MySQLVerticle.class.getName(), completion -> {
            System.out.println("Verticle DB deployed");
        });

        getVertx().deployVerticle(Receiver.class.getName(), completion -> {
            System.out.println("Verticle Receiver deployed");
        });

        getVertx().deployVerticle(MQTTVerticle.class.getName(), completion -> {
            System.out.println("MQTT Verticle deployed");
        });

    }


}