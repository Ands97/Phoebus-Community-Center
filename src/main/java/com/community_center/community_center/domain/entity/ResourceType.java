package com.community_center.community_center.domain.entity;

import lombok.Getter;

@Getter
public enum ResourceType {
    DOCTOR(4),
    VOLUNTEER(3),
    MEDICAL_SUPPLIES(7),
    TRANSPORT_VEHICLE(5),
    FOOD_BASKET(2);

    private final int points;

    ResourceType(int points) {
        this.points = points;
    }

}
