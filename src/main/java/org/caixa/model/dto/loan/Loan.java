package org.caixa.model.dto.loan;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Dados do empréstimo")
public class Loan {

    @Schema(description = "Id da simulação", example = "12345")
    private Long idSimulacao;

    @Schema(description = "Valor desejado do empréstimo", example = "10000.00")
    private BigDecimal valorDesejado;

    @Schema(description = "Prazo em meses para pagamento", example = "12")
    private Integer prazo;

    @Schema(description = "Valor total das parcelas", example = "11500.00")
    private BigDecimal valorTotalParcelas;
}