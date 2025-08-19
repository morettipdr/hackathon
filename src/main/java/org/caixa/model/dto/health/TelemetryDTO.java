package org.caixa.model.dto.health;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Schema(description = "Telemetria das APIs do sistema")
public class TelemetryDTO {

    @Schema(description = "Data de referência da telemetria", example = "2025-07-30")
    private LocalDate dataReferencia;

    @Schema(description = "Lista de métricas por endpoint")
    private List<EndpointMetricsDTO> listaEndpoints;
}