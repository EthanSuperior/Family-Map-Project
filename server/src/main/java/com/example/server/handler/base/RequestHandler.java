package com.example.server.handler.base;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.example.shared.MyJsonSerializer;

import java.io.*;
import java.net.HttpURLConnection;

public interface RequestHandler extends HttpHandler {
    @Override
    default void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handling Request for: " + exchange.getRequestURI());

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                handleService(exchange);
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            e.printStackTrace();
        }
        exchange.close();
    }

    void handleService(HttpExchange exchange) throws IOException;

    default <T> void writeString(HttpExchange exchange, T resultObject) throws IOException {
        OutputStream os = exchange.getResponseBody();
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        String resultString = MyJsonSerializer.serialize(resultObject);
        bw.write(resultString);
        bw.flush();
    }
}
