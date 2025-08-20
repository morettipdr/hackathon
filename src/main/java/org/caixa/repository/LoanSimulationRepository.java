package org.caixa.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.caixa.model.entity.LoanSimulation;

import java.util.List;

@ApplicationScoped
public class LoanSimulationRepository implements PanacheRepository<LoanSimulation> {

    public List<LoanSimulation> findAllSimulations() {
        return listAll();
    }
}
