package com.rafael.transaction_api.business.service;

import com.rafael.transaction_api.business.services.StatisticsService;
import com.rafael.transaction_api.business.services.TransactionService;
import com.rafael.transaction_api.controller.dtos.StatisticsResponseDTO;
import com.rafael.transaction_api.controller.dtos.TransactionRequestDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @InjectMocks
    StatisticsService statisticsService;

    @Mock
    TransactionService transactionService;

    TransactionRequestDTO transaction;

    StatisticsResponseDTO statistics;

    @BeforeEach
    void setUp() {
        transaction = new TransactionRequestDTO(20.0, OffsetDateTime.now());
        statistics = new StatisticsResponseDTO(1L, 20.0, 20.0, 20.0, 20.0);
    }

    @Test
    void calculateStatisticsWithSuccess() {
        when(transactionService.searchTransactions(60))
                .thenReturn(Collections.singletonList(transaction));

        StatisticsResponseDTO result = statisticsService.calculateTransactionsStatistics(60);

        verify(transactionService, times(1)).searchTransactions(60);
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(statistics);
    }

    @Test
    void calculateStatisticsWhenListIsEmpty() {

        StatisticsResponseDTO expectedStatistic =
                new StatisticsResponseDTO(0L, 0.0, 0.0, 0.0, 0.0);
        when(transactionService.searchTransactions(60))
                .thenReturn(Collections.emptyList());

        StatisticsResponseDTO result = statisticsService.calculateTransactionsStatistics(60);

        verify(transactionService, times(1)).searchTransactions(60);
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expectedStatistic);
    }

}
