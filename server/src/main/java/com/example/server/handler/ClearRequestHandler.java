package com.example.server.handler;

import com.example.server.service.ClearService;
import com.example.shared.result.ClearResult;
import com.sun.net.httpserver.HttpExchange;
import com.example.server.handler.base.PostRequestHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ClearRequestHandler implements PostRequestHandler {
    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        ClearService service = new ClearService();
        ClearResult result = service.clear();
        if (result.success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        }
        writeString(exchange, result);
        exchange.close();
    }
}
