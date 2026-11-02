package ru.itmo.lab2.server;

import java.time.Instant;
import java.util.List;
import ru.itmo.lab2.core.PointResult;

public final class JsonResponseWriter {
    private JsonResponseWriter() {
    }

    public static String write(List<PointResult> results, Instant currentTime,
                               double executionTimeMs, String error) {
        StringBuilder json = new StringBuilder();
        json.append("{\"currentTime\":").append(quote(currentTime.toString()));
        json.append(",\"executionTimeMs\":").append(executionTimeMs);
        if (error != null) {
            json.append(",\"error\":").append(quote(error));
        }
        json.append(",\"results\":[");

        for (int i = 0; i < results.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            PointResult result = results.get(i);
            json.append("{\"x\":").append(result.getX())
                    .append(",\"y\":").append(result.getY())
                    .append(",\"r\":").append(result.getR())
                    .append(",\"hit\":").append(result.isHit())
                    .append(",\"timestamp\":").append(quote(result.getCheckedAt().toString()))
                    .append('}');
        }

        return json.append("]}").toString();
    }

    private static String quote(String value) {
        StringBuilder escaped = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            switch (character) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (character < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) character));
                    } else {
                        escaped.append(character);
                    }
            }
        }
        return escaped.append('"').toString();
    }
}
