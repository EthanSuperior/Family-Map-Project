package com.example.server.handler;

import com.example.server.handler.base.AuthorizingRequestHandler;
import com.example.server.service.EventService;
import com.example.shared.result.EventResult;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

//event/id
public class EventRequestHandler implements AuthorizingRequestHandler {
    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        URI url = exchange.getRequestURI();
        String eventID = url.toString().substring(7);
        String token = exchange.getRequestHeaders().get("Authorization").get(0);
        System.out.println("Find " + eventID);

        EventService service = new EventService();
        EventResult result = service.getEvent(eventID, token);
        if (result.success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        writeString(exchange, result);
    }
}
