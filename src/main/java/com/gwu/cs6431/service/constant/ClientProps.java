package com.gwu.cs6431.service.constant;

import java.util.ResourceBundle;

public class ClientProps {
    static {
        ResourceBundle rb = ResourceBundle.getBundle("client");
        SERVER_ADDRESS = rb.getString("SERVER_ADDRESS");
        SERVER_PORT = Integer.parseInt(rb.getString("SERVER_PORT"));
        CLIENT_PORT = Integer.parseInt(rb.getString("CLIENT_PORT"));
    }

    public static final String SERVER_ADDRESS;
    public static final int SERVER_PORT;
    public static final int CLIENT_PORT;

    public static final String EOM = "\0";
    public static final String NEW_LINE = "\r\n";
    public static final int TIME_OUT = 1000;
}
