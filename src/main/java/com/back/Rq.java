package com.back;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Rq {
    private final String actionName;
    private final Map<String, String> params;

    public Rq(String cmd) {
        String[] cmdBits = cmd.split("\\?", 2);
        actionName = cmdBits[0].trim();
        String queryString = cmdBits.length > 1 ? cmdBits[1].trim() : "";

        params = Arrays.stream(queryString.split("&"))
                .map(s -> s.split("=", 2))
                .filter(e -> e.length == 2 && !e[0].isBlank() && !e[1].isBlank())
                .collect(Collectors.toMap(
                        e -> e[0].trim(),
                        e -> e[1].trim(),
                        (existing, replacement) -> existing
                ));
    }

    public String getParam(String paramName, String defaultValue) {
        return params.getOrDefault(paramName, defaultValue);
    }

    public int getParamInt(String paramName, int defaultValue) {
        String value = getParam(paramName, "");

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getActionName() {
        return actionName;
    }
}
