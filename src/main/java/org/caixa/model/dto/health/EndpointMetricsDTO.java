package org.caixa.model.dto.health;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Schema(description = "Métricas de um endpoint específico")
public class EndpointMetricsDTO {

    @Schema(description = "Nome da API", example = "Simulacao")
    private String nomeApi;

    @Schema(description = "Quantidade total de requisições", example = "135")
    private Long qtdRequisicoes;

    @Schema(description = "Tempo médio de resposta em milissegundos", example = "150")
    private Long tempoMedio;

    @Schema(description = "Tempo mínimo de resposta em milissegundos", example = "23")
    private Long tempoMinimo;

    @Schema(description = "Tempo máximo de resposta em milissegundos", example = "860")
    private Long tempoMaximo;

    @Schema(description = "Percentual de sucesso das requisições", example = "0.98")
    private Double percentualSucesso;
}