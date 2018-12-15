package com.gwu.cs6431.client.service.io.courier;

import com.gwu.cs6431.client.service.exception.MessageNotCompletedException;
import com.gwu.cs6431.client.service.message.Message;

import java.io.IOException;

/**
 * Defines what a Courier should do
 *
 * @author qijiuzhi
 */
public interface Courier {

    /**
     * Send the message to the server.
     *
     * @param msg message being sent
     * @throws IOException                  exception thrown by the underlying socket
     * @throws MessageNotCompletedException if the message is not ready to be sent
     */
    void send(Message msg) throws IOException, MessageNotCompletedException;
}
