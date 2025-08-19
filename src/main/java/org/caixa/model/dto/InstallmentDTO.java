package org.caixa.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "Detalhes de uma parcela do empréstimo")
public class InstallmentDTO {

    @Schema(description = "Número da parcela", example = "1")
    private Integer numero;

    @Schema(description = "Valor da amortização", example = "500.00")
    private Double valorAmortizacao;

    @Schema(description = "Valor dos juros", example = "89.50")
    private Double valorJuros;

    @Schema(description = "Valor da prestação", example = "589.50")
    private Double valorPrestacao;
}
