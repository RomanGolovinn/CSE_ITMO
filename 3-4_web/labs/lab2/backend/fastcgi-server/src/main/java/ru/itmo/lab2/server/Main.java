package ru.itmo.lab2.server;

import java.io.IOException;
import ru.itmo.lab2.core.LabAreaChecker;
import ru.itmo.lab2.core.PointHistory;
import ru.itmo.lab2.core.PointService;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("FCGI_PORT", "9000"));
        String host = System.getenv().getOrDefault("FCGI_HOST", "127.0.0.1");

        PointHistory history = new InMemoryPointHistory();
        PointService pointService = new PointService(new LabAreaChecker(), history);
        FastCgiRequestHandler requestHandler = new FastCgiRequestHandler(pointService);

        new FastCgiServer(host, port, requestHandler).start();
    }
}
