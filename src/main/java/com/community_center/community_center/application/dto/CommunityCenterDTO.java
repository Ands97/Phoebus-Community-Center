package com.community_center.community_center.application.dto;

import com.community_center.community_center.domain.entity.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Community Center")
public class CommunityCenterDTO {
    @NotBlank
    private String name;
    @NotNull
    private AddressDTO address;
    @NotNull
    private Location location;
    @Min(1)
    private int maxCapacity;
    @Min(0)
    private int currentOccupancy;
    private List<ResourceDTO> resources;
}
