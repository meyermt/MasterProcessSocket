package com.meyermt.proc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by michaelmeyer on 3/29/17.
 */
public class MasterServer {

    private static final String PORT_ARG = "--serverPort";
    private static final String FILE_ARG = "--filePath";

    public static void main(String[] args) {
        // Should only have one arg for port
        int port = 0;
        if (args.length == 4 && args[0].startsWith(PORT_ARG) && args[2].startsWith(FILE_ARG)) {
            port = Integer.parseInt(args[1]);
            Path filePath = Paths.get(args[3]);
            runServer(port, filePath);
        } else {
            System.out.println("Illegal arguments. Should be run with arguments: --serverPort <desired port number>.");
            System.exit(1);
        }
    }

    /*
        Starts the web server. Continuous loop that listens for clients. For each client connection, a new thread is run.
    */
    private static void runServer(int port, Path filePath) {
        try {
            ServerSocket server = new ServerSocket(port);
            // neverending loop

            System.out.println("Server started. To stop server press CTRL + C");
            while (true) {
                Socket client = server.accept();
                new Thread(new MasterHandler(client, filePath)).start();
            }
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Unrecoverable issue running server on port: " + port);
            System.exit(1);
        }
    }

}
