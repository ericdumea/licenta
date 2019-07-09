package ro.utcluj.lic.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.Provider;
import ro.utcluj.lic.domain.ProviderSolution;
import ro.utcluj.lic.repository.ProviderSolutionRepository;
import ro.utcluj.lic.service.dto.ConsumerDTO;
import ro.utcluj.lic.service.dto.ResultsDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderSolutionService {

    private final ProviderSolutionRepository providerSolutionRepository;

    public ProviderSolutionService(ProviderSolutionRepository providerSolutionRepository) {
        this.providerSolutionRepository = providerSolutionRepository;
    }

    public ProviderSolution insertSolution(ProviderSolution providerSolution) {
        return providerSolutionRepository.save(providerSolution);
    }

    public ProviderSolution getById(ObjectId objectId) {
        return providerSolutionRepository.findById(objectId).orElseThrow(() -> new RuntimeException("Provider not found"));
    }

    public ResultsDTO getResultsById(String id) {

        ProviderSolution providerSolution = providerSolutionRepository.findById(new ObjectId(id)).orElseThrow(() -> new RuntimeException("Provider not found"));
        ResultsDTO resultsDTO = new ResultsDTO();
        resultsDTO.setProviderInfoList(providerSolution.getProviderInfoList());
        resultsDTO.setConsumerDTO(new ConsumerDTO(providerSolution.getConsumer()));
        resultsDTO.setFitnessValue(providerSolution.getFitnessValue());

        List<Provider> activeProviders = providerSolution.getProviders().stream().filter(Provider::isFlag).collect(Collectors.toList());

        List<Double> fitnessValues = new ArrayList<>();


        for (int i = 0; i < 24; i++) {
            int finalI = i;
            fitnessValues.add(activeProviders.stream()
                    .map(Provider::getEnergy)
                    .map(list -> list.get(finalI))
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO)
                    .doubleValue()
            );
        }
        resultsDTO.setComputedEnergy(fitnessValues);
        return resultsDTO;
    }
}
