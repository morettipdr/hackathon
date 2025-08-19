package org.caixa.model.dto.health;

import lombok.Data;
import java.math.BigDecimal;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Schema(description = "Volume de simulações por produto")
public class LoanVolumeDTO {

    @Schema(description = "Código identificador do produto", example = "12345")
    private Long codigoProduto;

    @Schema(description = "Nome/Descrição do produto", example = "Crédito Pessoal")
    private String descricaoProduto;

    @Schema(description = "Taxa média de juros das simulações", example = "1.99")
    private BigDecimal taxaMediaJuro;

    @Schema(description = "Valor médio da prestação das simulações", example = "500.00")
    private BigDecimal valorMedioPrestacao;

    @Schema(description = "Valor total desejado nas simulações", example = "10000.00")
    private BigDecimal valorTotalDesejado;

    @Schema(description = "Valor total do crédito concedido", example = "9800.00")
    private BigDecimal valorTotalCredito;
}