package com.community_center.community_center.application.data_mapper;

import com.community_center.community_center.application.dto.CommunityCenterDTO;
import com.community_center.community_center.domain.entity.CommunityCenter;

public class CommunityCenterMapper {
    public static CommunityCenter dtoToEntity(CommunityCenterDTO dto) {
        CommunityCenter center = new CommunityCenter();
        center.setName(dto.getName());
        return center;
    }
}
