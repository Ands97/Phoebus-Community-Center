package com.community_center.community_center.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "resource_exchanges")
public class ResourceExchange {
    @Id
    private String id;
    private String fromCenterId;
    private String toCenterId;
    private List<Resource> resourcesSent;
    private List<Resource> resourcesReceived;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
