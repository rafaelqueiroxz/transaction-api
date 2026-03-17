/*
    Author: Rafael A. Queiroz
    Date: 09/03/2026
 */

package com.rafael.transaction_api.controller.dtos;

import java.time.OffsetDateTime;

// Data Transfer Object, a class used to transfer transaction data within the system.
public record TransactionRequestDTO(Double value, OffsetDateTime dateTime) {
    // The record type was added in Java to make it easier to create
    // classes that serve only to load data (such as DTOs).
}
