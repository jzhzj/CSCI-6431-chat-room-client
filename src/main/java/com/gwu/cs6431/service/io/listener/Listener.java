package com.gwu.cs6431.service.io.listener;

import com.gwu.cs6431.service.constant.ClientProps;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listener implements Runnable {
    private static Listener ourInstance = new Listener();

    public static Listener getInstance() {
        return ourInstance;
    }

    private Listener() {
    }

    private static final String EOM = ClientProps.EOM;
    private static final String NEW_LINE = ClientProps.NEW_LINE;
    private int IDLE = 0;
    private int WORKING = 1;
    private int STAT = IDLE;

    @Override
    public void run() {
        listen();
    }

    private void listen() {
        if (STAT == WORKING)
            return;
        STAT = WORKING;
        Socket s;
        try {
            s = new Socket(ClientProps.SERVER_ADDRESS, ClientProps.SERVER_PORT
                    , InetAddress.getLocalHost(), ClientProps.CLIENT_PORT);
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ExecutorService es = Executors.newCachedThreadPool();
            for (; ; ) {
                String line;
                while (!(line = in.readLine()).equals(EOM)) {
                    sb.append(line);
                    sb.append(NEW_LINE);
                }
                sb.append(EOM);
                es.execute(new Task(sb.toString()));
                sb = new StringBuilder();
            }
        } catch (IOException e) {
            // TODO show alert
            Platform.exit();
        }
    }
}
