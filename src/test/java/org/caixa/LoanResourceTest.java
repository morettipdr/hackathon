package org.caixa;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.caixa.model.dto.loan.LoanRequestDTO;
import org.caixa.model.dto.loan.LoanFilterDTO;
import org.caixa.model.dto.loan.LoanResponseDTO;
import org.caixa.model.dto.loan.LoanSummaryDTO;
import org.caixa.service.LoanService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class LoanResourceTest {

    @InjectMock
    LoanService loanService;

    @Test
    public void requestLoanReturnsSimulationResult() {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setValorDesejado(BigDecimal.valueOf(1000));
        request.setPrazo(12);

        LoanResponseDTO loanResponseDTO = new LoanResponseDTO();
        loanResponseDTO.setTaxaJuros(BigDecimal.valueOf(0.1));

        Mockito.when(loanService.simulateLoan(Mockito.any())).thenReturn(loanResponseDTO);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/simulacao")
        .then()
            .statusCode(200)
            .body("taxaJuros", equalTo(0.1F));
    }

    @Test
    public void requestLoanHandlesNoProductFound() {
        LoanRequestDTO request = new LoanRequestDTO();
        request.setValorDesejado(BigDecimal.valueOf(1000000));
        request.setPrazo(1);

        Mockito.when(loanService.simulateLoan(Mockito.any()))
               .thenThrow(new IllegalArgumentException("Nenhum produto encontrado para os critérios fornecidos."));

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/simulacao")
        .then()
            .statusCode(400)
            .body(containsString("Nenhum produto encontrado para os critérios fornecidos."));
    }

    @Test
    public void requestLoanReturnsBadRequestForInvalidInput() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/simulacao")
        .then()
            .statusCode(400)
            .body(containsString("Valor desejado não pode ser nulo"))
                .body(containsString("Prazo não pode ser nulo"));
    }

    @Test
    public void getAllLoansReturnsListOfSimulations() {
        LoanFilterDTO filter = new LoanFilterDTO();
        filter.setPage(1);
        filter.setPageSize(10);

        LoanSummaryDTO loanSummaryDTO = new LoanSummaryDTO();
        loanSummaryDTO.setQtdRegistrosPagina(10);
        loanSummaryDTO.setQtdRegistros(20L);

        Mockito.when(loanService.getAllLoanSimulations(Mockito.any())).thenReturn(loanSummaryDTO);

        given()
            .contentType(ContentType.JSON)
            .queryParam("page", filter.getPage())
            .queryParam("pageSize", filter.getPageSize())
        .when()
            .get("/simulacao")
        .then()
            .statusCode(200)
            .body("qtdRegistrosPagina", equalTo(10))
            .body("qtdRegistros", equalTo(20));
    }

    @Test
    public void getAllLoansHandlesEmptyResult() {
        LoanSummaryDTO loanSummaryDTO = new LoanSummaryDTO();
        loanSummaryDTO.setQtdRegistros(0L);
        loanSummaryDTO.setQtdRegistrosPagina(0);
        loanSummaryDTO.setPagina(10);
        Mockito.when(loanService.getAllLoanSimulations(Mockito.any())).thenReturn(loanSummaryDTO);

        given()
            .contentType(ContentType.JSON)
            .queryParam("pageSize", 999)
            .queryParam("page", 10)
        .when()
            .get("/simulacao")
        .then()
            .statusCode(200)
            .body("qtdRegistros", equalTo(0))
            .body("qtdRegistrosPagina", equalTo(0))
            .body("pagina", equalTo(10));
    }

    @Test
    public void getAllLoansBadRequestForInvalidInput() {
        given()
            .contentType(ContentType.JSON)
            .queryParam("page", -1)
            .queryParam("pageSize", "")
        .when()
            .get("/simulacao")
        .then()
            .statusCode(400)
            .body(containsString("O campo page deve ser um número positivo"))
            .body(containsString("O campo pageSize é obrigatório"));
    }
}