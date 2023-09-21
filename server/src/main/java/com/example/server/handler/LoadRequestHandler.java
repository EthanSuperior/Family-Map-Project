package com.example.server.handler;

import com.example.server.handler.base.PostRequestHandler;
import com.example.server.service.LoadService;
import com.example.shared.request.LoadRequest;
import com.example.shared.result.LoadResult;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoadRequestHandler implements PostRequestHandler {
    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        LoadRequest input = readString(exchange, LoadRequest.class);
        LoadService service = new LoadService();
        LoadResult result = service.load(input);
        if (result.success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        }
        writeString(exchange, result);
        exchange.close();
    }
}
