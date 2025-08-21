package org.caixa.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import net.bytebuddy.asm.Advice;
import org.caixa.mapper.LoanSimulationMapper;
import org.caixa.model.dto.loan.*;
import org.caixa.model.entity.loan.LoanSimulation;
import org.caixa.model.entity.product.Product;
import org.caixa.model.enums.LoanType;
import org.caixa.repository.ProductRepository;
import org.caixa.repository.LoanSimulationRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
        Product product = productRepository.findByValue(request);
        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();
        loanResponseDTO.setCodigoProduto(product.getId());
        loanResponseDTO.setTaxaJuros(product.getTaxaJuros().setScale(4, RoundingMode.HALF_UP));
        loanResponseDTO.setResultadoSimulacao(getLoanResults(request, product));
        return loanResponseDTO;
    }

    private List<LoanResultDTO> getLoanResults(LoanRequestDTO request, Product product) {
        Log.info("Calculando resultados do empréstimo para: " + request);
        BigDecimal value = request.getValorDesejado();
        Integer term = request.getPrazo();

        LoanSimulation sacLoanSimulation = new LoanSimulation();
        LoanSimulation priceLoanSimulation = new LoanSimulation();

        List<LoanResultDTO> results = List.of(
                new LoanResultDTO("SAC", sacCalculator(value, product, term, sacLoanSimulation)),
                new LoanResultDTO("PRICE", priceCalculator(value, product, term, priceLoanSimulation))
        );

        persistLoanSimulation(sacLoanSimulation);
        persistLoanSimulation(priceLoanSimulation);

        Log.info("Resultados calculados: " + results);
        return results;
    }

    @Transactional
    public List<InstallmentDTO> sacCalculator(BigDecimal value, Product product, Integer time, LoanSimulation loanSimulation) {
        BigDecimal interestRate = product.getTaxaJuros();
        Log.info("Calculando parcelas SAC para value: " + value + ", taxa de juros: " + interestRate + ", time: " + time);

        loanSimulation.setValorDesejado(value);
        loanSimulation.setCodigoProduto(product.getId());
        loanSimulation.setTaxaJuro(product.getTaxaJuros());

        BigDecimal amortization = value.divide(BigDecimal.valueOf(time), RoundingMode.HALF_UP);
        BigDecimal totalInstallmentValue = BigDecimal.ZERO;

        List<InstallmentDTO> installments = new ArrayList<>();
        for (int i = 1; i <= time; i++) {
            BigDecimal interest = value.multiply(interestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal installmentValue = amortization.add(interest).setScale(2, RoundingMode.HALF_UP);
            installments.add(new InstallmentDTO(i, amortization, interest, installmentValue));
            value = value.subtract(amortization);
            totalInstallmentValue = totalInstallmentValue.add(installmentValue);
        }

        loanSimulation.setPrazo(time);
        loanSimulation.setValorTotalParcelas(totalInstallmentValue);
        loanSimulation.setDataInclusaoSimulacao(LocalDate.now());
        loanSimulation.setTipoSimulacao(LoanType.SAC.getId());

        return installments;
    }

    @Transactional
    public void persistLoanSimulation(LoanSimulation loanSimulation) {
        Log.info("Persistindo simulação de empréstimo: " + loanSimulation);
        loanSimulationRepository.persist(loanSimulation);
    }

    @Transactional
    public List<InstallmentDTO> priceCalculator(BigDecimal value, Product product, Integer time, LoanSimulation loanSimulation) {
        BigDecimal interestRate = product.getTaxaJuros();
        Log.info("Calculando parcela PRICE para value: " + value + ", taxa de juros: " + interestRate + ", time: " + time);

        loanSimulation.setValorDesejado(value);
        loanSimulation.setCodigoProduto(product.getId());
        loanSimulation.setTaxaJuro(product.getTaxaJuros());

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
        
        loanSimulation.setPrazo(time);
        loanSimulation.setValorTotalParcelas(totalInstallmentValue);
        loanSimulation.setDataInclusaoSimulacao(LocalDate.now());
        loanSimulation.setTipoSimulacao(LoanType.PRICE.getId());

        return installments;
    }

    public LoanSummaryDTO getAllLoanSimulations(LoanFilterDTO loanFilterDTO) {
        Log.info("Buscando as simulacoes de emprestimo com pageSize: " + loanFilterDTO.getPageSize() + ", page: " + loanFilterDTO.getPage());

        List<LoanSimulation> simulations = loanSimulationRepository.listAllFIltered(loanFilterDTO);

        LoanSummaryDTO loanSummaryDTO = new LoanSummaryDTO();
        loanSummaryDTO.setPagina(loanFilterDTO.getPage());
        loanSummaryDTO.setQtdRegistros(loanSimulationRepository.count());
        loanSummaryDTO.setQtdRegistrosPagina(simulations.size());
        loanSummaryDTO.setRegistros(simulations.stream().map(loanSimulationMapper::toLoanDTO).toList());

        return loanSummaryDTO;
    }
}
