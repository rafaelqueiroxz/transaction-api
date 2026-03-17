/*
    Author: Rafael A. Queiroz
    Date: 10/03/2026
 */

package com.rafael.transaction_api.controller.dtos;

// Class used to return statistics on in-memory transactions in the system.
public record StatisticsResponseDTO(Long count,
                                    Double sum,
                                    Double avg,
                                    Double min,
                                    Double max) {
    // The record type was added in Java to make it easier to create
    // classes that serve only to load data (such as DTOs).
}
