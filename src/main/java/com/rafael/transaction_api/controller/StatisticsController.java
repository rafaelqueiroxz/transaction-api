/*
    Author: Rafael A. Queiroz
    Date: 10/03/2026
 */

package com.rafael.transaction_api.controller;


import com.rafael.transaction_api.business.services.StatisticsService;
import com.rafael.transaction_api.controller.dtos.StatisticsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    public final StatisticsService statisticsService;

    // This method maps API URLs to the GET method within the code.
    // It also receives the DTO sent in the request body and prepares
    // it for processing. It also defines which transactions the GET
    // will retrieve from memory according to the requested range
    // (rangeSearch -> default = 60). It doesn't have complex business
    // rules; it simply calls the Service class (where the actual logic
    // occurs). After processing, the Controller decides what to send back
    // to the client, such as a success code (200 OK), an error (400 Bad
    // Request), or the requested data in JSON format.
    @GetMapping
    @Operation(description = "Endpoint responsible for searching transaction statistics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully."),
            @ApiResponse(responseCode = "400", description = "Error in transaction statistics search."),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<StatisticsResponseDTO> searchStatistics(
            @RequestParam(value = "rangeSearch", required = false, defaultValue = "60") Integer rangeSearch) {

        return ResponseEntity.ok(statisticsService.calculateTransactionsStatistics(rangeSearch));

    }

}
