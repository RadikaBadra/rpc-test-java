/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tesrpc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ASUS
 */
public class Server {
    
 private final ServerSocket serverSocket;

@SuppressWarnings("CallToThreadStartDuringObjectConstruction")

    public Server(int port) throws IOException {

            serverSocket = new ServerSocket(port);
            String localIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server is running on " + localIP + ":" + port);
            while (true) {
                Socket rpcClient = serverSocket.accept();
                String address = rpcClient.getRemoteSocketAddress().toString();
                System.out.println("New client connected : " + address);

            new Thread(() -> {
            try {
            addHook(rpcClient);
            } catch (IOException ex) {
                System.err.println("Client disconnected " + address);
            }
            }).start();
            }
    }

    private void addHook(Socket rpcClient) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(rpcClient.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
        System.out.println("Client request : " + line);
        String[] commands = line.split(":", 3);
        int result;
        int operand1 = Integer.parseInt(commands[1]);
        int operand2 = Integer.parseInt(commands[2]);
        String message = "";

        switch (commands[0]) {
        case "add":
        result = (operand1 + operand2);
        message = operand1 + " + " + operand2 + " = " + result;
        break;

        case "sub":
        result = (operand1 - operand2);
        message = operand1 + " - " + operand2 + " = " + result;
        break;

        case "mul":
        result = (operand1 * operand2);
        message = operand1 + " * " + operand2 + " = " + result;
        break;

        case "div":
        result = (operand1 / operand2);
        message = operand1 + " / " + operand2 + " = " + result;
        break;

        case "mod":
        result = (operand1 % operand2);
        message = operand1 + " % " + operand2 + " = " + result;
        break;

        }

        PrintStream printStream = new PrintStream(rpcClient.getOutputStream(), true);
        printStream.println(message);

        }

    }

    public static void main(String[] args) throws Exception {

    Server server = new Server(3000);

    }     
}
