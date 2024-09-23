package com.community_center.community_center.application.data_mapper;

import com.community_center.community_center.application.dto.AddressDTO;
import com.community_center.community_center.application.dto.CommunityCenterDTO;
import com.community_center.community_center.application.dto.CommunityCenterResponseDTO;
import com.community_center.community_center.application.dto.ResourceDTO;
import com.community_center.community_center.domain.entity.Address;
import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.entity.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class CommunityCenterMapper {
    public static CommunityCenter dtoToEntity(CommunityCenterDTO dto) {
        CommunityCenter center = new CommunityCenter();
        center.setName(dto.getName());
        center.setAddress(addressDTOToEntity(dto.getAddress()));
        center.setLocation(dto.getLocation());
        center.setMaxCapacity(dto.getMaxCapacity());
        center.setCurrentOccupancy(dto.getCurrentOccupancy());
        center.setResources(ResourceMapper.dtoToEntity(dto.getResources()));
        return center;
    }

    private static Address addressDTOToEntity (AddressDTO addressDTO) {
        if (addressDTO == null) {
            return null;
        }

        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setDistrict(addressDTO.getDistrict());
        address.setNumber(addressDTO.getNumber());
        address.setZipCode(addressDTO.getZipCode());

        return address;
    }

    public static CommunityCenterResponseDTO toReponseDTO (CommunityCenter communityCenter, boolean hasReachedCapacity) {
        CommunityCenterResponseDTO dto = new CommunityCenterResponseDTO();
        dto.setCommunityCenter(communityCenter);
        dto.setHasReachedCapacity(hasReachedCapacity);
        return dto;
    }
}
