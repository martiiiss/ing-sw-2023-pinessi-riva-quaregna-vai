package util;

import distributed.messages.Message;

import java.io.IOException;

public interface Observer {

    void update(Observable observable, Message message);

    void onUpdate(Message message) throws IOException;

}
