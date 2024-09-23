package com.community_center.community_center.domain.repository;

import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.utils.Result;

import java.util.List;

public interface CommunityCenterRepository {
    Result<List<CommunityCenter>> listCommunityCentersByOccupancyAtLeast(double occupancy, int page, int size);
    Result<List<CommunityCenter>> findAll();
    Result<CommunityCenter> save(CommunityCenter center);
    Result<CommunityCenter> findById(String centerId);
}
