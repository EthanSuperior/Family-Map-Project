package com.example.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.lang.reflect.Type;

public class MyJsonSerializer {

    public static <T> String serialize(T returnType) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(returnType);
    }

    public static <T> T deserialize(String value, Class<T> returnObject) {
        return (new Gson()).fromJson(value, returnObject);
    }

    public static <T> T deserialize(BufferedReader value, Type returnObject) {
        return (new Gson()).fromJson(value, returnObject);
    }
}
