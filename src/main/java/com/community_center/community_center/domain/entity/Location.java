package com.community_center.community_center.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Localização de um centro de coleta")
public class Location {
    private double latitude;
    private double longitude;
}
