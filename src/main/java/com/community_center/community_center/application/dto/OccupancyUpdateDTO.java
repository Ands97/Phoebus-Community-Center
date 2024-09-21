package com.community_center.community_center.application.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OccupancyUpdateDTO {
    @Min(0)
    private int currentOccupancy;
}
