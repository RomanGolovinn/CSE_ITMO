package ru.itmo.lab2.server;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import ru.itmo.lab2.core.PointRequest;
import ru.itmo.lab2.core.PointService;
import ru.itmo.lab2.core.PointValidator;

public final class FastCgiRequestHandler {
    private final PointService pointService;

    public FastCgiRequestHandler(PointService pointService) {
        this.pointService = pointService;
    }

    public HandlerResponse handle(Map<String, String> environment, String body) {
        long startedAt = System.nanoTime();
        int status = 200;
        String error = null;

        try {
            String method = environment.get("REQUEST_METHOD");
            if (!"POST".equalsIgnoreCase(method)) {
                status = 405;
                throw new IllegalArgumentException("Only POST requests are supported");
            }

            String contentType = environment.get("CONTENT_TYPE");
            if (contentType == null || !contentType.toLowerCase().startsWith("application/x-www-form-urlencoded")) {
                status = 415;
                throw new IllegalArgumentException("Content-Type must be application/x-www-form-urlencoded");
            }

            Map<String, String> form = parseForm(body);
            String action = form.getOrDefault("action", "check");
            if ("history".equals(action)) {
                // Return the current server-side history without adding a point.
            } else if ("clear".equals(action)) {
                pointService.getHistory().clear();
            } else if ("check".equals(action)) {
                PointRequest request = PointValidator.validate(
                        form.get("x"),
                        form.get("y"),
                        form.get("r")
                );
                pointService.check(request);
            } else {
                throw new IllegalArgumentException("Unknown action");
            }
        } catch (IllegalArgumentException exception) {
            if (status == 200) {
                status = 400;
            }
            error = exception.getMessage();
        }

        double executionTimeMs = (System.nanoTime() - startedAt) / 1_000_000.0;
        String json = JsonResponseWriter.write(
                pointService.getHistory().getAll(),
                Instant.now(),
                executionTimeMs,
                error
        );
        return new HandlerResponse(status, json);
    }

    private Map<String, String> parseForm(String body) {
        Map<String, String> values = new LinkedHashMap<>();
        if (body == null || body.isEmpty()) {
            return values;
        }

        for (String pair : body.split("&", -1)) {
            if (pair.isEmpty()) {
                continue;
            }
            int separator = pair.indexOf('=');
            String encodedName = separator < 0 ? pair : pair.substring(0, separator);
            String encodedValue = separator < 0 ? "" : pair.substring(separator + 1);
            String name = URLDecoder.decode(encodedName, StandardCharsets.UTF_8);
            String value = URLDecoder.decode(encodedValue, StandardCharsets.UTF_8);
            if (values.putIfAbsent(name, value) != null) {
                throw new IllegalArgumentException("Duplicate parameter: " + name);
            }
        }
        return values;
    }
}
