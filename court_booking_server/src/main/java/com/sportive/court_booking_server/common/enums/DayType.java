package com.sportive.court_booking_server.common.enums;

import java.time.DayOfWeek;

public enum DayType {
    WEEKDAY,
    WEEKEND,
    HOLIDAY;

    public static DayType from(DayOfWeek dayOfWeek) {
        if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return WEEKEND;
        }
        return WEEKDAY;
    }
}