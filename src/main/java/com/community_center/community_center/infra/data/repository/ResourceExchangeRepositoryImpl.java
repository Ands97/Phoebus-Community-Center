package com.community_center.community_center.infra.data.repository;

import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.domain.repository.ResourceExchangeRepository;
import com.community_center.community_center.utils.Result;
import com.community_center.community_center.utils.error.ErrorApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ResourceExchangeRepositoryImpl implements ResourceExchangeRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Result<List<ResourceExchange>> listByFromCenterId(String centerId, int page, int size) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("fromCenterId").is(centerId));
            query.skip((long) (page - 1) * size);
            query.limit(size);
            List<ResourceExchange> exchanges = mongoTemplate.find(query, ResourceExchange.class);
            return Result.success(exchanges);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "ResouceExchangeRepository > listByFromCenterId",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }

    public Result<List<ResourceExchange>> listByFromCenterIdAndDate(
            String centerId,
            int page,
            int size,
            Date date
    ) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("fromCenterId").is(centerId));
            query.addCriteria(Criteria.where("createdAt").gt(date));
            query.skip((long) (page - 1) * size);
            query.limit(size);

            List<ResourceExchange> exchanges = mongoTemplate.find(query, ResourceExchange.class);
            return Result.success(exchanges);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "ResouceExchangeRepository > listByFromCenterIdTimestamp",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }

    @Override
    public Result<ResourceExchange> save(ResourceExchange exchange) {
        try {
            ResourceExchange exchangeSaved = mongoTemplate.save(exchange);
            return Result.success(exchangeSaved);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterRepository > save",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }
}
