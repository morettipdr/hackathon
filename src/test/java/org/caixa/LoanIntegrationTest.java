package org.caixa;

import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.caixa.eventhub.Sender;
import org.caixa.model.dto.loan.LoanFilterDTO;
import org.caixa.model.dto.loan.LoanRequestDTO;
import org.caixa.model.dto.loan.LoanResponseDTO;
import org.caixa.model.entity.loan.LoanSimulation;
import org.caixa.model.entity.product.Product;
import org.caixa.repository.LoanSimulationRepository;
import org.caixa.repository.ProductRepository;
import org.caixa.service.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@QuarkusTest
public class LoanIntegrationTest {

    @InjectMock
    Sender sender;

    @Inject
    LoanSimulationRepository loanSimulationRepository;

    @InjectMock
    ProductRepository productRepository;

    @Inject
    LoanService loanService;

    private Product createProduct() {
        Product product = new Product();
        product.setId(1);
        product.setNome("Produto 1");
        product.setTaxaJuros(new BigDecimal("0.05"));
        product.setValorMinimo(new BigDecimal("500"));
        product.setValorMaximo(new BigDecimal("50000"));
        product.setMinimoMeses((short) 6);
        product.setMaximoMeses((short) 60);
        return product;
    }

    private LoanSimulation createLoanSimulation(Integer id, Double valorTotalParcelas, Integer tipoSimulacao) {
        LoanSimulation loanSimulation = new LoanSimulation();
        loanSimulation.setIdSimulacao((long) id);
        loanSimulation.setCodigoProduto(1L);
        loanSimulation.setValorDesejado(new BigDecimal("1937.30"));
        loanSimulation.setPrazo(10);
        loanSimulation.setTaxaJuro(new BigDecimal("0.0500"));
        loanSimulation.setValorTotalParcelas(new BigDecimal(valorTotalParcelas).setScale(2, RoundingMode.HALF_UP));
        loanSimulation.setTipoSimulacao(tipoSimulacao);
        loanSimulation.setDataInclusaoSimulacao(LocalDate.now());
        return loanSimulation;
    }

    @Test
    public void testSimulateLoan(){
        Product product = createProduct();
        Mockito.doNothing().when(sender).publishEvents(Mockito.any());
        Mockito.when(productRepository.findByValue(Mockito.any())).thenReturn(product);

        LoanRequestDTO loanRequestDTO = new LoanRequestDTO();
        loanRequestDTO.setPrazo(10);
        BigDecimal valorDesejado = new BigDecimal(1937.30);
        loanRequestDTO.setValorDesejado(valorDesejado);
        LoanResponseDTO loanResponseDTO = loanService.simulateLoan(loanRequestDTO);
        LoanFilterDTO loanFilterDTO = new LoanFilterDTO();
        loanFilterDTO.setPage(1);
        loanFilterDTO.setPageSize(10);
        List<LoanSimulation> loanSimulationList = loanSimulationRepository.listAllFIltered(loanFilterDTO);

        Assertions.assertEquals(loanSimulationList.getFirst(), createLoanSimulation(1, 2470.06, 1));
        Assertions.assertEquals(loanSimulationList.getLast(), createLoanSimulation(2, 2508.90, 2));

        Assertions.assertEquals(loanResponseDTO.getTaxaJuros(), new BigDecimal("0.0500"));
        for (var resultado : loanResponseDTO.getResultadoSimulacao()) {
            Assertions.assertEquals(resultado.getParcelas().size(), 10);
        }
    }

}
