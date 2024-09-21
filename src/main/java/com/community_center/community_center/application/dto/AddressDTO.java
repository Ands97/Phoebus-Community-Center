package com.community_center.community_center.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDTO {
    @NotBlank
    private String street;
    @NotBlank
    private String number;
    @NotBlank
    private String district;
    @NotBlank
    private String zipCode;
}
