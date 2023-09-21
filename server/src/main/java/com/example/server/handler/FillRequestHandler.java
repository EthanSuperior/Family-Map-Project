package com.example.server.handler;

import com.example.server.handler.base.PostRequestHandler;
import com.example.server.service.FillService;
import com.example.shared.MyJsonSerializer;
import com.example.shared.model.Location;
import com.example.shared.result.FillResult;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

public class FillRequestHandler implements PostRequestHandler {
    public static String[] parse(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            StringDataWrapper data = com.example.shared.MyJsonSerializer.deserialize(bufferedReader, StringDataWrapper.class);
            return data.getData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    public static Location[] parseLocations(File file) throws IOException {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            LocationDataWrapper data = MyJsonSerializer.deserialize(bufferedReader, LocationDataWrapper.class);
            return data.getData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    @Override
    public void handleService(HttpExchange exchange) throws IOException {
        URI url = exchange.getRequestURI();
        String[] parts = url.toString().split("/");
        String userName = parts[2];
        int generations = 4;

        if (parts.length >= 4) {
            generations = Integer.parseInt(parts[3]);
        }

        FillService service = new FillService();

        String[] mnames = parse(new File("json/mnames.json"));
        String[] fnames = parse(new File("json/fnames.json"));
        String[] snames = parse(new File("json/snames.json"));
        Location[] locations = parseLocations(new File("json/locations.json"));
        service.setGenerators(mnames, fnames, snames, locations);

        FillResult result = service.fill(userName, generations);
        if (result.success) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        }

        writeString(exchange, result);
        System.out.println("Completed Fill Service");
        exchange.close();
    }

    private static class StringDataWrapper {
        private List<String> data;

        public StringDataWrapper(List<String> data) {
            this.data = data;
        }

        public String[] getData() {
            return data.toArray(new String[data.size()]);
        }
    }

    private static class LocationDataWrapper {
        private List<Location> data;

        public LocationDataWrapper(List<Location> data) {
            this.data = data;
        }

        public Location[] getData() {
            return data.toArray(new Location[data.size()]);
        }
    }
}
