package com.example.server.handler;

import com.example.server.handler.base.AuthorizingRequestHandler;
import com.example.server.service.EventsService;
import com.example.shared.result.EventsResult;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public class EventsRequestHandler implements AuthorizingRequestHandler {
    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        URI url = exchange.getRequestURI();
        String token = exchange.getRequestHeaders().get("Authorization").get(0);
        System.out.println("Find All Events");

        EventsService service = new EventsService();
        EventsResult result = service.event(token);
        if (result.success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        writeString(exchange, result);
    }
}
