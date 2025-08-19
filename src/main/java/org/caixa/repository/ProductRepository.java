package org.caixa.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.caixa.model.entity.Product;

import java.math.BigDecimal;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Product findByValue(BigDecimal minimumValue) {
        return find("(valorMaximo >= ?1 or valorMaximo is null) and valorMinimo <= ?1 order by taxaJuros asc", minimumValue).firstResult();
    }
}
