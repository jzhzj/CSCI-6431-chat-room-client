package com.gwu.cs6431.service.io.listener;

import java.net.Socket;

public class Task implements Runnable {
    Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
