package ru.dkx86.boorudownloader.dl;

import java.util.HashMap;
import java.util.Map;

public enum Rating {
    SAFE("s"),
    QUESTIONABLE("q"),
    EXPLICIT("e");

    private static final Map<String, Rating> idsToNames;

    static {
        idsToNames = new HashMap<>(values().length);
        for (Rating rating : values()) {
            idsToNames.put(rating.value, rating);
        }
    }

    private final String value;

    Rating(String value) {
        this.value = value;
    }

    public static Rating getByValue(String value) {
        return idsToNames.get(value);
    }

    public String getValue() {
        return value;
    }

}
