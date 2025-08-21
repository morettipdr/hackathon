package org.caixa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.caixa.model.dto.health.LoanVolumeDTO;
import org.caixa.model.dto.health.VolumeRequestDTO;
import org.caixa.model.enums.LoanType;
import org.caixa.repository.LoanSimulationRepository;
import org.caixa.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class HealthService {

    @Inject
    LoanSimulationRepository loanSimulationRepository;

    @Inject
    ProductRepository productRepository;

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
}
