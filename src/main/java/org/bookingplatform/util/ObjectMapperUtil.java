package org.bookingplatform.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class ObjectMapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> fromJson(String json) {
        try {
            if (json == null || json.trim().isEmpty()) {
                return Collections.emptyList();
            }
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize list: " + json, e);
        }
    }

    public static String toJson(List<String> list) {
        try {
            if (list == null || list.isEmpty()) {
                return "[]";
            }
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize list", e);
        }
    }
}
