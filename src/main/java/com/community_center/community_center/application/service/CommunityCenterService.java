package com.community_center.community_center.application.service;

import com.community_center.community_center.application.dto.CommunityCenterDTO;
import com.community_center.community_center.application.dto.CommunityCenterResponseDTO;
import com.community_center.community_center.application.dto.OccupancyUpdateDTO;
import com.community_center.community_center.application.dto.ResourceExchangeDTO;
import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.domain.entity.ResourceType;
import com.community_center.community_center.utils.Result;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CommunityCenterService {
    Result<CommunityCenter> addCommunityCenter(CommunityCenterDTO dto);
    Result<CommunityCenterResponseDTO> updateOccupancy(String centerId, OccupancyUpdateDTO dto);
    Result<ResourceExchange> exchangeResources(ResourceExchangeDTO dto);
    Result<List<CommunityCenter>> getCentersWithHighOccupancy(int page, int size);
    Result<Map<ResourceType, Double>> getAverageResources();
    Result<List<ResourceExchange>> getExchangeHistory(String centerId, int page, int size, Date date);
}