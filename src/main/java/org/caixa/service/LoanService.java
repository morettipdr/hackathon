package org.caixa.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.caixa.mapper.LoanSimulationMapper;
import org.caixa.model.dto.loan.*;
import org.caixa.model.entity.LoanSimulation;
import org.caixa.model.entity.Product;
import org.caixa.repository.ProductRepository;
import org.caixa.repository.LoanSimulationRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LoanService {

    @Inject
    ProductRepository productRepository;
    
    @Inject
    LoanSimulationRepository loanSimulationRepository;

    @Inject
    LoanSimulationMapper loanSimulationMapper;

    public LoanResponseDTO simulateLoan(LoanRequestDTO request) {
        Log.info("Iniciando simulacao de empréstimo: " + request);
        Product product = productRepository.findByValue(request.getDesiredAmount());
        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();
        loanResponseDTO.setCodigoProduto(product.getId());
        loanResponseDTO.setTaxaJuros(product.getTaxaJuros().setScale(4));
        loanResponseDTO.setResultadoSimulacao(getLoanResults(request, product));
        return loanResponseDTO;
    }

    public LoanSummaryDTO getAllLoans() {
        Log.info("Buscando todos os empréstimos");
        return new LoanSummaryDTO();
    }

    private List<LoanResultDTO> getLoanResults(LoanRequestDTO request, Product product) {
        Log.info("Calculando resultados do empréstimo para: " + request);
        BigDecimal value = request.getDesiredAmount();
        Integer term = request.getTerm();
        BigDecimal interestRate = product.getTaxaJuros();

        List<LoanResultDTO> results = List.of(
                new LoanResultDTO("SAC", sacCalculator(value, interestRate, term)),
                new LoanResultDTO("PRICE", priceCalculator(value, interestRate, term))
        );

        Log.info("Resultados calculados: " + results);
        return results;
    }

    private List<InstallmentDTO> sacCalculator(BigDecimal value, BigDecimal interestRate, Integer time) {
        Log.info("Calculando parcelas SAC para value: " + value + ", taxa de juros: " + interestRate + ", time: " + time);

        BigDecimal amortization = value.divide(BigDecimal.valueOf(time));
        BigDecimal totalInstallmentValue = BigDecimal.ZERO;

        List<InstallmentDTO> installments = new ArrayList<>();
        for (int i = 1; i <= time; i++) {
            BigDecimal interest = value.multiply(interestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal installmentValue = amortization.add(interest).setScale(2, RoundingMode.HALF_UP);
            installments.add(new InstallmentDTO(i, amortization, interest, installmentValue));
            value = value.subtract(amortization);
            totalInstallmentValue = totalInstallmentValue.add(installmentValue);
        }

        LoanSimulation loanSimulation = new LoanSimulation();
        loanSimulation.setValorDesejado(value);
        loanSimulation.setPrazo(time);
        loanSimulation.setValorTotalParcelas(totalInstallmentValue);
        loanSimulationRepository.persist(loanSimulation);

        return installments;
    }

    private List<InstallmentDTO> priceCalculator(BigDecimal value, BigDecimal interestRate, Integer time) {
        Log.info("Calculando parcela PRICE para value: " + value + ", taxa de juros: " + interestRate + ", time: " + time);

        BigDecimal operationStart = BigDecimal.ONE.add(interestRate).pow(time);
        BigDecimal installmentValue = value.multiply((operationStart.multiply(interestRate)).divide(operationStart.subtract(BigDecimal.ONE), RoundingMode.HALF_UP));
        BigDecimal totalInstallmentValue = installmentValue.multiply(BigDecimal.valueOf(time));

        List<InstallmentDTO> installments = new ArrayList<>();
        for (int i = 1; i < time; i++) {
            BigDecimal interest = value.multiply(interestRate);
            BigDecimal amortization = installmentValue.subtract(interest).setScale(2, RoundingMode.HALF_UP);
            installments.add(new InstallmentDTO(i, amortization, interest.setScale(2, RoundingMode.HALF_UP), installmentValue.setScale(2, RoundingMode.HALF_UP)));
            value = value.subtract(amortization);
        }
        
        LoanSimulation loanSimulation = new LoanSimulation();
        loanSimulation.setValorDesejado(value);
        loanSimulation.setPrazo(time);
        loanSimulation.setValorTotalParcelas(totalInstallmentValue);
        loanSimulationRepository.persist(loanSimulation);
        
        return installments;
    }
}
