package com.community_center.community_center.domain.entity;

import lombok.Data;

@Data
public class Resource {
    private ResourceType type;
    private int quantity;
}
