package com.community_center.community_center.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "Community Center occupancy update")
public class OccupancyUpdateDTO {
    @Min(0)
    private int currentOccupancy;
}
