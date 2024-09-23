package com.community_center.community_center.infra.data.repository;

import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.repository.CommunityCenterRepository;
import com.community_center.community_center.utils.Result;
import com.community_center.community_center.utils.error.ErrorApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;

public class CommunityCenterRepositoryImpl implements CommunityCenterRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Result<List<CommunityCenter>> listCommunityCentersByOccupancyAtLeast(double occupancyPercentage, int page, int size) {
        try {
            Query query = new Query(Criteria.where("occupancyPercentage").gte(occupancyPercentage))
                    .skip((long) (page - 1) * size)
                    .limit(size);

            List<CommunityCenter> results = mongoTemplate.find(query, CommunityCenter.class);

            return Result.success(results);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterRepository > listCommunityCentersByOccupancyAtLeast",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }

    public Result<CommunityCenter> save(CommunityCenter center) {
        try {
            CommunityCenter centerSaved = mongoTemplate.save(center);
            return Result.success(centerSaved);
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

    public Result<List<CommunityCenter>> findAll() {
        try {
            Query query = new Query();

            query.with(Sort.by(Sort.Direction.DESC, "created"));

            List<CommunityCenter> centers = mongoTemplate.find(query, CommunityCenter.class);
            return Result.success(centers);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterRepository > findAll",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }

    public Result<CommunityCenter> findById(String centerId) {
        try {
            CommunityCenter center = mongoTemplate.findById(centerId, CommunityCenter.class);
            return Result.success(center);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterRepository > findById",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }
}
