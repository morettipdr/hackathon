package org.caixa.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.caixa.model.dto.loan.LoanRequestDTO;
import org.caixa.model.dto.loan.LoanResponseDTO;
import org.caixa.model.dto.loan.LoanSummaryDTO;

@ApplicationScoped
public class LoanService {

    public LoanResponseDTO simulateLoan(LoanRequestDTO request) {
        Log.info("Starting loan simulation with request: " + request);
        return new LoanResponseDTO();
    }

    public LoanSummaryDTO getAllLoans() {
        Log.info("Fetching all loans");
        return new LoanSummaryDTO();
    }
}
