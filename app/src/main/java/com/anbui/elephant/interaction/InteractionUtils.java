/*
 * Copyright (C) 2026 Nguyen Bao An Bui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.anbui.elephant.interaction;

import java.util.Locale;

public class InteractionUtils {
    public static String formatCount(int number) {
        if (number >= 1_000_000) {
            return String.format(Locale.US, "%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000) {
            return String.format(Locale.US, "%.1fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }
}
