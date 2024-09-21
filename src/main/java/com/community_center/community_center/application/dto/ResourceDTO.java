package com.community_center.community_center.application.dto;

import com.community_center.community_center.domain.entity.ResourceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResourceDTO {
    @NotNull
    private ResourceType type;
    @Min(0)
    private int quantity;
}
