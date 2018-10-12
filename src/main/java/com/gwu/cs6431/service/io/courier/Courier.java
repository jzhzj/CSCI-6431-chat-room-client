package com.gwu.cs6431.service.io.courier;

import com.gwu.cs6431.service.message.Message;

/**
 * Defines what a Courier should do
 */
public interface Courier {
    Message execute(Message msg);

    void send(Message msg);
}
