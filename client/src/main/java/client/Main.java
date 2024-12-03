package client;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new Client().start("192.168.1.220", 8080);
    }

}
