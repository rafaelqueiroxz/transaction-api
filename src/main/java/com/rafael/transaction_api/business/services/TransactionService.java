/*
    Author: Rafael A. Queiroz
    Date: 09/03/2026
 */

package com.rafael.transaction_api.business.services;


import com.rafael.transaction_api.controller.dtos.TransactionRequestDTO;
import com.rafael.transaction_api.infrastructure.exceptions.UnprocessableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // log library for recording events within the software
public class TransactionService {

    //The challenge states that we shouldn't use a database to store
    // this data. Considering the simplicity of the request and the
    // ease of handling this structure, I will use an array list to
    // store the transaction information.

    // In a larger project, the use of a database is indispensable.

    private final List<TransactionRequestDTO> transactionsList = new ArrayList<>();

    // Method that adds transactions
    public void addTransactions(TransactionRequestDTO dto) {

        // Logs are records of events in the system that help to understand what is
        // happening behind the scenes.
        log.info("Transaction " + dto + " recording process initiated.");

        // Exception handling
        if (dto.dateTime().isAfter(OffsetDateTime.now())) {
            log.error("Date and time are later than the current date and time.");
            throw new UnprocessableEntity("Date and time are later than the current date and time.");
        }

        // Exception handling
        if (dto.value() < 0) {
            log.error("The value cannot be less than zero.");
            throw new UnprocessableEntity("The value cannot be less than zero.");
        }

        // Adds a transaction to the ArrayList with the parameters passed by the dto.
        transactionsList.add(dto);
        log.info("Transactions added successfully.");

    }

    // A method that clears transactions from the system's cache memory.
    public void clearTransactions() {
        log.info("The process to delete transactions has begun.");
        transactionsList.clear();
        log.info("Transactions successfully deleted.");
    }

    // A method that searches for transactions according
    // to a time interval (search range = OffSetDateTime.now() - seconds(rangeSearch) ).
    public List<TransactionRequestDTO> searchTransactions(Integer rangeSearch) {

        log.info("Searching for transactions with range " + rangeSearch);
        OffsetDateTime rangeDateTime = OffsetDateTime.now().minusSeconds(rangeSearch);

        // Defines the date and time that will serve as a filter for the
        // Java Stream, which will return all transactions after that date.
        log.info("Transactions returned successfully.");
        return transactionsList.stream() // Retrieve the list of transactions in memory.
                // It filters each transaction and checks if the date and time of
                // those transactions is later than the date and time of the rangeDateTime.
                .filter(transaction -> transaction.dateTime()
                .isAfter(rangeDateTime)).toList(); // If it's later, add it to the list.


    }
}
