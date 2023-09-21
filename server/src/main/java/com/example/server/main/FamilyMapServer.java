package com.example.server.main;

import com.example.server.handler.FileRequestHandler;
import com.sun.net.httpserver.HttpServer;
import com.example.server.handler.EventsRequestHandler;
import com.example.server.handler.EventRequestHandler;
import com.example.server.handler.ClearRequestHandler;
import com.example.server.handler.FillRequestHandler;
import com.example.server.handler.LoadRequestHandler;
import com.example.server.handler.PersonRequestHandler;
import com.example.server.handler.PersonsRequestHandler;
import com.example.server.handler.LoginRequestHandler;
import com.example.server.handler.RegisterRequestHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        if (port < 1 || port > 65535) {
            port = 64046;
        }
        startServer(port);
    }

    private static void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private static void registerHandlers(HttpServer server) {
        server.createContext("/", new FileRequestHandler());
        server.createContext("/clear", new ClearRequestHandler());
        server.createContext("/load", new LoadRequestHandler());
        server.createContext("/person", new PersonsRequestHandler());
        server.createContext("/event", new EventsRequestHandler());
        server.createContext("/fill/", new FillRequestHandler());
        server.createContext("/person/", new PersonRequestHandler());
        server.createContext("/event/", new EventRequestHandler());
        server.createContext("/user/register", new RegisterRequestHandler());
        server.createContext("/user/login", new LoginRequestHandler());
    }
}
