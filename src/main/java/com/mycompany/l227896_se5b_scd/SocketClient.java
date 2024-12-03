package com.mycompany.l227896_se5b_scd;

import utility.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {
    private String hostname;
    private int port;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public SocketClient() {
        this.hostname = Constants.hostname;
        this.port =Constants.port;
    }

    public void connect() throws IOException {
        socket = new Socket(hostname, port);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
        System.out.println("Connected to the server.");
    }

    public void sendData(String data) throws IOException {
        outputStream.writeUTF(data);
        System.out.println("Data sent to the server: " + data);
    }

    public String waitForResponse() throws IOException {
        String response = inputStream.readUTF();
        System.out.println("Response from server: " + response);
        return response;
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}