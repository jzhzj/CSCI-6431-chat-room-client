package com.gwu.cs6431.service.constant;

import java.util.ResourceBundle;

public class ClientProps {
    static {
        ResourceBundle rb = ResourceBundle.getBundle("client");
        SERVER_ADDRESS = rb.getString("SERVER_ADDRESS");
        SERVER_PORT = Integer.parseInt(rb.getString("SERVER_PORT"));
        CLIENT_PORT = Integer.parseInt(rb.getString("CLIENT_PORT"));
        TIME_OUT = Integer.parseInt(rb.getString("TIME_OUT"));
        MAX_MSG_LEN = Integer.parseInt(rb.getString("MAX_MSG_LEN"));
    }

    public static final String SERVER_ADDRESS;
    public static final int SERVER_PORT;
    public static final int CLIENT_PORT;
    public static final int TIME_OUT;
    public static final int MAX_MSG_LEN;

    public static final String EOM = "\0";
    public static final String NEW_LINE = "\r\n";
}
