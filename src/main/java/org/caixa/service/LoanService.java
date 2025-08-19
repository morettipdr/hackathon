package org.caixa.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.caixa.model.dto.LoanRequestDTO;
import org.caixa.model.dto.LoanResponseDTO;

@ApplicationScoped
public class LoanService {

    public LoanResponseDTO simulateLoan(LoanRequestDTO request) {
        Log.info("Starting loan simulation with request: " + request);
        return new LoanResponseDTO();
    }
}
