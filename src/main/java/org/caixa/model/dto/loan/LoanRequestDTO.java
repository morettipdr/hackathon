package org.caixa.model.dto.loan;

import io.smallrye.common.constraint.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Data
public class LoanRequestDTO {

    @NotNull
    @Schema(description = "Valor desejado do empréstimo", example = "10000.00")
    private BigDecimal desiredAmount;

    @NotNull
    @Schema(description = "Prazo em meses para pagamento do empréstimo", example = "12")
    private Integer term;
}
