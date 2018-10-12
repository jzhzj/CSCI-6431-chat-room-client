package com.gwu.cs6431.service.io.courier;

import com.gwu.cs6431.service.message.Message;

import java.io.*;
import java.net.Socket;

public class CourierImpl implements Courier {
    private static final String EOM = "\0";
    private static final String NEW_LINE = "\r\n";
    private Socket socket;
    private PrintWriter out;

    public CourierImpl(Socket socket) {
        this.socket = socket;
    }

    /**
     * Sends a message to the server. Returns a Message object from the server.
     * If failed to get a message, returns null.
     *
     * TODO add timer
     * */
    @Override
    public Message execute(Message msg) {
        System.out.println(msg);
        StringBuilder sb;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.print(msg.toString());
            sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            int i = 0;
            while (!(line = in.readLine()).equals(EOM) && i < 50) {
                sb.append(line);
                sb.append(NEW_LINE);
                i++;
            }
            sb.append(EOM);
        } catch (IOException e) {
            return null;
        }
        return Message.genMessage(sb.toString());
    }

    @Override
    public void send(Message msg) {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.print(msg.toString());
        } catch (IOException e) {

        }
    }
}
