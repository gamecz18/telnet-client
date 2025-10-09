package utb.fai;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TelnetClient {

    private String serverIp;
    private int port;
     private volatile boolean pokracovat = true;
    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try (Socket socket = new Socket(serverIp, port)) {

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            BufferedReader readerConsole = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
      
            Thread threadReader = new Thread(() -> {

                try {
                    while (pokracovat) {
                        
                        if (in.available() > 0) {
                            String buffer = reader.readLine();
                            if (buffer != null) {
                                System.out.println(buffer);
                            }
                        }
                      
                        Thread.sleep(20);
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            });
            Thread threadWriter = new Thread(() -> {

                try {
                    String buffer;
                    while ((buffer = readerConsole.readLine()) != null) {

                        if (buffer.equalsIgnoreCase("/QUIT")) {
                            pokracovat = false;
                            break;
                            
                        }

                        writer.write(buffer);
                        writer.write("\r\n");
                        writer.flush();
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
            });

            threadReader.start();
            threadWriter.start();

            try {
                threadReader.join();
                threadWriter.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
