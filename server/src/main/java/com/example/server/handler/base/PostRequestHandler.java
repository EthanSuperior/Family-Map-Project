package com.example.server.handler.base;

import com.sun.net.httpserver.HttpExchange;
import com.example.shared.MyJsonSerializer;

import java.io.*;
import java.net.HttpURLConnection;

public interface PostRequestHandler extends RequestHandler {
    @Override
    default void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handling Request for: " + exchange.getRequestURI());
        //Check AuthToken
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                handleService(exchange);
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            //e.printStackTrace();
        }
        exchange.close();
    }

    void handleService(HttpExchange exchange) throws IOException;

    default <T> T readString(HttpExchange exchange, Class<T> requestClass) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        // Read JSON string from the input stream
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(reqBody);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        String reqData = sb.toString();
        // Display/log the request JSON data
        System.out.println(reqData);
        return MyJsonSerializer.deserialize(reqData, requestClass);
    }
}
