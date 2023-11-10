package com.trashbase.Rest;

import java.util.Map;

import io.vertx.core.Promise;

public class RestClientUtil {

    public RestClientUtil(){

    }
    
    public <T> void getRequest(Integer port, String host, String resource, Class<T> classType, Promise<T> promise){

    }

    public <T> void getRequestWithParam(Integer port, String host, String resource, Class<T> classType, Promise<T> promise, Map<String, String> params){

    }

    public <B, T> void postRequest(Integer port, String host, String resource, Object body, Class<T> classType, Promise<T> promise){

    }

    public <B, T> void putRequest(Integer port, String host, String resource, Object body, Class<T> classType, Promise<T> promise){

    }

    public <B, T> void deleteRequest(Integer port, String host, String resource, Promise<String> promise){

    }

}
