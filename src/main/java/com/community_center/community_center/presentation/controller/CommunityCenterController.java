package com.community_center.community_center.presentation.controller;

import com.community_center.community_center.application.dto.*;
import com.community_center.community_center.application.service.CommunityCenterService;
import com.community_center.community_center.domain.entity.CommunityCenter;
import com.community_center.community_center.domain.entity.ResourceExchange;
import com.community_center.community_center.domain.entity.ResourceType;
import com.community_center.community_center.utils.Result;
import com.community_center.community_center.utils.error.ErrorApplication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/community-centers")
public class CommunityCenterController {

    @Autowired
    private CommunityCenterService communityCenterService;

    @PostMapping
    @Operation(summary = "Add a new community center", description = "Creates a new community center.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Community center created successfully",
                    content = @Content(schema = @Schema(implementation = CommunityCenter.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class)))
    })
    public ResponseEntity<Object> addCommunityCenter(@RequestBody CommunityCenterDTO dto) {
        Result<CommunityCenter> result = communityCenterService.addCommunityCenter(dto);

        if (!result.success) {
            return ResponseEntity
                    .status(result.error.getCode())
                    .body(result.error);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.response);
    }

    @PutMapping("/{centerId}/occupancy")
    @Operation(summary = "Update occupancy of a community center", description = "Updates the occupancy of the specified community center.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Occupancy updated successfully",
                    content = @Content(schema = @Schema(implementation = CommunityCenterResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Community center not found",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class)))
    })
    public ResponseEntity<Object> updateOccupancy(
            @PathVariable String centerId,
            @RequestBody OccupancyUpdateDTO currentOccupancy
    ) {
        Result<CommunityCenterResponseDTO> result = communityCenterService.updateOccupancy(centerId, currentOccupancy);

        if (!result.success) {
            return ResponseEntity
                    .status(result.error.getCode())
                    .body(result.error);
        }

        return ResponseEntity.ok(result.response);
    }

    @PostMapping("/exchange")
    @Operation(summary = "Exchange resources", description = "Exchanges resources between community centers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources exchanged successfully",
                    content = @Content(schema = @Schema(implementation = ResourceExchange.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class))),
            @ApiResponse(responseCode = "404", description = "Community center not found",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class)))
    })
    public ResponseEntity<Object> exchange(@RequestBody ResourceExchangeDTO dto) {
        Result<ResourceExchange> result = communityCenterService.exchangeResources(dto);

        if (!result.success) {
            return ResponseEntity
                    .status(result.error.getCode())
                    .body(result.error);
        }

        return ResponseEntity.ok(result.response);
    }

    @GetMapping("/high-occupancy")
    @Operation(summary = "Get community centers with high occupancy", description = "Retrieves a list of community centers with high occupancy.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of community centers retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CommunityCenter.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class)))
    })
    public ResponseEntity<Object> getHighOccupancy(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Result<List<CommunityCenter>> result = communityCenterService.getCentersWithHighOccupancy(page, size);

        if (!result.success) {
            return ResponseEntity
                    .status(result.error.getCode())
                    .body(result.error);
        }

        return ResponseEntity.ok(result.response);
    }

    @GetMapping("/average-resources")
    @Operation(summary = "Get average resources", description = "Retrieves average resources for community centers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Average resources retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AverageResourcesResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class)))
    })
    public ResponseEntity<Object> getAverageResources() {
        Result<Map<ResourceType, Double>> result = communityCenterService.getAverageResources();

        if (!result.success) {
            return ResponseEntity
                    .status(result.error.getCode())
                    .body(result.error);
        }

        return ResponseEntity.ok(result.response);
    }

    @GetMapping("/{centerId}/exchange-history")
    @Operation(summary = "Get exchange history", description = "Retrieves the exchange history for a specific community center.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchange history retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ResourceExchange.class)))),
            @ApiResponse(responseCode = "404", description = "Community center not found",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorApplication.class)))
    })
    public ResponseEntity<Object> getExchangeHistory(
            @PathVariable String centerId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(
                    description = "Data de referência para a operação no formato ISO 8601 (yyyy-MM-dd'T'HH:mm:ss.SSSZ)",
                    example = "2024-09-23T15:30:00Z"
            )
            Date date
    ) {
        Result<List<ResourceExchange>> result = communityCenterService.getExchangeHistory(centerId, page, size, date);

        if (!result.success) {
            return ResponseEntity
                    .status(result.error.getCode())
                    .body(result.error);
        }

        return ResponseEntity.ok(result.response);
    }
}
