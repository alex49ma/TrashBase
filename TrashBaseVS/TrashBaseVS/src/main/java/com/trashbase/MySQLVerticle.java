package com.trashbase;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

public class MySQLVerticle extends AbstractVerticle {

    public static MySQLPool mySqlClient;
    Future<?> response;
    Router router = Router.router(vertx);
	public static EventBus eventBus;

    public void start(Promise<Void> start) {

        dbSetup();

        serverSetup(start);

		eventBus = getVertx().eventBus();

        ////////////////////////////////////////////////////////////////
        //      Device Routing
        ////////////////////////////////////////////////////////////////

        router.route("/api/v1/devices").handler(BodyHandler.create());
        router.get("/api/v1/devices/:idDev").handler(methods::getBin);              // Method to get a specific device
        router.get("/api/v1/devices").handler(methods::getBins);                    // Method to get all devices
        router.put("/api/v1/devices/empty/:idDev").handler(ctx -> {
            methods.emptyBin(ctx);
            methods.newPeriod(ctx);
            vertx.eventBus().publish("device_instruction", "open");
        });      //  Method to empty one device, set all sensors and open all lockers from that device
        router.put("/api/v1/devices/:idDev").handler(methods::updateBin);           // Method to update a device
        router.post("/api/v1/devices").handler(methods::addBin);                    // Method to post a device
        
        ////////////////////////////////////////////////////////////////
        //      Sensor and Actuator Routing
        ////////////////////////////////////////////////////////////////

        router.route("/api/v1/sensors*").handler(BodyHandler.create());
        router.get("/api/v1/sensors/:idSens").handler(methods::getSens);            // Method to get a specific sensor
        router.get("/api/v1/sensors").handler(methods::getAllSens);                 // Method to get all sensors
        router.post("/api/v1/sensors").handler(methods::addSens);                   // Method to post a sensor
        router.put("/api/v1/sensors/:idSens").handler(methods::updateSens);         // Method to update a sensor
        router.delete("/api/v1/sensors/:idSens").handler(methods::deleteSens);      // Method to delete a sensor
    }

    private void serverSetup(Promise<Void> start) {

        ////////////////////////////////////////////////////////////////
        //      SERVER INITIALIZATION
        ////////////////////////////////////////////////////////////////

        vertx.createHttpServer().requestHandler(router::handle).listen(8081, result -> {
			if (result.succeeded()) {
				start.complete();
			} else {
				start.fail(result.cause());
			}
		});

    }

    private void dbSetup() {
        ////////////////////////////////////////////////////////////////
        //      DB SETUP
        ////////////////////////////////////////////////////////////////
        //Router router = Router.router(vertx);

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
            .setPort(3306)
            .setHost("127.0.0.1")
            .setDatabase("trashbase")
            .setUser("DBuserName")
            .setPassword("yourPass");

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
 
        mySqlClient = MySQLPool.pool(vertx, connectOptions, poolOptions);


    }
}