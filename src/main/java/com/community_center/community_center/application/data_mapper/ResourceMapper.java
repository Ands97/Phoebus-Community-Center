package com.community_center.community_center.application.data_mapper;

import com.community_center.community_center.application.dto.ResourceDTO;
import com.community_center.community_center.domain.entity.Resource;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceMapper {
    public static List<Resource> dtoToEntity(List<ResourceDTO> resourceDTO) {
        return resourceDTO.stream().map(dto -> {
            Resource resource = new Resource();
            resource.setType(dto.getType());
            resource.setQuantity(dto.getQuantity());
            return resource;
        }).collect(Collectors.toList());
    }
}
