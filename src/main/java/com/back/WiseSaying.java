package com.back;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class WiseSaying {
    private int id;
    private String content;
    private String author;

    @Override
    public String toString() {
        return "%d / %s / %s".formatted(id, author, content);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("id", id);
        map.put("content", content);
        map.put("author", author);

        return map;
    }

    public WiseSaying(Map<String, Object> wiseSayingMap) {
        this.id = (Integer) wiseSayingMap.get("id");
        this.content = (String) wiseSayingMap.get("content");
        this.author = (String) wiseSayingMap.get("author");
    }
}