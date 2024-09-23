package com.community_center.community_center.infra.data.repository;

import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ResourceExchangeRepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private ResourceExchangeRepositoryImpl resourceExchangeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListByFromCenterIdSuccess() {
        String centerId = "center1";
        int page = 1;
        int size = 10;
        ResourceExchange exchange = new ResourceExchange();
        exchange.setFromCenterId(centerId);
        List<ResourceExchange> exchanges = Collections.singletonList(exchange);

        when(mongoTemplate.find(any(Query.class), eq(ResourceExchange.class))).thenReturn(exchanges);

        Result<List<ResourceExchange>> result = resourceExchangeRepository.listByFromCenterId(centerId, page, size);

        assertNotNull(result);
        assertTrue(result.success);
        assertEquals(1, result.response.size());
        assertEquals(centerId, result.response.get(0).getFromCenterId());
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(ResourceExchange.class));
    }

    @Test
    void testListByFromCenterIdError() {
        String centerId = "center1";
        int page = 1;
        int size = 10;

        when(mongoTemplate.find(any(Query.class), eq(ResourceExchange.class))).thenThrow(new RuntimeException("Database error"));

        Result<List<ResourceExchange>> result = resourceExchangeRepository.listByFromCenterId(centerId, page, size);

        assertNotNull(result);
        assertFalse(result.success);
        assertEquals("Database error", result.error.getMessage());
    }

    @Test
    void testListByFromCenterIdAndDateSuccess() {
        String centerId = "center1";
        int page = 1;
        int size = 10;
        Date date = new Date();
        ResourceExchange exchange = new ResourceExchange();
        exchange.setFromCenterId(centerId);
        List<ResourceExchange> exchanges = Collections.singletonList(exchange);

        when(mongoTemplate.find(any(Query.class), eq(ResourceExchange.class))).thenReturn(exchanges);

        Result<List<ResourceExchange>> result = resourceExchangeRepository.listByFromCenterIdAndDate(centerId, page, size, date);

        assertNotNull(result);
        assertTrue(result.success);
        assertEquals(1, result.response.size());
        assertEquals(centerId, result.response.get(0).getFromCenterId());
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(ResourceExchange.class));
    }

    @Test
    void testListByFromCenterIdAndDateError() {
        String centerId = "center1";
        int page = 1;
        int size = 10;
        Date date = new Date();

        when(mongoTemplate.find(any(Query.class), eq(ResourceExchange.class))).thenThrow(new RuntimeException("Database error"));

        Result<List<ResourceExchange>> result = resourceExchangeRepository.listByFromCenterIdAndDate(centerId, page, size, date);

        assertNotNull(result);
        assertFalse(result.success);
        assertEquals("Database error", result.error.getMessage());
    }

    @Test
    void testSaveSuccess() {
        ResourceExchange exchange = new ResourceExchange();
        when(mongoTemplate.save(any(ResourceExchange.class))).thenReturn(exchange);

        Result<ResourceExchange> result = resourceExchangeRepository.save(exchange);

        assertNotNull(result);
        assertTrue(result.success);
        assertEquals(exchange, result.response);
        verify(mongoTemplate, times(1)).save(exchange);
    }

    @Test
    void testSaveError() {
        ResourceExchange exchange = new ResourceExchange();
        when(mongoTemplate.save(any(ResourceExchange.class))).thenThrow(new RuntimeException("Database error"));

        Result<ResourceExchange> result = resourceExchangeRepository.save(exchange);

        assertNotNull(result);
        assertFalse(result.success);
        assertEquals("Database error", result.error.getMessage());
    }
}
