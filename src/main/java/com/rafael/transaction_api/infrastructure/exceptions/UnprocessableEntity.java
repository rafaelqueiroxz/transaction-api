/*
    Author: Rafael A. Queiroz
    Date: 09/03/2026
 */

package com.rafael.transaction_api.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// This exception was created to extend RunTimeException and provide
// a custom exception for the project.
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntity extends RuntimeException {
    public UnprocessableEntity(String message) {
        super(message);
    }
}
