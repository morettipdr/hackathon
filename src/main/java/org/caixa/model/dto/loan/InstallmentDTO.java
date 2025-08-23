package org.caixa.model.dto.loan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Detalhes de uma parcela do empréstimo")
public class InstallmentDTO {

    @Schema(description = "Número da parcela", example = "1")
    private Integer numero;

    @Schema(description = "Valor da amortização", example = "500.00")
    private BigDecimal valorAmortizacao;

    @Schema(description = "Valor dos juros", example = "89.50")
    private BigDecimal valorJuros;

    @Schema(description = "Valor da prestação", example = "589.50")
    private BigDecimal valorPrestacao;
}
