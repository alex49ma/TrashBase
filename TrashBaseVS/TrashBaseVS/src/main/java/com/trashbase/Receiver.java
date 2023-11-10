package com.trashbase;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.mysqlclient.MySQLPool;

public class Receiver extends AbstractVerticle {

    public static MySQLPool mySqlClient;    

    public void start(Promise<Void> start) {

        getVertx().eventBus().consumer("msg_bus", message -> {
            String custom = (String) message.body();
            System.out.println("Message received: " + custom);
        });
        
        getVertx().eventBus().consumer("sql_completed", message -> {
            String custom = (String) message.body();
            System.out.println("SQL statement completed: " + custom);
        });


    }

}