package org.caixa.mapper;

import org.caixa.model.dto.loan.LoanDTO;
import org.caixa.model.entity.loan.LoanSimulation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LoanSimulationMapper {

    LoanDTO toLoanDTO(LoanSimulation loanSimulation);
}
