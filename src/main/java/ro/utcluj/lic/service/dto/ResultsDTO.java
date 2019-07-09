package ro.utcluj.lic.service.dto;

import java.util.List;

public class ResultsDTO {

    private String id;

    private List<Double> computedEnergy;

    private ConsumerDTO consumerDTO;

    private Double fitnessValue;

    private List<ProviderInfoResultsDTO> providerInfoList;

    public ResultsDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Double> getComputedEnergy() {
        return computedEnergy;
    }

    public void setComputedEnergy(List<Double> computedEnergy) {
        this.computedEnergy = computedEnergy;
    }

    public ConsumerDTO getConsumerDTO() {
        return consumerDTO;
    }

    public void setConsumerDTO(ConsumerDTO consumerDTO) {
        this.consumerDTO = consumerDTO;
    }

    public Double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(Double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public List<ProviderInfoResultsDTO> getProviderInfoList() {
        return providerInfoList;
    }

    public void setProviderInfoList(List<ProviderInfoResultsDTO> providerInfoList) {
        this.providerInfoList = providerInfoList;
    }
}
