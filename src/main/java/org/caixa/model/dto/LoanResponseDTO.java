package org.caixa.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Resposta da simulação de empréstimo")
public class LoanResponseDTO {

    @Schema(description = "Id do empréstimo", example = "12345")
    private Integer idSimulacao;

    @Schema(description = "Código do produto", examples = "123")
    private Integer codigoProduto;

    @Schema(description = "Descrição do produto", example = "Empréstimo Pessoal")
    private String descricaoProduto;

    @Schema(description = "Taxa de juros do produto", example = "0.0179")
    private BigDecimal taxaJuros;

    @Schema(description = "Resultado da simulação")
    private LoanResultDTO resultadoSimulacao;
}
