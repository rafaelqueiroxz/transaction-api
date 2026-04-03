package com.rafael.transaction_api.business.service;

import com.rafael.transaction_api.business.services.StatisticsService;
import com.rafael.transaction_api.business.services.TransactionService;
import com.rafael.transaction_api.controller.dtos.StatisticsResponseDTO;
import com.rafael.transaction_api.controller.dtos.TransactionRequestDTO;
import com.rafael.transaction_api.infrastructure.exceptions.UnprocessableEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    TransactionService transactionService;

    TransactionRequestDTO transaction;

    StatisticsResponseDTO statistics;

    @BeforeEach
    void setUp() {
        transaction = new TransactionRequestDTO(20.0, OffsetDateTime.now());
        statistics = new StatisticsResponseDTO(1L, 20.0, 20.0, 20.0, 20.0);
    }

    @Test
    void transactionShouldBeAddedSuccessfully() {

        transactionService.addTransactions(transaction);

        List<TransactionRequestDTO> transactions = transactionService.searchTransactions(5000);

        assertTrue(transactions.contains(transaction));
    }

    @Test
    void anExceptionShouldBeThrownIfTheValueIsNegative() {

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> transactionService.addTransactions(new TransactionRequestDTO(-10.0, OffsetDateTime.now())));

        assertEquals("The value cannot be less than zero.", exception.getMessage());

    }

    @Test
    void anExceptionShouldBeThrownIfTheDateAndTimeAreLaterThanTheCurrentDate() {

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> transactionService.addTransactions
                        (new TransactionRequestDTO(10.0, OffsetDateTime.now().plusDays(1))));

        assertEquals("Date and time are later than the current date and time.", exception.getMessage());
    }

    @Test
    void transactionShouldBeCleanSuccessfully() {

        transactionService.clearTransactions();

        List<TransactionRequestDTO> transactions = transactionService.searchTransactions(5000);

        assertTrue(transactions.isEmpty());
    }

    @Test
    void mustSearchTransactionsWithinTheInterval() {

        TransactionRequestDTO dto = new TransactionRequestDTO(10.0, OffsetDateTime.now().minusHours(1));

        transactionService.addTransactions(transaction);

        transactionService.addTransactions(dto);

        List<TransactionRequestDTO> transactions = transactionService.searchTransactions(60);

        assertTrue(transactions.contains(transaction));
        assertFalse(transactions.contains(dto));

    }
}
