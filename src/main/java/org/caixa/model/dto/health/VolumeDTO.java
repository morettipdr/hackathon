package org.caixa.model.dto.health;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Schema(description = "Volume de simulações de empréstimo por período")
public class VolumeDTO {

    @Schema(description = "Data de referência das simulações", example = "2024-03-20")
    private LocalDate dataReferencia;

    @Schema(description = "Lista de simulações agrupadas por produto")
    private List<LoanVolumeDTO> simulacoes;
}