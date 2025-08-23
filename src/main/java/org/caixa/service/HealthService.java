package org.caixa.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.MeterNotFoundException;
import io.micrometer.core.instrument.search.RequiredSearch;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.caixa.model.dto.health.EndpointMetricsDTO;
import org.caixa.model.dto.health.LoanVolumeDTO;
import org.caixa.model.dto.health.TelemetryDTO;
import org.caixa.model.dto.health.VolumeRequestDTO;
import org.caixa.model.enums.EndpointType;
import org.caixa.model.enums.LoanType;
import org.caixa.repository.LoanSimulationRepository;
import org.caixa.repository.ProductRepository;
import org.hibernate.resource.beans.container.spi.BeanContainer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class HealthService {

    @Inject
    LoanSimulationRepository loanSimulationRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    MeterRegistry meterRegistry;

    public List<LoanVolumeDTO> getVolume(VolumeRequestDTO volumeRequestDTO){
        LoanVolumeDTO sacVolumeDTO = new LoanVolumeDTO();
        LoanVolumeDTO priceVolumeDTO = new LoanVolumeDTO();

        sacVolumeDTO.setCodigoProduto(volumeRequestDTO.getIdProduto());
        sacVolumeDTO.setDescricaoProduto(productRepository.findById(volumeRequestDTO.getIdProduto()).getNome());
        priceVolumeDTO.setCodigoProduto(volumeRequestDTO.getIdProduto());
        priceVolumeDTO.setDescricaoProduto(productRepository.findById(volumeRequestDTO.getIdProduto()).getNome());

        List<LoanVolumeDTO> loanVolumeDTOList = new ArrayList<>();
        loanVolumeDTOList.add(loanSimulationRepository.fillVolumeByProductAndDate(volumeRequestDTO.getIdProduto(), volumeRequestDTO.getData(), sacVolumeDTO, LoanType.SAC));
        loanVolumeDTOList.add(loanSimulationRepository.fillVolumeByProductAndDate(volumeRequestDTO.getIdProduto(), volumeRequestDTO.getData(), priceVolumeDTO, LoanType.PRICE));
        return loanVolumeDTOList;
    }

    public TelemetryDTO getTelemetry(){
        TelemetryDTO telemetryDTO = new TelemetryDTO();
        telemetryDTO.setListaEndpoints(List.of(
                getEndpointMetrics(EndpointType.SIMULACAO),
                getEndpointMetrics(EndpointType.TELEMETRIA),
                getEndpointMetrics(EndpointType.VOLUME)));
        telemetryDTO.setDataReferencia(LocalDate.now());
        return telemetryDTO;
    }

    private EndpointMetricsDTO getEndpointMetrics(EndpointType endpointType){
        try {
            EndpointMetricsDTO endpointMetricsDTO = new EndpointMetricsDTO();
            endpointMetricsDTO.setNomeApi(endpointType.getDescricao());
            RequiredSearch requiredSearch = meterRegistry.get("http.server.requests").tag("uri", endpointType.getDescricao());
            BigDecimal successfulRequests = BigDecimal.valueOf(meterRegistry.get("http.server.requests").tag("status", "200").timer().count()).setScale(4);
            BigDecimal reqAmount = BigDecimal.valueOf(requiredSearch.timers().stream().map(Timer::count).reduce(0L, (a, b) -> a+b)).setScale(4);
            BigDecimal reqMaxTime = BigDecimal.valueOf(requiredSearch.timer().max(TimeUnit.SECONDS));
            BigDecimal reqMeanTime = BigDecimal.valueOf(requiredSearch.timer().mean(TimeUnit.SECONDS));

            endpointMetricsDTO.setPercentualSucesso(successfulRequests.divide(reqAmount, RoundingMode.HALF_UP));
            endpointMetricsDTO.setTempoMaximo(reqMaxTime);
            endpointMetricsDTO.setTempoMedio(reqMeanTime);
            endpointMetricsDTO.setQtdRequisicoes(reqAmount.setScale(0));
            return endpointMetricsDTO;
        } catch (MeterNotFoundException e){
            return EndpointMetricsDTO.notUsedEndpoint(endpointType);
        }
    }
}
