
import distributed.Socket.ClientController;
import distributed.ClientInterface;
import distributed.RMI.RMIClient;
import view.UserView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class AppClient {
    public static void main(String[] args) throws IOException,InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));

        String add;
        System.out.println("Insert address:");
        add = (reader.readLine());

        int choice = 0;
        do {
            System.out.println("1 for socket, 2 for RMI :");
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        }while (choice != 2 && choice != 1);

        //String add = "localhost";
        if(choice==2) {
            String name = "rmi://"+add+":45398/server";
            ClientInterface client = new RMIClient(name, 45398);
            ((RMIClient) client).startConnection();
            System.out.println("You chose RMI!");
            ((RMIClient) client).lobby();


        } else if(choice == 1){
            String address = add;
            int portSocket = 43808;
            UserView tui = new UserView();
            ClientController clientController = new ClientController(tui);
            try{
                clientController.initClient(address,portSocket);
                System.out.println("You chose Socket!");
                clientController.lobby();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
