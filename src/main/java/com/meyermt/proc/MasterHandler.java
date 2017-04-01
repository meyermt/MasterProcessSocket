package com.meyermt.proc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by michaelmeyer on 4/1/17.
 */
public class MasterHandler implements Runnable {

    private Socket client;
    private Path fileToSend;

    /**
     * Instantiates a new MasterHandler.
     *
     * @param client the client making a request
     */
    public MasterHandler(Socket client, Path fileToSend) {
        this.client = client;
        this.fileToSend = fileToSend;
    }

    public void run() {
        try {
            PrintWriter output = new PrintWriter(client.getOutputStream(), true);
            Files.readAllLines(fileToSend).forEach(line -> output.println(line));
            client.close();
        }catch (IOException e) {
            // throw a runtime here
        }
    }
}
