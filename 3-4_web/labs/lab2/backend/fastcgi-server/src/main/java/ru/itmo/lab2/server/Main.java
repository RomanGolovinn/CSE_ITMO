package ru.itmo.lab2.server;

import ru.itmo.lab2.core.LabAreaChecker;
import ru.itmo.lab2.core.PointHistory;
import ru.itmo.lab2.core.PointService;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("FCGI_PORT", "9000"));
        System.setProperty("FCGI_PORT", Integer.toString(port));

        PointHistory history = new InMemoryPointHistory();
        PointService pointService = new PointService(new LabAreaChecker(), history);
        FastCgiRequestHandler requestHandler = new FastCgiRequestHandler(pointService);

        System.out.println("FastCGI server listening on port " + port);
        new FastCgiServer(requestHandler).start();
    }
}
