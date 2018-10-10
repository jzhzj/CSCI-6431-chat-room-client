package com.gwu.cs6431.service.io.listener;

import com.gwu.cs6431.service.constant.ClientProps;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenerImpl implements Listener {
    private static ListenerImpl ourInstance = new ListenerImpl();

    public static ListenerImpl getInstance() {
        return ourInstance;
    }

    private ListenerImpl() {
    }

    @Override
    public void listen() {

        ServerSocket ss;
        try {
            ss = new ServerSocket(ClientProps.CLIENT_PORT);
            ExecutorService es = Executors.newCachedThreadPool();
            for (; ; ) {
                es.execute(new Task(ss.accept()));
            }
        } catch (IOException e) {
            // TODO
        }
    }
}
