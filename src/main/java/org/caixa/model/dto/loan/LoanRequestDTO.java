package org.caixa.model.dto.loan;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO para requisição da simulacao de empréstimo")
public class LoanRequestDTO {

    @NotNull(message = "Valor desejado não pode ser nulo")
    @Schema(description = "Valor desejado do empréstimo", example = "10000.00")
    private BigDecimal valorDesejado;

    @NotNull(message = "Prazo não pode ser nulo")
    @Schema(description = "Prazo em meses para pagamento do empréstimo", example = "12")
    private Integer prazo;
}
