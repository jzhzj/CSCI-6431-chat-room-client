package com.gwu.cs6431.service.io.courier;

import com.gwu.cs6431.service.constant.ClientProps;
import com.gwu.cs6431.service.message.Message;

import java.io.*;
import java.net.Socket;

public class CourierImpl implements Courier {
    private static final String EOM = ClientProps.EOM;
    private static final String NEW_LINE = ClientProps.NEW_LINE;
    private Socket socket;
    private PrintWriter out;

    public CourierImpl(Socket socket) {
        this.socket = socket;
    }

    /**
     * Sends a message to the server. Returns a Message object from the server.
     * If failed to get a message, returns null.
     * <p>
     * TODO add timer
     */
    @Override
    public Message execute(Message msg) {
        String rawStr;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.print(msg.toString());
            rawStr = listen();
        } catch (IOException e) {
            return null;
        }
        return Message.genMessage(rawStr);
    }

    @Override
    public void send(Message msg) throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        out.print(msg.toString());
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public String listen() throws IOException {
        StringBuilder sb;
        sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        int lineNum = 0;
        while (!(line = in.readLine()).equals(EOM) && lineNum < ClientProps.MAX_MSG_LEN) {
            sb.append(line);
            sb.append(NEW_LINE);
            lineNum++;
        }
        sb.append(EOM);
        return sb.toString();
    }
}
