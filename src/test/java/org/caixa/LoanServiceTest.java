package org.caixa;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.caixa.eventhub.Sender;
import org.caixa.model.dto.loan.InstallmentDTO;
import org.caixa.model.dto.loan.LoanRequestDTO;
import org.caixa.model.dto.loan.LoanResponseDTO;
import org.caixa.model.entity.loan.LoanSimulation;
import org.caixa.model.entity.product.Product;
import org.caixa.model.enums.LoanType;
import org.caixa.repository.ProductRepository;
import org.caixa.service.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@QuarkusTest
public class LoanServiceTest {

    @Inject
    LoanService loanService;

    @InjectMock
    ProductRepository productRepository;

    @InjectMock
    Sender sender;

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

    @Test
    public void testSimulateLoan(){
        Product product = createProduct();
        Mockito.when(productRepository.findByValue(Mockito.any())).thenReturn(product);
        Mockito.doNothing().when(sender).publishEvents(Mockito.any());

        LoanRequestDTO request = new LoanRequestDTO();
        request.setValorDesejado(new BigDecimal("1000"));
        request.setPrazo(12);

        LoanResponseDTO response = loanService.simulateLoan(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Long.valueOf(1), response.getCodigoProduto());
        Assertions.assertEquals(new BigDecimal("0.0500"), response.getTaxaJuros());
        Assertions.assertFalse(response.getResultadoSimulacao().isEmpty());
    }

    @Test
    public void testSimulateLoanProductNotFound(){
        Mockito.when(productRepository.findByValue(Mockito.any())).thenThrow(new IllegalArgumentException("Nenhum produto encontrado para os critérios fornecidos."));

        LoanRequestDTO request = new LoanRequestDTO();
        request.setValorDesejado(new BigDecimal("100"));
        request.setPrazo(12);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            loanService.simulateLoan(request);
        });
    }

    @Test
    public void testSacCalculator(){
        Product product = createProduct();
        LoanSimulation sacLoanSimulation = new LoanSimulation();
        List<InstallmentDTO> installments = new ArrayList<>();

        installments = loanService.sacCalculator(new BigDecimal(1923.30), product, 2, sacLoanSimulation);

        Assertions.assertNotNull(sacLoanSimulation);
        Assertions.assertEquals(sacLoanSimulation.getTipoSimulacao(), LoanType.SAC.getId());
        Assertions.assertEquals(sacLoanSimulation.getTaxaJuro(), product.getTaxaJuros());
        Assertions.assertEquals(sacLoanSimulation.getValorTotalParcelas(), new BigDecimal(2067.54).setScale(2, RoundingMode.HALF_UP));

        Assertions.assertEquals(installments.getFirst().getValorPrestacao(), new BigDecimal(1057.81).setScale(2, RoundingMode.HALF_UP));
        Assertions.assertEquals(installments.get(1).getValorPrestacao(), new BigDecimal(1009.73).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void testPriceCalculator(){
        Product product = createProduct();
        LoanSimulation priceLoanSimulation = new LoanSimulation();
        List<InstallmentDTO> installments = new ArrayList<>();

        installments = loanService.priceCalculator(new BigDecimal(1923.30), product, 2, priceLoanSimulation);

        Assertions.assertNotNull(priceLoanSimulation);
        Assertions.assertEquals(priceLoanSimulation.getTipoSimulacao(), LoanType.PRICE.getId());
        Assertions.assertEquals(priceLoanSimulation.getTaxaJuro(), product.getTaxaJuros());
        Assertions.assertEquals(priceLoanSimulation.getValorTotalParcelas(), new BigDecimal(2068.72).setScale(2, RoundingMode.HALF_UP));

        Assertions.assertEquals(installments.getFirst().getValorPrestacao(), new BigDecimal(1034.36).setScale(2, RoundingMode.HALF_UP));
        Assertions.assertEquals(installments.get(1).getValorPrestacao(), new BigDecimal(1034.36).setScale(2, RoundingMode.HALF_UP));
    }
}
