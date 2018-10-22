package com.gwu.cs6431.service.io.listener;

import com.gwu.cs6431.gui.MainController;
import com.gwu.cs6431.service.constant.ClientProps;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
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
    private MainController mainController;
    private Socket socket;

    @Override
    public void run() {
        listen();
    }

    private void listen() {
        if (STAT == WORKING)
            return;
        STAT = WORKING;

        try {
            socket = new Socket();
            socket.bind(new InetSocketAddress(ClientProps.CLIENT_PORT));
            socket.connect(new InetSocketAddress(ClientProps.SERVER_ADDRESS, ClientProps.SERVER_PORT), ClientProps.TIME_OUT);
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
                es.execute(new Task(mainController, sb.toString()));
                sb = new StringBuilder();
            }
        } catch (IOException e) {
            if (Thread.currentThread().isInterrupted())
                System.out.println("Listener is interrupted from Blocked I/O");
            // TODO show alert
            Platform.exit();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void close() throws IOException{
        socket.close();
    }
}
