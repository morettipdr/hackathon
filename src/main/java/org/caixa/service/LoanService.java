package org.caixa.service;

import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import org.caixa.model.dto.LoanRequestDTO;
import org.caixa.model.dto.LoanResponseDTO;

@ApplicationScoped
public class LoanService {

    public LoanResponseDTO simulateLoan(LoanRequestDTO request) {
        throw new UnauthorizedException("not authorized to simulate loan");
    }
}
