package com.community_center.community_center.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "community_centers")
public class CommunityCenter {
    @Id
    private String id;
    private String name;
    private Address address;
    private Location location;
    private int maxCapacity;
    private int currentOccupancy;
    private double occupancyPercentage;
    private List<Resource> resources;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
