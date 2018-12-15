package com.gwu.cs6431.client;

import com.gwu.cs6431.client.service.io.listener.Listener;

public class Main {
    public static void main(String[] args) {

        listenerThread = new Thread(Listener.getInstance());
        listenerThread.setDaemon(true);
        listenerThread.start();
        App.launch(App.class, args);
    }
}
