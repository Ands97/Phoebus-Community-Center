package com.community_center.community_center.application.service;

import com.community_center.community_center.application.dto.CommunityCenterDTO;
import com.community_center.community_center.application.dto.OccupancyUpdateDTO;
import com.community_center.community_center.application.dto.ResourceExchangeDTO;
import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.domain.entity.ResourceType;

import java.util.List;
import java.util.Map;

public interface CommunityCenterService {
    CommunityCenter addCommunityCenter(CommunityCenterDTO dto);
    CommunityCenter updateOccupancy(String centerId, OccupancyUpdateDTO dto);
    ResourceExchange exchangeResources(ResourceExchangeDTO dto);
    List<CommunityCenter> getCentersWithHighOccupancy();
    Map<ResourceType, Double> getAverageResources();
    List<ResourceExchange> getExchangeHistory(String centerId, Long fromTimestamp);
}