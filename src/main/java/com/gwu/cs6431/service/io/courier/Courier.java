package com.gwu.cs6431.service.io.courier;

import java.net.Socket;

/**
 * Defines what a Courier should do
 */
public interface Courier {
    String execute(Socket socket, String message);

    void send(Socket socket, String message);
}
