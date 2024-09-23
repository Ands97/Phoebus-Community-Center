package com.community_center.community_center.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Address")
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
