/*
    Author: Rafael A. Queiroz
    Date: 10/03/2026
 */

package com.rafael.transaction_api.controller;

import com.rafael.transaction_api.business.services.TransactionService;
import com.rafael.transaction_api.controller.dtos.TransactionRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController // The @RestController annotation tells Spring that the
// class will return the data directly in the response body (usually as JSON).
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    // Methods that map API URLs to POST or DELETE methods within the code.
    // They also receive the DTO sent in the request body and prepare it for
    // processing. They don't have complex business rules; they simply call
    // the Service class (where the actual logic happens). After processing,
    // the Controller decides what to send back to the client, such as a
    // success code (200 OK), an error (400 Bad Request), or the requested
    // data in JSON format.
    @PostMapping
    @Operation(description = "Endpoint responsible for adding transactions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction successfully recorded."),
            @ApiResponse(responseCode = "422", description = "The fields don't meet the transaction requirements."),
            @ApiResponse(responseCode = "400", description = "Requisition error."),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Void> addTransaction(@RequestBody TransactionRequestDTO dto) {
        transactionService.addTransactions(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    @Operation(description = "Endpoint responsible for deleting transactions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully deleted."),
            @ApiResponse(responseCode = "400", description = "Requisition error."),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    public ResponseEntity<Void> deleteTransaction() {
        transactionService.clearTransactions();
        return ResponseEntity.ok().build();
    }
}
