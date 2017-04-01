package com.meyermt.proc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple handler that serves first which number of client is being served and then specified file for each client
 * thread.
 * Created by michaelmeyer on 4/1/17.
 */
public class MasterHandler implements Runnable {

    private Socket client;
    private Path fileToSend;
    private int clientCount;

    /**
     * Instantiates a new MasterHandler.
     *
     * @param client the client requesting the file
     * @param fileToSend the file that will be served to the client
     * @param clientCount the number of the client being served
     */
    public MasterHandler(Socket client, Path fileToSend, int clientCount) {
        this.client = client;
        this.fileToSend = fileToSend;
        this.clientCount = clientCount;
    }

    public void run() {
        try {
            PrintWriter output = new PrintWriter(client.getOutputStream(), true);
            output.println(clientCount);
            Files.readAllLines(fileToSend).forEach(line -> output.println(line));
            client.close();
        }catch (IOException e) {
            throw new RuntimeException("Exception encountered serving file " + fileToSend.toString(), e);
        }
    }
}
