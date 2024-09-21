package com.community_center.community_center.domain.repository;

import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.utils.Result;

import java.util.List;

public interface ResourceExchangeRepository {
    Result<List<ResourceExchange>> listByFromCenterId(String centerId);
    Result<List<ResourceExchange>> listByFromCenterIdTimestamp(String centerId, long timestamp);
}
