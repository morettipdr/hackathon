package org.caixa.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.caixa.model.dto.loan.LoanRequestDTO;
import org.caixa.model.entity.product.Product;

import java.math.BigDecimal;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Product findByValue(LoanRequestDTO request) {
        Product product = find("(valorMaximo >= ?1 or valorMaximo is null) and valorMinimo <= ?1 and " +
                "minimoMeses <= ?2 and (maximoMeses is null or maximoMeses >= ?2) " +
                "order by taxaJuros asc", request.getValorDesejado(), request.getPrazo()).firstResult();
        if(product == null) throw new IllegalArgumentException("Nenhum produto encontrado para os critérios fornecidos.");
        return product;
    }
}
