package com.inverseapps.punchcard.utils;


import com.inverseapps.punchcard.time.Clock;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    /**
     * Determines whether or not the input year has already passed.
     *
     * @param years the input year, as a two or four-digit integer
     * @return {@code true} if the year has passed, {@code false} otherwise.
     */
    public static boolean hasYearPassed(int years) {
        int normalized = normalizeYear(years);
        Calendar now = Clock.getCalendarInstance();
        return normalized < now.get(Calendar.YEAR);
    }

    /**
     * Determines whether the input year-month pair has passed.
     *
     * @param years  the input year, as a two or four-digit integer
     * @param month the input month
     * @return {@code true} if the input time has passed, {@code false} otherwise.
     */
    public static boolean hasMonthPassed(int years, int month) {
        if (hasYearPassed(years)) {
            return true;
        }

        Calendar now = Clock.getCalendarInstance();
        // Expires at end of specified month, Calendar month starts at 0
        return normalizeYear(years) == now.get(Calendar.YEAR)
                && month < (now.get(Calendar.MONTH) + 1);
    }

    // Convert two-digit year to full year if necessary
    private static int normalizeYear(int years) {
        if (years < 100 && years >= 0) {
            Calendar now = Clock.getCalendarInstance();
            String currentYear = String.valueOf(now.get(Calendar.YEAR));
            String prefix = currentYear.substring(0, currentYear.length() - 2);
            years = Integer.parseInt(String.format(Locale.US, "%s%02d", prefix, years));
        Logger.d(years);
        }
        return years;
    }
}
