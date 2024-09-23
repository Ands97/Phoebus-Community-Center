package com.community_center.community_center.application.service;

import com.community_center.community_center.application.data_mapper.CommunityCenterMapper;
import com.community_center.community_center.application.data_mapper.ResourceMapper;
import com.community_center.community_center.application.dto.*;
import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.entity.Resource;
import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.domain.entity.ResourceType;
import com.community_center.community_center.domain.repository.CommunityCenterRepository;
import com.community_center.community_center.domain.repository.ResourceExchangeRepository;
import com.community_center.community_center.utils.Result;
import com.community_center.community_center.utils.error.ErrorApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CommunityCenterServiceImpl implements CommunityCenterService {
    @Autowired
    private CommunityCenterRepository communityCenterRepository;

    @Autowired
    private ResourceExchangeRepository resourceExchangeRepository;

    public Result<CommunityCenter> addCommunityCenter(CommunityCenterDTO dto) {
        try {
            CommunityCenter center = CommunityCenterMapper.dtoToEntity(dto);
            center.setOccupancyPercentage(calculateOccupancyPercentage(center));
            return communityCenterRepository.save(center);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterService > addCommunityCenter",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }

    private double calculateOccupancyPercentage(CommunityCenter center) {
        if (center.getMaxCapacity() == 0) {
            return 0;
        }

        double percentage = (double) center.getCurrentOccupancy() / center.getMaxCapacity() * 100;

        BigDecimal roundedPercentage = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP);

        return roundedPercentage.doubleValue();
    }

    public Result<CommunityCenterResponseDTO> updateOccupancy (String centerId, OccupancyUpdateDTO dto)
    {
        Result<CommunityCenter> centerResult = communityCenterRepository.findById(centerId);

        if (!centerResult.success) {
            return Result.error(centerResult.error);
        }

        CommunityCenter center = centerResult.response;

        if (dto.getCurrentOccupancy() > center.getMaxCapacity()) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterService > updateOccupancy",
                            "Current occupancy cannot be greater than max capacity. Max campacity: " + center.getMaxCapacity(),
                            400,
                            Collections.emptyList()
                    )
            );
        }

        center.setCurrentOccupancy(dto.getCurrentOccupancy());
        center.setOccupancyPercentage(calculateOccupancyPercentage(center));

        Result<CommunityCenter> updatedCenterResult = communityCenterRepository.save(center);

        if (!updatedCenterResult.success) {
            return Result.error(updatedCenterResult.error);
        }

        CommunityCenter updatedCenter = updatedCenterResult.response;
        boolean hasReachedCapacity = (double) updatedCenter.getCurrentOccupancy() >= updatedCenter.getMaxCapacity();
        CommunityCenterResponseDTO communityCenterResponseDTO = CommunityCenterMapper.toReponseDTO(updatedCenter, hasReachedCapacity);

        return Result.success(communityCenterResponseDTO);
    }

    @Transactional
    public Result<ResourceExchange> exchangeResources(ResourceExchangeDTO dto) {
        if (dto.getFromCenterId() == dto.getToCenterId()) {
                return  Result.error(
                        new ErrorApplication(
                                "CommunityCenterService > exchangeResources",
                                "Cannot exchange resources between the same center",
                                400,
                                Collections.emptyList()
                        )
                );
        }

        CompletableFuture<Result<CommunityCenter>> fromCenterFuture = CompletableFuture.supplyAsync(
                () -> communityCenterRepository.findById(dto.getFromCenterId())
        );

        CompletableFuture<Result<CommunityCenter>> toCenterFuture = CompletableFuture.supplyAsync(
                () -> communityCenterRepository.findById(dto.getToCenterId())
        );

        try {
            CompletableFuture.allOf(fromCenterFuture, toCenterFuture).join();

            Result<CommunityCenter> fromCenterResult = fromCenterFuture.get();
            Result<CommunityCenter> toCenterResult = toCenterFuture.get();

            if (!fromCenterResult.success) {
                return Result.error(fromCenterResult.error);
            }

            if (!toCenterResult.success) {
                return Result.error(toCenterResult.error);
            }

            CommunityCenter fromCenter = fromCenterResult.response;
            CommunityCenter toCenter = toCenterResult.response;

            int fromPoints = this.calculatePoints(dto.getResourcesSent());
            int toPoints = this.calculatePoints(dto.getResourcesReceived());

            if (!canExchange(fromPoints, toPoints, fromCenter, toCenter)) {
                return Result.error(
                        new ErrorApplication(
                                "CommunityCenterService > exchangeResources",
                                "Exchange not possible",
                                400,
                                Collections.emptyList()
                        )
                );
            }

            boolean fromCenterValid = validateSufficientResources(fromCenter, dto.getResourcesSent());
            boolean toCenterValid = validateSufficientResources(toCenter, dto.getResourcesReceived());

            if (!fromCenterValid || !toCenterValid) {
                return Result.error(
                        new ErrorApplication(
                                "CommunityCenterService > exchangeResources",
                                "Insufficient resources to exchange",
                                400,
                                Collections.emptyList()
                        )
                );
            }

            CompletableFuture<Void> fromUpdateFuture = CompletableFuture.runAsync(() -> {
                updateResource(fromCenter, dto.getResourcesSent(), false);
                updateResource(fromCenter, dto.getResourcesReceived(), true);
            });

            CompletableFuture<Void> toUpdateFuture = CompletableFuture.runAsync(() -> {
                updateResource(toCenter, dto.getResourcesReceived(), false);
                updateResource(toCenter, dto.getResourcesSent(), true);
            });

            CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(fromUpdateFuture, toUpdateFuture);
            combinedFuture.join();

            ResourceExchange exchange = createExchange(dto);
            return Result.success(exchange);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterService > exchangeResources",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }

    private boolean canExchange (
            int fromPoints,
            int toPoints,
            CommunityCenter fromCenter,
            CommunityCenter toCenter
    ) {
        double maxCapacity = 0.9;

        boolean fromCenterReached = (double) fromCenter.getCurrentOccupancy() / fromCenter.getMaxCapacity() > maxCapacity;
        boolean toCenterReached = (double) toCenter.getCurrentOccupancy() / toCenter.getMaxCapacity() > maxCapacity;

        if (!fromCenterReached && !toCenterReached) {
            return fromPoints == toPoints;
        }

        if (fromCenterReached && toCenterReached) {
            return true;
        }

        if (fromCenterReached) {
            return fromPoints <= toPoints;
        }

        return fromPoints >= toPoints;
    }

    private int calculatePoints (List<ResourceDTO> resources) {
        return resources.stream()
                .mapToInt(resource -> resource.getType().getPoints() * resource.getQuantity())
                .sum();
    }

    private boolean validateSufficientResources(CommunityCenter center, List<ResourceDTO> resources) {
        Map<ResourceType, Integer> exchangeMap = resources.stream()
                .collect(Collectors.toMap(ResourceDTO::getType, ResourceDTO::getQuantity));

        for (Resource resource : center.getResources()) {
            if (exchangeMap.containsKey(resource.getType())) {
                int quantity = exchangeMap.get(resource.getType());
                if (resource.getQuantity() < quantity) {
                    return false;
                }
                exchangeMap.remove(resource.getType());
            }
        }

        return exchangeMap.isEmpty();
    }

    private void updateResource(CommunityCenter center, List<ResourceDTO> resources, boolean isReceiving) {
        Map<ResourceType, Integer> exchangeMap = resources.stream()
                .collect(Collectors.toMap(ResourceDTO::getType, ResourceDTO::getQuantity));

        for (Resource resource : center.getResources()) {
            if (exchangeMap.containsKey(resource.getType())) {
                int quantity = exchangeMap.get(resource.getType());
                if (isReceiving) {
                    resource.setQuantity(resource.getQuantity() + quantity);
                } else {
                    resource.setQuantity(resource.getQuantity() - quantity);
                }
                exchangeMap.remove(resource.getType());
            }
        }

        if (isReceiving) {
            for (Map.Entry<ResourceType, Integer> entry : exchangeMap.entrySet()) {
                Resource newResource = new Resource();
                newResource.setType(entry.getKey());
                newResource.setQuantity(entry.getValue());
                center.getResources().add(newResource);
            }
        }

        try {
            communityCenterRepository.save(center);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update resources in the database", e);
        }
    }

    private ResourceExchange createExchange(ResourceExchangeDTO dto) {
        ResourceExchange exchange = new ResourceExchange();
        exchange.setFromCenterId(dto.getFromCenterId());
        exchange.setToCenterId(dto.getToCenterId());
        exchange.setResourcesSent(ResourceMapper.dtoToEntity(dto.getResourcesSent()));
        exchange.setResourcesReceived(ResourceMapper.dtoToEntity(dto.getResourcesReceived()));
        Result<ResourceExchange> result = resourceExchangeRepository.save(exchange);

        if  (!result.success) {
            throw new RuntimeException("Failed to save exchange in the database");
        }

        return result.response;
    }

    public Result<List<CommunityCenter>> getCentersWithHighOccupancy(int page, int size) {
        try {
            return communityCenterRepository.listCommunityCentersByOccupancyAtLeast(90.0, page, size);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterService > getCentersWithHighOccupancy",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }

    public Result<Map<ResourceType, Double>> getAverageResources() {
        try {
            Result<List<CommunityCenter>> allCentersResult = communityCenterRepository.findAll();

            if (!allCentersResult.success) {
                return Result.error(allCentersResult.error);
            }

            List<CommunityCenter> allCenters = allCentersResult.response;

            Map<ResourceType, List<Integer>> resourceQuantities = new HashMap<>();

            allCenters.stream()
                    .flatMap(center -> center.getResources().stream())
                    .forEach(resource -> resourceQuantities
                            .computeIfAbsent(resource.getType(), k -> new ArrayList<>())
                            .add(resource.getQuantity())
                    );

            Map<ResourceType, Double> averageResourceQuantities = Arrays.stream(ResourceType.values())
                    .collect(Collectors.toMap(
                            type -> type,
                            type -> {
                                List<Integer> quantities = resourceQuantities.getOrDefault(type, List.of());
                                return quantities.isEmpty() ? 0.0 : quantities.stream()
                                        .mapToInt(Integer::intValue)
                                        .sum() / (double) allCenters.size();
                            }
                    ));

            return Result.success(averageResourceQuantities);
        } catch (Exception e) {
            return Result.error(
                    new ErrorApplication(
                            "CommunityCenterService > getAverageResources",
                            e.getMessage(),
                            500,
                            Collections.emptyList()
                    )
            );
        }
    }

    public Result<List<ResourceExchange>> getExchangeHistory(String centerId, int page, int size, Date date) {
        return date == null
                ? resourceExchangeRepository.listByFromCenterId(centerId, page, size)
                : resourceExchangeRepository.listByFromCenterIdAndDate(centerId, page, size, date);
    }

}
