package com.community_center.community_center.domain.repository;

import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.utils.Result;

import java.util.Date;
import java.util.List;

public interface ResourceExchangeRepository {
    Result<List<ResourceExchange>> listByFromCenterId(String centerId, int page, int size);
    Result<List<ResourceExchange>> listByFromCenterIdAndDate(
            String centerId,
            int page,
            int size,
            Date date
    );
    Result<ResourceExchange> save(ResourceExchange exchange);
}
