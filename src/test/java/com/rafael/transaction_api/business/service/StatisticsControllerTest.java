package com.rafael.transaction_api.business.service;

import com.rafael.transaction_api.business.services.StatisticsService;
import com.rafael.transaction_api.controller.StatisticsController;
import com.rafael.transaction_api.controller.dtos.StatisticsResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsControllerTest {

    @InjectMocks
    StatisticsController statisticsController;

    @Mock
    StatisticsService statisticsService;

    MockMvc mockMvc;

    StatisticsResponseDTO statistics;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
        statistics = new StatisticsResponseDTO(1L, 20.0, 20.0, 20.0, 20.0);
    }

    @Test
    void shouldSuccessfullyRetrieveStatistics() throws Exception {

        when(statisticsService.calculateTransactionsStatistics(60)).thenReturn(statistics);

        mockMvc.perform(get("/statistics")
                .param("rangeSearch", "60")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(statistics.count()));

    }

}
