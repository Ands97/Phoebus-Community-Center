package com.community_center.community_center.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Exchange of Resources between centers")
public class ResourceExchangeDTO {
    private String fromCenterId;
    private String toCenterId;
    private List<ResourceDTO> resourcesSent;
    private List<ResourceDTO> resourcesReceived;
}