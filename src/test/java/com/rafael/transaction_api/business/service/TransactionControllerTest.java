package com.rafael.transaction_api.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rafael.transaction_api.business.services.StatisticsService;
import com.rafael.transaction_api.business.services.TransactionService;
import com.rafael.transaction_api.controller.StatisticsController;
import com.rafael.transaction_api.controller.TransactionController;
import com.rafael.transaction_api.controller.dtos.StatisticsResponseDTO;
import com.rafael.transaction_api.controller.dtos.TransactionRequestDTO;
import com.rafael.transaction_api.infrastructure.exceptions.UnprocessableEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    TransactionController transactionController;

    @Mock
    TransactionService transactionService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    TransactionRequestDTO transaction;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        transaction = new TransactionRequestDTO(20.0, OffsetDateTime.of(2025,
                2, 18, 14, 30, 0, 0, ZoneOffset.UTC));
    }

    @Test
    void mustAddTransactionSuccessfully() throws Exception {

        doNothing().when(transactionService).addTransactions(transaction);

        mockMvc.perform(post("/transaction")
                .content(objectMapper.writeValueAsString(transaction))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    void mustGenerateExceptionWhenAddingTransaction() throws Exception {
        doThrow(new UnprocessableEntity("Requisition error")).when(transactionService).addTransactions(transaction);

        mockMvc.perform(post("/transaction")
                        .content(objectMapper.writeValueAsString(transaction))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldDeleteTransactionsSuccessfully() throws Exception {
        doNothing().when(transactionService).clearTransactions();
        mockMvc.perform(delete("/transaction"))
                .andExpect(status().isOk());
    }

}
