package org.caixa.mapper;

import org.caixa.model.dto.loan.LoanDTO;
import org.caixa.model.dto.loan.LoanSummaryDTO;
import org.caixa.model.entity.LoanSimulation;
import org.mapstruct.Mapper;

@Mapper
public interface LoanSimulationMapper {

    LoanDTO toLoanDTO(LoanSimulation loanSimulation);
}
