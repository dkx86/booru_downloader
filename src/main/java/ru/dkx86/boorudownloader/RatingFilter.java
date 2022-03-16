package ru.dkx86.boorudownloader;

import ru.dkx86.boorudownloader.dl.Rating;

import java.util.Locale;

public final class RatingFilter {

    private final boolean safe;
    private final boolean questionable;
    private final boolean explicit;

    public RatingFilter(String ratingStr) {
        if (ratingStr == null) {
            safe = true;
            questionable = true;
            explicit = true;
        } else {
            safe = ratingStr.toLowerCase(Locale.ROOT).contains(Rating.SAFE.getValue());
            questionable = ratingStr.toLowerCase(Locale.ROOT).contains(Rating.QUESTIONABLE.getValue());
            explicit = ratingStr.toLowerCase(Locale.ROOT).contains(Rating.EXPLICIT.getValue());
        }
    }

    public boolean isValid(Rating rating) {
        return switch (rating) {
            case SAFE -> safe;
            case QUESTIONABLE -> questionable;
            case EXPLICIT -> explicit;
        };

    }
}
