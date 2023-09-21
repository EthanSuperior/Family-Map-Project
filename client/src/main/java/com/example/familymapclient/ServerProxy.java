package com.example.familymapclient;

import com.example.shared.MyJsonSerializer;
import com.example.shared.model.AuthToken;
import com.example.shared.request.LoginRequest;
import com.example.shared.request.RegisterRequest;
import com.example.shared.result.EventsResult;
import com.example.shared.result.LoginResult;
import com.example.shared.result.PersonsResult;
import com.example.shared.result.RegisterResult;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerProxy {

    private static String baseURL;

    public static void setURL(String host, String port) {
        baseURL = "http://" + host + ":" + port;
    }

    public static RegisterResult register(RegisterRequest registerRequest) {
        try {
            URL loginURL = new URL(baseURL.concat("/user/register"));
            System.out.println(loginURL.toURI());
            HttpURLConnection connection = (HttpURLConnection) loginURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter sw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(sw);
            String resultString = MyJsonSerializer.serialize(registerRequest);
            bw.write(resultString);
            bw.flush();
            bw.close();

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                StringBuilder sb = new StringBuilder();
                InputStreamReader sr = new InputStreamReader(responseBody);
                char[] buf = new char[1024];
                int len;
                while ((len = sr.read(buf)) > 0) {
                    sb.append(buf, 0, len);
                }
                String reqData = sb.toString();
                // Display/log the response JSON data
                System.out.println(reqData);
                return (RegisterResult) MyJsonSerializer.deserialize(reqData, RegisterResult.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RegisterResult(e.getMessage());
        }
        return new RegisterResult("Something went wrong");
    }

    public static PersonsResult getPeople(AuthToken token) {
        try {
            URL loginURL = new URL(baseURL.concat("/person"));
            System.out.println(loginURL.toURI());
            HttpURLConnection connection = (HttpURLConnection) loginURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", token.getAuthorizationToken());

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                StringBuilder sb = new StringBuilder();
                InputStreamReader sr = new InputStreamReader(responseBody);
                char[] buf = new char[1024];
                int len;
                while ((len = sr.read(buf)) > 0) {
                    sb.append(buf, 0, len);
                }
                String reqData = sb.toString();
                System.out.println(reqData);
                return MyJsonSerializer.deserialize(reqData, PersonsResult.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PersonsResult(e.getMessage());
        }
        return new PersonsResult("Something went wrong");
    }

    public static EventsResult getEvents(AuthToken token) {
        try {
            URL loginURL = new URL(baseURL.concat("/event"));
            System.out.println(loginURL.toURI());
            HttpURLConnection connection = (HttpURLConnection) loginURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", token.getAuthorizationToken());

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                StringBuilder sb = new StringBuilder();
                InputStreamReader sr = new InputStreamReader(responseBody);
                char[] buf = new char[1024];
                int len;
                while ((len = sr.read(buf)) > 0) {
                    sb.append(buf, 0, len);
                }
                String reqData = sb.toString();
                System.out.println(reqData);
                return MyJsonSerializer.deserialize(reqData, EventsResult.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new EventsResult(e.getMessage());
        }
        return new EventsResult("Something went wrong");
    }

    //do everything with httpURLConnection
    public LoginResult login(LoginRequest loginRequest) {
        try {
            URL loginURL = new URL(baseURL.concat("/user/login"));
            System.out.println(loginURL.toURI());
            HttpURLConnection connection = (HttpURLConnection) loginURL.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.connect();

            OutputStream os = connection.getOutputStream();
            OutputStreamWriter sw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(sw);
            String resultString = MyJsonSerializer.serialize(loginRequest);
            bw.write(resultString);
            bw.flush();
            bw.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                StringBuilder sb = new StringBuilder();
                InputStreamReader sr = new InputStreamReader(responseBody);
                char[] buf = new char[1024];
                int len;
                while ((len = sr.read(buf)) > 0) {
                    sb.append(buf, 0, len);
                }
                String reqData = sb.toString();
                // Display/log the response JSON data
                System.out.println(reqData);
                return (LoginResult) MyJsonSerializer.deserialize(reqData, LoginResult.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResult(e.getMessage());
        }
        return new LoginResult("Something went wrong");
    }

}
