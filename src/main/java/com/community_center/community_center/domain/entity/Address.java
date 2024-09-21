package com.community_center.community_center.domain.entity;

import lombok.Data;

@Data
public class Address {
    private String street;
    private String number;
    private String district;
    private String zipCode;
}
