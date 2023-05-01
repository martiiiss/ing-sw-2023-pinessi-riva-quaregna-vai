package distributed;

public abstract class Connection {
    private boolean connected;
    private String token;

    public boolean isConnected(){
        return connected;
    }

    public abstract void disconnected();

    public abstract void ping();

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}
