package com.mycompany.l227896_se5b_scd;


public class L227896_SE5B_SCD {
    public static void main(String[] args) {
        int port = 5000;
        SocketServer server = new SocketServer(port);
        server.start();
    }
}
