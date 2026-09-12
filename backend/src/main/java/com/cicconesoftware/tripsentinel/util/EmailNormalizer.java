package com.cicconesoftware.tripsentinel.util;

import java.util.Locale;

public final class EmailNormalizer {

    private EmailNormalizer() {
    }

    public static String normalize(String email) {
        if (email == null) {
            return null;
        }

        return email.strip().toLowerCase(Locale.ROOT);
    }
}