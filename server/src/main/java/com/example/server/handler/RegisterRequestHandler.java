package com.example.server.handler;

import com.example.server.handler.base.PostRequestHandler;
import com.example.server.service.RegisterService;
import com.example.shared.model.Location;
import com.example.shared.request.RegisterRequest;
import com.example.shared.result.RegisterResult;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;

import static com.example.server.handler.FillRequestHandler.parse;
import static com.example.server.handler.FillRequestHandler.parseLocations;

public class RegisterRequestHandler implements PostRequestHandler {
    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        System.out.println("Entering Register Handler");
        RegisterRequest input = readString(exchange, RegisterRequest.class);
        RegisterService service = new RegisterService();

        String[] mnames = parse(new File("json/mnames.json"));
        String[] fnames = parse(new File("json/fnames.json"));
        String[] snames = parse(new File("json/snames.json"));
        Location[] locations = parseLocations(new File("json/locations.json"));
        service.setGenerators(mnames, fnames, snames, locations);

        RegisterResult result = service.register(input);
        if (result.success) {
            System.out.println("Finished Registering");
            Headers header = exchange.getRequestHeaders();
            header.put("Authorization", Collections.singletonList(result.getAuthToken()));
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        writeString(exchange, result);
        exchange.close();
    }
}
