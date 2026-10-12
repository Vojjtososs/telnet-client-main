package utb.fai;

import java.io.*;
import java.net.*;

public class TelnetClient {

    private String serverIp;
    private int port;

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try (Socket socket = new Socket(serverIp, port)) {

            Thread readerThread = new Thread(() -> {
                try {
                    InputStream input = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    
                    while ((bytesRead = input.read(buffer)) != -1) {
                        System.out.write(buffer, 0, bytesRead);
                        System.out.flush();
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) {
                    }
                }
            });
            readerThread.start();

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            OutputStream output = socket.getOutputStream();
            String userInput;

            while ((userInput = consoleReader.readLine()) != null) {
                if ("/quit".equalsIgnoreCase(userInput.trim())) {
                    break;
                }
                
                userInput += "\r\n";
                output.write(userInput.getBytes());
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
