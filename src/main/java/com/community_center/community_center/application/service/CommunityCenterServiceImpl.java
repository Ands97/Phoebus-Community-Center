package com.community_center.community_center.application.service;

import com.community_center.community_center.application.dto.CommunityCenterDTO;
import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.repository.CommunityCenterRepository;
import com.community_center.community_center.domain.repository.ResourceExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityCenterServiceImpl implements CommunityCenterService {
    @Autowired
    private CommunityCenterRepository communityCenterRepository;

    @Autowired
    private ResourceExchangeRepository resourceExchangeRepository;

    public CommunityCenter addCommunityCenter(CommunityCenterDTO dto) {
        CommunityCenter center = mapDtoToEntity(dto);
    }
}
