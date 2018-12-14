package com.gwu.cs6431.client.service.handler;

import com.gwu.cs6431.client.gui.InitController;
import com.gwu.cs6431.client.gui.MainController;
import com.gwu.cs6431.client.service.io.courier.Courier;
import com.gwu.cs6431.client.service.message.Message;

/**
 * Abstract handler.
 * Contains class variables and member variables that all handler must have.
 *
 * @author qijiuzhi
 */
public abstract class AbstractHandler {
    Courier courier;
    Message msg;
    static MainController mainController;
    static InitController initController;

    public static void setMainController(MainController mainController) {
        AbstractHandler.mainController = mainController;
    }

    public static void setInitController(InitController initController) {
        AbstractHandler.initController = initController;
    }
}
