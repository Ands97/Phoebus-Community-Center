package com.community_center.community_center.application.dto;

import com.community_center.community_center.domain.entity.ResourceType;
import lombok.Data;

import java.util.Map;

@Data
public class AverageResourcesResponseDTO {
    private Map<ResourceType, Double> averageResources;
}
