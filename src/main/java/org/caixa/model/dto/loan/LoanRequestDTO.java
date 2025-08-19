package org.caixa.model.dto.loan;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Data
public class LoanRequestDTO {

    @Schema(description = "Valor desejado do empréstimo", example = "10000.00")
    private BigDecimal valorDesejado;

    @Schema(description = "Prazo em meses para pagamento do empréstimo", example = "12")
    private Integer prazo;
}
