package com.example.server.handler;

import com.example.server.handler.base.AuthorizingRequestHandler;
import com.example.server.service.PersonService;
import com.example.shared.result.PersonResult;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

///person/id
public class PersonRequestHandler implements AuthorizingRequestHandler {
    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        URI url = exchange.getRequestURI();
        String personID = url.toString().substring(8);
        String token = exchange.getRequestHeaders().get("Authorization").get(0);
        System.out.println("Find " + personID);

        PersonService service = new PersonService();
        PersonResult result = service.getPerson(personID, token);
        System.out.println(result);
        if (result.success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        writeString(exchange, result);
    }
}
