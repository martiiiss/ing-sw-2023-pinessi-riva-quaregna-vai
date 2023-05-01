package distributed;

public interface ServerRMIInterface {
    void startServer();
    void login(String username, ClientConnectionRMI clientConnection);
    void disconnect();

}
