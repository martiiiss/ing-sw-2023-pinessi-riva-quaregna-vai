package view;

import distributed.messages.Message;
import util.Observable;
import util.Observer;

import java.io.IOException;

public class View implements Observer, ViewInterface {

    /**
     * @param observable
     * @param message
     */
    @Override
    public void update(Observable observable, Message message) {

    }

    /**
     * @param message
     * @throws IOException
     */
    @Override
    public void onUpdateNickname(Message message) throws IOException {

    }
}
