package org.caixa.model.dto.loan;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Getter
@Setter
@Schema(description = "Resumo das simulações de empréstimo")
public class LoanSummaryDTO {

    @Schema(description = "Página atual", example = "0")
    private Integer pagina;

    @Schema(description = "Quantidade total de registros", example = "100")
    private Long qtdRegistros;

    @Schema(description = "Quantidade de registros por página", example = "10")
    private Integer qtdRegistrosPagina;

    @Schema(description = "Lista de empréstimos")
    private List<LoanDTO> registros;
}