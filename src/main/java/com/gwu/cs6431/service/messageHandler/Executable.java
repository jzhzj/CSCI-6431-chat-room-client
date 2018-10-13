package com.gwu.cs6431.service.messageHandler;

/**
 * Defines what an Executable handler should do
 */
public interface Executable {
    boolean execute();

    String getServerFeedback();
}
