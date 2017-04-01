package com.meyermt.proc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Master server serves one file to any connecting client. A timeout must be set on the server in milliseconds. After the
 * period has happened, the server will terminate.
 * Created by michaelmeyer on 3/29/17.
 */
public class MasterServer {

    private static final String PORT_ARG = "--serverPort";
    private static final String FILE_ARG = "--filePath";
    private static final String TIMEOUT_ARG = "--timeout";

    /**
     * Main method for the master server. Ensures flags are set appropriately and returns error messaging if not.
     * @param args port, filepath, and timeout flags and arguments
     */
    public static void main(String[] args) {
        if (args.length == 6 && args[0].startsWith(PORT_ARG) && args[2].startsWith(FILE_ARG) && args[4].startsWith(TIMEOUT_ARG)) {
            int port = Integer.parseInt(args[1]);
            Path filePath = Paths.get(args[3]);
            int timeout = Integer.parseInt(args[5]);
            runServer(port, filePath, timeout);
        } else {
            System.out.println("Illegal arguments. Should be run with arguments: --serverPort <desired port number> --filePath <path-to-file> --timeout <timeout in millisecs>");
            System.exit(1);
        }
    }

    /*
        Starts the master file server. Continuous loop that listens for clients. For each client connection, a new thread is run.
        Passes file path and client count information to thread to be served to client.
    */
    private static void runServer(int port, Path filePath, int timeout) {
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(timeout);
            int clientCount = 1;

            while (true) {
                Socket client = server.accept();
                Thread clientThread = new Thread(new MasterHandler(client, filePath, clientCount));
                clientThread.start();
                clientCount++;
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Server has timed out and is no longer serving the file " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unrecoverable issue running server on port: " + port);
            System.exit(1);
        }
    }

}
