/*
    Author: Rafael A. Queiroz
    Date: 10/03/2026
 */

package com.rafael.transaction_api.business.services;

import com.rafael.transaction_api.controller.dtos.StatisticsResponseDTO;
import com.rafael.transaction_api.controller.dtos.TransactionRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    public final TransactionService transactionService;


    public StatisticsResponseDTO calculateTransactionsStatistics(Integer rangeSearch) {

        // A method that retrieves transactions from the transactionServices
        // and stores the data in a list.
        log.info("The search for transaction statistics for the time period of " + rangeSearch + " has begun.");
        List<TransactionRequestDTO> transactions = transactionService.searchTransactions(rangeSearch);

        if (transactions.isEmpty()) {
            return new StatisticsResponseDTO(0L, 0.0, 0.0, 0.0, 0.0);
        }

        // It retrieves each of the values from transactions.stream() and
        // maps them to Double. Then it calculates and converts the values
        // to DoubleSummaryStatistics.
        DoubleSummaryStatistics transactionsStatistics = transactions.stream()
                .mapToDouble(TransactionRequestDTO::value).summaryStatistics();

        // Returns all transaction statistics calculated by summaryStatistics.
        log.info("Statistics returned successfully.");
        return new StatisticsResponseDTO(transactionsStatistics.getCount(),
                transactionsStatistics.getSum(),
                transactionsStatistics.getAverage(),
                transactionsStatistics.getMin(),
                transactionsStatistics.getMax());
    }

}
