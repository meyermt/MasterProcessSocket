package com.meyermt.proc;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelmeyer on 3/29/17.
 */
public class MasterClient {

    // We will assume that a client's connection to the master is always hardwired, so can be hardcoded
    private final static String MASTER_IP = "127.0.0.1";
    private final static int MASTER_PORT = 12345;
    private final static String PROC_ARG = "--procNum";

    public static void main(String[] args) {
        // Should only have one arg for process number. Used in log file creation
        if (args.length == 2 && args[0].startsWith(PROC_ARG)) {
            int processNumber = Integer.parseInt(args[1]);
            runClient(processNumber);
        } else {
            System.out.println("Illegal arguments. Should be run with arguments: --procNum <process number>");
            System.exit(1);
        }

    }

    /*
        Runs a master server client.
    */
    private static void runClient(int processNumber) {
        try (
                Socket client = new Socket(MASTER_IP, MASTER_PORT);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ) {
            Path outputFilePath = Paths.get("log_process" + processNumber + ".log");
            String clientInput;
            List<String> outputLines = new ArrayList<>();
            while ((clientInput = input.readLine()) != null) {
                outputLines.add(clientInput);
                System.out.println("Got this message: " + clientInput);
            }
            Files.write(outputFilePath, outputLines);
            client.close();
        } catch (IOException e) {
            System.out.println("Error in socket connection.");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

}
