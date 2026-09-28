package com.sportive.court_booking_server.common.enums;

public enum SportType {
    BADMINTON("Cầu lông"),
    PICKLEBALL("Pickelball"),
    TENNIS("Tennis"),
    FOOTBALL_7("Bóng đá sân 7"),
    FOOTBALL_9("Bóng đá sân 9"),
    FOOTBALL_11("Bóng đá sân 11");

    private final String label;

    SportType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
