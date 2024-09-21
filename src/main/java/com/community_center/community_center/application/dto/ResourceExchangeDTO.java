package com.community_center.community_center.application.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResourceExchangeDTO {
    private String fromCenterId;
    private String toCenterId;
    private List<ResourceDTO> resourcesExchanged;
}