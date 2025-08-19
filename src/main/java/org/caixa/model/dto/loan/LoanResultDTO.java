package org.caixa.model.dto.loan;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Getter
@Setter
@Schema(description = "Resultado da simulação do empréstimo")
public class LoanResultDTO {

    @Schema(description = "TIpo de empréstimo", example = "SAC")
    private String tipo;

    @Schema(description = "Parcelas do empréstimo")
    private List<InstallmentDTO> parcelas;
}
