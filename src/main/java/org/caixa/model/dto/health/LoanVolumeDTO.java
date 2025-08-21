package org.caixa.model.dto.health;

import lombok.AllArgsConstructor;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Volume de simulações por produto")
public class LoanVolumeDTO {

    @Schema(description = "Código identificador do produto", example = "12345")
    private Long codigoProduto;

    @Schema(description = "Nome/Descrição do produto", example = "Crédito Pessoal")
    private String descricaoProduto;

    @Schema(description = "Taxa média de juros das simulações", example = "1.99")
    private BigDecimal taxaMediaJuro = BigDecimal.ZERO;

    @Schema(description = "Valor médio da prestação das simulações", example = "500.00")
    private BigDecimal valorMedioPrestacao = BigDecimal.ZERO;

    @Schema(description = "Valor total desejado nas simulações", example = "10000.00")
    private BigDecimal valorTotalDesejado = BigDecimal.ZERO;

    @Schema(description = "Valor total do crédito concedido", example = "9800.00")
    private BigDecimal valorTotalCredito = BigDecimal.ZERO;

    @Schema(description = "Tipo de simulação (SAC ou PRICE)", example = "SAC")
    private String tipoSimulacao;
}