package com.gwu.cs6431.client.service.io.listener;

import com.gwu.cs6431.client.gui.MainController;
import com.gwu.cs6431.client.service.constant.ClientProps;
import com.gwu.cs6431.client.service.io.SocketFactory;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private Socket socket;

    @Override
    public void run() {
        listen();
    }

    private void listen() {
        if (STAT == WORKING) {
            return;
        }
        STAT = WORKING;

        try {
            socket = SocketFactory.getConstSocket();
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ExecutorService es = Executors.newCachedThreadPool();
            for (; ; ) {
                String line;
                int lineNum = 0;
                while (!(line = in.readLine()).equals(EOM) && lineNum < ClientProps.MAX_MSG_LEN) {
                    sb.append(line);
                    sb.append(NEW_LINE);
                    lineNum++;
                }
                sb.append(EOM);
                es.execute(new Task(sb.toString()));
                sb = new StringBuilder();
            }
        } catch (IOException e) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Listener is interrupted from Blocked I/O");
            }
            e.printStackTrace();
            // TODO show alert
            MainController.promptAlert(Alert.AlertType.ERROR,
                    "Connection Error",
                    "Cannot connet to the server.",
                    "Please check your network settings. Or you may want to check the config file.");
            Platform.exit();
        }
    }
}
