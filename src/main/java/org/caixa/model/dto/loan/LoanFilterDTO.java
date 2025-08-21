package org.caixa.model.dto.loan;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.reactive.RestQuery;

@Getter
@Setter
public class LoanFilterDTO {

    @Positive(message = "O campo pageSize deve ser um número positivo")
    @NotNull(message = "O campo pageSize é obrigatório")
    @RestQuery("pageSize")
    Integer pageSize;

    @Positive(message = "O campo page deve ser um número positivo")
    @NotNull(message = "O campo page é obrigatório")
    @RestQuery("page")
    Integer page;
}
