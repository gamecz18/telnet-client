package utb.fai;

public class App {

    public static void main(String[] args) {
        // TODO: Implement input parameter processing
        String ip;
        int port;
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
            ip = args[0];

        } else {
            System.out.println("Using default parameters: port=12345, max_threads=10");
            port = 12345;
            ip = "127.0.0.1";

        }

        TelnetClient telnetClient = new TelnetClient(ip, port);
        telnetClient.run(); // run telnet client
    }
}
