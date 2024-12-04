package client;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new Client().start("127.0.0.1", 8080);
    }

}
