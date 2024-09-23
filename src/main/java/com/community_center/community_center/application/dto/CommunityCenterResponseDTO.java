package com.community_center.community_center.application.dto;

import com.community_center.community_center.domain.entity.CommunityCenter;
import lombok.Data;

@Data
public class CommunityCenterResponseDTO {
    private CommunityCenter communityCenter;
    private boolean hasReachedCapacity;
}
