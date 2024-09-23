package com.community_center.community_center.presentation.controller;

import com.community_center.community_center.application.dto.CommunityCenterDTO;
import com.community_center.community_center.application.dto.CommunityCenterResponseDTO;
import com.community_center.community_center.application.dto.OccupancyUpdateDTO;
import com.community_center.community_center.application.dto.ResourceExchangeDTO;
import com.community_center.community_center.application.service.CommunityCenterService;
import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.domain.entity.ResourceType;
import com.community_center.community_center.utils.Result;
import com.community_center.community_center.utils.error.ErrorApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommunityCenterControllerTest {

    @Mock
    private CommunityCenterService communityCenterService;

    @InjectMocks
    private CommunityCenterController communityCenterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCommunityCenterSuccess() {
        // Arrange
        CommunityCenterDTO dto = new CommunityCenterDTO();
        CommunityCenter communityCenter = new CommunityCenter();
        Result<CommunityCenter> result = Result.success(communityCenter);

        when(communityCenterService.addCommunityCenter(any(CommunityCenterDTO.class))).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.addCommunityCenter(dto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(communityCenter, response.getBody());
        verify(communityCenterService, times(1)).addCommunityCenter(dto);
    }

    @Test
    void testAddCommunityCenterError() {
        // Arrange
        CommunityCenterDTO dto = new CommunityCenterDTO();
        Result<CommunityCenter> result = Result.error(
                new ErrorApplication(
                        "CommunityService > addCommunityCenter",
                        "Error adding community center",
                        400,
                        Collections.emptyList()
                )
        );

        when(communityCenterService.addCommunityCenter(any(CommunityCenterDTO.class))).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.addCommunityCenter(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(result.error, response.getBody());
    }

    @Test
    void testUpdateOccupancySuccess() {
        // Arrange
        String centerId = "center1";
        OccupancyUpdateDTO occupancyUpdateDTO = new OccupancyUpdateDTO();
        CommunityCenterResponseDTO responseDTO = new CommunityCenterResponseDTO();
        Result<CommunityCenterResponseDTO> result = Result.success(responseDTO);

        when(communityCenterService.updateOccupancy(any(String.class), any(OccupancyUpdateDTO.class))).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.updateOccupancy(centerId, occupancyUpdateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(communityCenterService, times(1)).updateOccupancy(centerId, occupancyUpdateDTO);
    }

    @Test
    void testUpdateOccupancyError() {
        // Arrange
        String centerId = "center1";
        OccupancyUpdateDTO occupancyUpdateDTO = new OccupancyUpdateDTO();
        Result<CommunityCenterResponseDTO> result = Result.error(
                new ErrorApplication(
                        "CommunityService > updateOccupancy",
                        "Error updating occupancy",
                        400,
                        Collections.emptyList()
                )
        );

        when(communityCenterService.updateOccupancy(any(String.class), any(OccupancyUpdateDTO.class))).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.updateOccupancy(centerId, occupancyUpdateDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(result.error, response.getBody());
    }

    @Test
    void testExchangeSuccess() {
        // Arrange
        ResourceExchangeDTO dto = new ResourceExchangeDTO();
        ResourceExchange resourceExchange = new ResourceExchange();
        Result<ResourceExchange> result = Result.success(resourceExchange);

        when(communityCenterService.exchangeResources(any(ResourceExchangeDTO.class))).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.exchange(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resourceExchange, response.getBody());
        verify(communityCenterService, times(1)).exchangeResources(dto);
    }

    @Test
    void testExchangeError() {
        // Arrange
        ResourceExchangeDTO dto = new ResourceExchangeDTO();
        Result<ResourceExchange> result = Result.error(
                new ErrorApplication(
                        "CommunityService > exchangeResources",
                        "Error exchanging resources",
                        400,
                        Collections.emptyList()
                )
        );

        when(communityCenterService.exchangeResources(any(ResourceExchangeDTO.class))).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.exchange(dto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(result.error, response.getBody());
    }

    @Test
    void testGetHighOccupancySuccess() {
        // Arrange
        int page = 1;
        int size = 10;
        List<CommunityCenter> centers = Collections.singletonList(new CommunityCenter());
        Result<List<CommunityCenter>> result = Result.success(centers);

        when(communityCenterService.getCentersWithHighOccupancy(anyInt(), anyInt())).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.getHighOccupancy(page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(centers, response.getBody());
        verify(communityCenterService, times(1)).getCentersWithHighOccupancy(page, size);
    }

    @Test
    void testGetHighOccupancyError() {
        // Arrange
        int page = 1;
        int size = 10;
        Result<List<CommunityCenter>> result = Result.error(
                new ErrorApplication(
                        "CommunityService > getCentersWithHighOccupancy",
                        "Error retrieving high occupancy centers",
                        400,
                        Collections.emptyList()
                )
        );

        when(communityCenterService.getCentersWithHighOccupancy(anyInt(), anyInt())).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.getHighOccupancy(page, size);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(result.error, response.getBody());
    }

    @Test
    void testGetAverageResourcesSuccess() {
        // Arrange
        Map<ResourceType, Double> averageResources = Map.of(ResourceType.DOCTOR, 5.0);
        Result<Map<ResourceType, Double>> result = Result.success(averageResources);

        when(communityCenterService.getAverageResources()).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.getAverageResources();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(averageResources, response.getBody());
        verify(communityCenterService, times(1)).getAverageResources();
    }

    @Test
    void testGetAverageResourcesError() {
        // Arrange
        Result<Map<ResourceType, Double>> result = Result.error(
                new ErrorApplication(
                        "CommunityService > getAverageResources",
                        "Error retrieving average resources",
                        400,
                        Collections.emptyList()
                )
        );

        when(communityCenterService.getAverageResources()).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.getAverageResources();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(result.error, response.getBody());
    }

    @Test
    void testGetExchangeHistorySuccess() {
        // Arrange
        String centerId = "center1";
        int page = 1;
        int size = 10;
        Date date = new Date();
        List<ResourceExchange> exchanges = Collections.singletonList(new ResourceExchange());
        Result<List<ResourceExchange>> result = Result.success(exchanges);

        when(communityCenterService.getExchangeHistory(any(String.class), anyInt(), anyInt(), any(Date.class))).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.getExchangeHistory(centerId, page, size, date);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exchanges, response.getBody());
        verify(communityCenterService, times(1)).getExchangeHistory(centerId, page, size, date);
    }

    @Test
    void testGetExchangeHistoryError() {
        // Arrange
        String centerId = "center1";
        int page = 1;
        int size = 10;
        Date date = new Date();
        Result<List<ResourceExchange>> result = Result.error(
                new ErrorApplication(
                        "CommunityService > getExchangeHistory",
                        "Error retrieving exchange history",
                        400,
                        Collections.emptyList()
                )
        );

        when(communityCenterService.getExchangeHistory(any(String.class), anyInt(), anyInt(), any(Date.class))).thenReturn(result);

        // Act
        ResponseEntity<Object> response = communityCenterController.getExchangeHistory(centerId, page, size, date);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(result.error, response.getBody());
    }
}
