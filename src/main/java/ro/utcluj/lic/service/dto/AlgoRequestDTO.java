package ro.utcluj.lic.service.dto;

import ro.utcluj.lic.domain.ProviderType;

import java.util.List;

public class AlgoRequestDTO {

    private String consumerId;

    private int numberOfIterations;

    private int numberOfFireflies;

    private int startHour;

    private int endHour;

    private List<ProviderType> providerTypes;

    public AlgoRequestDTO() {
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public int getNumberOfFireflies() {
        return numberOfFireflies;
    }

    public void setNumberOfFireflies(int numberOfFireflies) {
        this.numberOfFireflies = numberOfFireflies;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public List<ProviderType> getProviderTypes() {
        return providerTypes;
    }

    public void setProviderTypes(List<ProviderType> providerTypes) {
        this.providerTypes = providerTypes;
    }
}
