package com.community_center.community_center.application.service;

import com.community_center.community_center.application.data_mapper.ResourceMapper;
import com.community_center.community_center.application.dto.*;
import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.entity.Resource;
import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.domain.entity.ResourceType;
import com.community_center.community_center.domain.repository.CommunityCenterRepository;
import com.community_center.community_center.domain.repository.ResourceExchangeRepository;
import com.community_center.community_center.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommunityCenterServiceTest {

    @Mock
    private CommunityCenterRepository communityCenterRepository;

    @Mock
    private ResourceExchangeRepository resourceExchangeRepository;

    @InjectMocks
    private CommunityCenterServiceImpl communityCenterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCommunityCenter() {
        CommunityCenterDTO dto = new CommunityCenterDTO();
        dto.setName("Test Center");
        List<ResourceDTO> resources = new ArrayList<>();
        ResourceDTO resource = new ResourceDTO();
        resource.setType(ResourceType.DOCTOR);
        resource.setQuantity(1);
        resources.add(resource);
        dto.setResources(new ArrayList<ResourceDTO>(resources));
        CommunityCenter center = new CommunityCenter();
        center.setName("Test Center");

        when(communityCenterRepository.save(any(CommunityCenter.class))).thenReturn(Result.success(center));

        Result<CommunityCenter> result = communityCenterService.addCommunityCenter(dto);

        CommunityCenter centerResponse = result.response;
        assertNotNull(result);
        assertEquals("Test Center", centerResponse.getName());
        verify(communityCenterRepository, times(1)).save(any(CommunityCenter.class));
    }

    @Test
    void testAddCommunityCenterError() {
        CommunityCenterDTO dto = new CommunityCenterDTO();
        dto.setName("Test Center");
        List<ResourceDTO> resources = new ArrayList<>();
        ResourceDTO resource = new ResourceDTO();
        resource.setType(ResourceType.DOCTOR);
        resource.setQuantity(1);
        resources.add(resource);
        dto.setResources(resources);

        when(communityCenterRepository.save(any(CommunityCenter.class))).thenThrow(new RuntimeException("Database error"));

        Result<CommunityCenter> result = communityCenterService.addCommunityCenter(dto);

        assertNotNull(result);
        assertFalse(result.success);
        assertEquals("CommunityCenterService > addCommunityCenter", result.error.getProcess());
        assertEquals("Database error", result.error.getMessage());
        verify(communityCenterRepository, times(1)).save(any(CommunityCenter.class));
    }

    @Test
    void testUpdateOccupancySuccess() {
        String centerId = "123";
        OccupancyUpdateDTO dto = new OccupancyUpdateDTO();
        dto.setCurrentOccupancy(50);

        CommunityCenter center = new CommunityCenter();
        center.setId(centerId);
        center.setMaxCapacity(100);
        center.setCurrentOccupancy(30);

        when(communityCenterRepository.findById(centerId)).thenReturn(Result.success(center));
        when(communityCenterRepository.save(any(CommunityCenter.class))).thenReturn(Result.success(center));

        Result<CommunityCenterResponseDTO> result = communityCenterService.updateOccupancy(centerId, dto);

        assertNotNull(result);
        assertTrue(result.success);
        assertEquals(50, result.response.getCommunityCenter().getCurrentOccupancy());
        verify(communityCenterRepository, times(1)).findById(centerId);
        verify(communityCenterRepository, times(1)).save(any(CommunityCenter.class));
    }

    @Test
    void testUpdateOccupancyError_ExceedsCapacity() {
        String centerId = "123";
        OccupancyUpdateDTO dto = new OccupancyUpdateDTO();
        dto.setCurrentOccupancy(150);

        CommunityCenter center = new CommunityCenter();
        center.setId(centerId);
        center.setMaxCapacity(100);

        when(communityCenterRepository.findById(centerId)).thenReturn(Result.success(center));

        Result<CommunityCenterResponseDTO> result = communityCenterService.updateOccupancy(centerId, dto);

        assertNotNull(result);
        assertFalse(result.success);
        assertEquals("Current occupancy cannot be greater than max capacity. Max campacity: 100", result.error.getMessage());
        verify(communityCenterRepository, times(1)).findById(centerId);
        verify(communityCenterRepository, never()).save(any(CommunityCenter.class)); // Não deve salvar
    }

    @Test
    void testExchangeResourcesSuccess() {
        ResourceExchangeDTO dto = new ResourceExchangeDTO();
        dto.setFromCenterId("123");
        dto.setToCenterId("321");
        ResourceDTO resource = new ResourceDTO();
        List<ResourceDTO> resourcesDTO = new ArrayList<>();
        resource.setType(ResourceType.DOCTOR);
        resource.setQuantity(1);
        resourcesDTO.add(resource);
        dto.setResourcesSent(resourcesDTO);
        dto.setResourcesReceived(resourcesDTO);

        CommunityCenter fromCenter = new CommunityCenter();
        fromCenter.setId("123");
        List<Resource> resources = ResourceMapper.dtoToEntity(resourcesDTO);
        fromCenter.setResources(resources);

        CommunityCenter toCenter = new CommunityCenter();
        toCenter.setId("321");
        toCenter.setResources(resources);

        when(communityCenterRepository.findById("123")).thenReturn(Result.success(fromCenter));
        when(communityCenterRepository.findById("321")).thenReturn(Result.success(toCenter));
        when(resourceExchangeRepository.save(any(ResourceExchange.class))).thenReturn(Result.success(new ResourceExchange()));

        Result<ResourceExchange> result = communityCenterService.exchangeResources(dto);

        assertNotNull(result);
        assertTrue(result.success);
        verify(communityCenterRepository, times(1)).findById("123");
        verify(communityCenterRepository, times(1)).findById("321");
        verify(resourceExchangeRepository, times(1)).save(any(ResourceExchange.class));
    }

    @Test
    void testExchangeResourcesError_SameCenter() {
        ResourceExchangeDTO dto = new ResourceExchangeDTO();
        dto.setFromCenterId("123");
        dto.setToCenterId("123");

        Result<ResourceExchange> result = communityCenterService.exchangeResources(dto);

        assertNotNull(result);
        assertFalse(result.success);
        assertEquals("Cannot exchange resources between the same center", result.error.getMessage());
    }

    @Test
    void testGetCentersWithHighOccupancySuccess() {
        when(communityCenterRepository.listCommunityCentersByOccupancyAtLeast(90.0, 0, 10))
                .thenReturn(Result.success(Arrays.asList(new CommunityCenter())));

        Result<List<CommunityCenter>> result = communityCenterService.getCentersWithHighOccupancy(0, 10);

        assertNotNull(result);
        assertTrue(result.success);
        assertFalse(result.response.isEmpty());
        verify(communityCenterRepository, times(1)).listCommunityCentersByOccupancyAtLeast(90.0, 0, 10);
    }

    @Test
    void testGetCentersWithHighOccupancyError() {
        when(communityCenterRepository.listCommunityCentersByOccupancyAtLeast(90.0, 0, 10))
                .thenThrow(new RuntimeException("Database error"));

        Result<List<CommunityCenter>> result = communityCenterService.getCentersWithHighOccupancy(0, 10);

        assertNotNull(result);
        assertFalse(result.success);
        assertEquals("Database error", result.error.getMessage());
    }

    @Test
    void testGetAverageResourcesSuccess() {
        CommunityCenter center = new CommunityCenter();
        List<Resource> resources = new ArrayList<>();
        Resource resource = new Resource();
        resource.setType(ResourceType.DOCTOR);
        resource.setQuantity(1);
        resources.add(resource);
        center.setResources(resources);
        when(communityCenterRepository.findAll()).thenReturn(Result.success(Arrays.asList(center)));

        Result<Map<ResourceType, Double>> result = communityCenterService.getAverageResources();

        assertNotNull(result);
        assertTrue(result.success);
        verify(communityCenterRepository, times(1)).findAll();
    }

    @Test
    void testGetAverageResourcesError() {
        when(communityCenterRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Result<Map<ResourceType, Double>> result = communityCenterService.getAverageResources();

        assertNotNull(result);
        assertFalse(result.success);
        assertEquals("Database error", result.error.getMessage());
    }

    @Test
    void testGetExchangeHistorySuccess() {
        String centerId = "center1";
        Date date = null;
        when(resourceExchangeRepository.listByFromCenterId(centerId, 0, 10))
                .thenReturn(Result.success(Arrays.asList(new ResourceExchange())));

        Result<List<ResourceExchange>> result = communityCenterService.getExchangeHistory(centerId, 0, 10, date);

        assertNotNull(result);
        assertTrue(result.success);
        verify(resourceExchangeRepository, times(1)).listByFromCenterId(centerId, 0, 10);
    }
}
