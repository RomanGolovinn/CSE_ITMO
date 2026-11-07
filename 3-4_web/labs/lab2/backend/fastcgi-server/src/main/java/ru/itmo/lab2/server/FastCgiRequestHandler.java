package ru.itmo.lab2.server;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
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

            Map<String, List<String>> form = parseForm(body);
            String action = firstValue(form, "action", "check");
            if ("history".equals(action)) {
                // Return the current server-side history without adding a point.
            } else if ("clear".equals(action)) {
                pointService.getHistory().clear();
            } else if ("check".equals(action)) {
                List<String> rawXs = form.getOrDefault("x", List.of());
                if (rawXs.isEmpty()) {
                    throw new IllegalArgumentException("At least one X value is required");
                }

                String y = firstValue(form, "y", null);
                String r = firstValue(form, "r", null);
                List<PointRequest> requests = new ArrayList<>();
                for (String x : rawXs) {
                    requests.add(PointValidator.validate(x, y, r));
                }
                for (PointRequest request : requests) {
                    pointService.check(request);
                }
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

    private String firstValue(Map<String, List<String>> form, String name, String defaultValue) {
        List<String> values = form.get(name);
        return values == null || values.isEmpty() ? defaultValue : values.get(0);
    }

    private Map<String, List<String>> parseForm(String body) {
        Map<String, List<String>> values = new LinkedHashMap<>();
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
            List<String> fieldValues = values.computeIfAbsent(name, ignored -> new ArrayList<>());
            if (!"x".equals(name) && !fieldValues.isEmpty()) {
                throw new IllegalArgumentException("Duplicate parameter: " + name);
            }
            fieldValues.add(value);
        }
        return values;
    }
}
