package org.caixa.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import org.caixa.model.dto.health.LoanVolumeDTO;
import org.caixa.model.dto.loan.LoanFilterDTO;
import org.caixa.model.entity.loan.LoanSimulation;
import org.caixa.model.enums.LoanType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LoanSimulationRepository implements PanacheRepository<LoanSimulation> {

    public List<LoanSimulation> listAllFIltered(LoanFilterDTO loanFilterDTO) {
       return findAll().page(Page.of(loanFilterDTO.getPage() - 1, loanFilterDTO.getPageSize())).list();
    }

   public LoanVolumeDTO fillVolumeByProductAndDate(Long produtoId, LocalDate data, LoanVolumeDTO loanVolumeDTO, LoanType loanType) {
       String query ="select AVG(taxaJuro), AVG(valorTotalParcelas / prazo), SUM(valorDesejado), SUM(valorTotalParcelas) " +
                "from LoanSimulation " +
                "where dataInclusaoSimulacao = ?1 and codigoProduto = ?2 and tipoSimulacao = ?3";

       Object[] queryResult = find(query, data, produtoId, loanType.getId()).project(Object.class).firstResult();
       loanVolumeDTO.setTipoSimulacao(loanType.getDescription());

       if(isResultNull(queryResult)){
           return loanVolumeDTO;
       }

       loanVolumeDTO.setTaxaMediaJuro(toBigDecimal(queryResult[0]).setScale(4, BigDecimal.ROUND_HALF_UP));
       loanVolumeDTO.setValorMedioPrestacao(toBigDecimal(queryResult[1]).setScale(2, BigDecimal.ROUND_HALF_UP));
       loanVolumeDTO.setValorTotalDesejado((toBigDecimal(queryResult[2])).setScale(2, BigDecimal.ROUND_HALF_UP));
       loanVolumeDTO.setValorTotalCredito(((BigDecimal) queryResult[3]).setScale(2, BigDecimal.ROUND_HALF_UP));
       return loanVolumeDTO;
   }

    private boolean isResultNull(Object[] objects) {
        return Arrays.stream(objects).filter(object -> object != null).count() == 0;
    }

    private BigDecimal toBigDecimal(Object object){
        if(object instanceof Double){
            return BigDecimal.valueOf((Double) object);
        }
        return (BigDecimal) object;
   }
}
