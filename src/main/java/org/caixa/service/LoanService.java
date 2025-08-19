package org.caixa.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.caixa.model.dto.loan.LoanRequestDTO;
import org.caixa.model.dto.loan.LoanResponseDTO;
import org.caixa.model.dto.loan.LoanSummaryDTO;
import org.caixa.model.entity.Product;
import org.caixa.repository.ProductRepository;

@ApplicationScoped
public class LoanService {

    @Inject
    ProductRepository productRepository;

    public LoanResponseDTO simulateLoan(LoanRequestDTO request) {
        Log.info("Starting loan simulation with request: " + request);
        Product product = productRepository.findByValue(request.getValorDesejado());
        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();
        loanResponseDTO.setCodigoProduto(product.getCodigo());
        return loanResponseDTO;
    }

    public LoanSummaryDTO getAllLoans() {
        Log.info("Fetching all loans");
        return new LoanSummaryDTO();
    }
}
