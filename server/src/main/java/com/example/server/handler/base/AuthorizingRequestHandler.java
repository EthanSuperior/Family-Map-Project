package com.example.server.handler.base;

import com.example.server.service.authentication.CheckAuthToken;
import com.example.shared.result.ServiceResult;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface AuthorizingRequestHandler extends RequestHandler {
    @Override
    default void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handling Request for: " + exchange.getRequestURI());
        String token = exchange.getRequestHeaders().get("Authorization").get(0);
        ServiceResult authTokenCheckResult = new CheckAuthToken().checkToken(token);
        if (!authTokenCheckResult.success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            writeString(exchange, authTokenCheckResult);
        } else {
            try {
                if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                    handleService(exchange);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            } catch (IOException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                //e.printStackTrace();
            }
        }
        exchange.close();
    }
}
