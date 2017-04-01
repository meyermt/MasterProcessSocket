package com.meyermt.proc;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple client implementation for the master. Retrieves and writes one file that master is serving. Must enter master's
 * IP and port to connect. The file that is written includes the count of client in its name.
 * Created by michaelmeyer on 3/29/17.
 */
public class MasterClient {

    // We will assume that a client's connection to the master is always hardwired, so can be hardcoded
    private final static String IP_ARG = "--masterIP";
    private final static String PORT_ARG = "--masterPort";

    public static void main(String[] args) {
        // Should only have one arg for process number. Used in log file creation
        if (args.length == 4 && args[0].startsWith(IP_ARG) && args[2].startsWith(PORT_ARG)) {
            String masterIP = args[1];
            int masterPort = Integer.parseInt(args[3]);
            runClient(masterIP, masterPort);
        } else {
            System.out.println("Illegal arguments. Should be run with arguments: --masterIP <ip> --masterPort <port>");
            System.exit(1);
        }

    }

    /*
        Runs a master server client. Uses the first read line as a count of client used in file name. The rest of the
        input is written to the log process file.
    */
    private static void runClient(String masterIP, int masterPort) {
        try (
                Socket client = new Socket(masterIP, masterPort);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ) {
            String clientInput = input.readLine();
            Path outputFilePath = Paths.get("log_process" + clientInput + ".log");
            List<String> outputLines = new ArrayList<>();
            while ((clientInput = input.readLine()) != null) {
                outputLines.add(clientInput);
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
