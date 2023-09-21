package com.example.server.handler;

import com.example.server.handler.base.RequestHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileRequestHandler implements RequestHandler {

    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        String urlPath = exchange.getRequestURI().toString();

        if (urlPath == null || urlPath.equals("/")) {
            urlPath = "/index.html";
        }
        String filePath = "web" + urlPath;
        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

            OutputStream respBody = exchange.getResponseBody();
            file = new File("web/HTML/404.html");
            Files.copy(file.toPath(), respBody);
            respBody.close();
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            OutputStream respBody = exchange.getResponseBody();
            Files.copy(file.toPath(), respBody);
            respBody.close();
        }
    }
}
