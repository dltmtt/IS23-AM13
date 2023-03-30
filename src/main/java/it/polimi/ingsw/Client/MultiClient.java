package it.polimi.ingsw.Client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MultiClient {
    private static final int max = 4;

    public static void main(String[] args) throws UnknownHostException {
        InetAddress address;

        if (args.length >= 2) {
            address = InetAddress.getByName(args[0]);
        }
        //JSON?
    }
}
