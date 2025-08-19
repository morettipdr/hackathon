package org.caixa.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRequestDTO {

    @Schema(description = "Valor desejado do empréstimo", example = "10000.00")
    private BigDecimal valorDesejado;

    @Schema(description = "Prazo em meses para pagamento do empréstimo", example = "12")
    private Integer prazo;
}
