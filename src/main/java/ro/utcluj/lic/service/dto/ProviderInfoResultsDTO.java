package ro.utcluj.lic.service.dto;

public class ProviderInfoResultsDTO {
    private String providerType;

    private int nrOfProvidersActivated;

    private int desiredPercentage;

    private int actualPercentage;

    private Double price;

    public ProviderInfoResultsDTO() {
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public int getNrOfProvidersActivated() {
        return nrOfProvidersActivated;
    }

    public void setNrOfProvidersActivated(int nrOfProvidersActivated) {
        this.nrOfProvidersActivated = nrOfProvidersActivated;
    }

    public int getDesiredPercentage() {
        return desiredPercentage;
    }

    public void setDesiredPercentage(int desiredPercentage) {
        this.desiredPercentage = desiredPercentage;
    }

    public int getActualPercentage() {
        return actualPercentage;
    }

    public void setActualPercentage(int actualPercentage) {
        this.actualPercentage = actualPercentage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
