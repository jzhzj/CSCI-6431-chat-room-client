package com.gwu.cs6431.client.service.io.courier;

import com.gwu.cs6431.client.service.exception.MessageNotCompletedException;
import com.gwu.cs6431.client.service.message.Message;

import java.io.*;
import java.net.Socket;

/**
 * Implementation class of Courier used to send messages to the server.
 *
 * @author qijiuzhi
 */
public class CourierImpl implements Courier {
    private Socket socket;

    public CourierImpl(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void send(Message msg) throws IOException, MessageNotCompletedException {
        if (!msg.readyToSend()) {
            throw new MessageNotCompletedException(msg.getStartLine().toString() + " message is not completed.");
        }
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.print(msg.toString());
        out.flush();
    }
}
