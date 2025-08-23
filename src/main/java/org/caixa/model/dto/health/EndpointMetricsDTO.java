package org.caixa.model.dto.health;

import lombok.*;
import org.apache.qpid.proton.amqp.transport.End;
import org.caixa.model.enums.EndpointType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Métricas de um endpoint específico")
public class EndpointMetricsDTO {

    @Schema(description = "Nome da API", example = "Simulacao")
    private String nomeApi;

    @Schema(description = "Quantidade total de requisições", example = "135")
    private BigDecimal qtdRequisicoes;

    @Schema(description = "Tempo médio de resposta em milissegundos", example = "150")
    private BigDecimal tempoMedio;

    @Schema(description = "Tempo máximo de resposta em milissegundos", example = "860")
    private BigDecimal tempoMaximo;

    @Schema(description = "Percentual de sucesso das requisições", example = "0.98")
    private BigDecimal percentualSucesso;

    public static EndpointMetricsDTO notUsedEndpoint(EndpointType endpointType){
        return new EndpointMetricsDTO(endpointType.getDescricao(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}