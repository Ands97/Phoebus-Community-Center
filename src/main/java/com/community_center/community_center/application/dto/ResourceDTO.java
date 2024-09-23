package com.community_center.community_center.application.dto;

import com.community_center.community_center.domain.entity.ResourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Resource available at the community center")
public class ResourceDTO {
    @NotNull
    private ResourceType type;
    @Min(0)
    private int quantity;
}
