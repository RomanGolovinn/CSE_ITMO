package ru.itmo.lab2.server;

public final class HandlerResponse {
    private final int status;
    private final String body;

    public HandlerResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
