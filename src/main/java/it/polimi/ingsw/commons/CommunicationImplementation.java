package it.polimi.ingsw.commons;

public class CommunicationImplementation implements CommunicationInterface {
    // Maybe we should have a reference to the model/controller here

    @Override
    public String sendMessage(String clientMessage) {
        if (clientMessage.startsWith("login")) {
            // TODO: parse the JSON, this is just a mock
            // Maybe we should use different methods for different requests
            String username = clientMessage.substring(5);
            System.out.println(username + " requested login.");

            System.out.print("Manipulating the model or whatever to login the user... ");
            // TODO: do crazy things
            System.out.println("done (I'm lying).");

            // This should be a JSON that the view will parse and display
            return "Welcome, " + username + "!\n";
        } else {
            System.out.println(clientMessage + " requested unknown");
            return "Unknown request";
        }
    }
}
