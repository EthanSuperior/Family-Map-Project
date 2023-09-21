package com.example.server.handler;

import com.example.server.handler.base.PostRequestHandler;
import com.example.server.service.LoginService;
import com.example.shared.request.LoginRequest;
import com.example.shared.result.LoginResult;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;

public class LoginRequestHandler implements PostRequestHandler {
    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        LoginRequest input = readString(exchange, LoginRequest.class);
        LoginService service = new LoginService();
        LoginResult result = service.login(input);
        System.out.println(exchange.getRequestHeaders().get("Authorization"));
        if (result.success) {
            Headers header = exchange.getRequestHeaders();
            header.put("Authorization", Collections.singletonList(result.getAuthToken()));
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        System.out.println(exchange.getRequestHeaders().get("Authorization"));
        writeString(exchange, result);
        exchange.close();
    }
}
