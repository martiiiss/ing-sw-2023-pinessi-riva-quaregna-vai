package util;

import distributed.messages.Message;

public interface Observer {

    void update(Observable observable, Message message);
}
