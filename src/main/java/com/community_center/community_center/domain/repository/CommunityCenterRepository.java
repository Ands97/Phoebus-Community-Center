package com.community_center.community_center.domain.repository;

import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.utils.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommunityCenterRepository extends MongoRepository<CommunityCenter, String> {
    Result<List<CommunityCenter>> listByCurrentOccupancy(int occupancy);
}
