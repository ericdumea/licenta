package ro.utcluj.lic.service.dto;

import java.math.BigDecimal;

public class WeightTestDTO {

    private BigDecimal fitness;

    private double actualPrice;

    private double actualPercentage;

    public WeightTestDTO() {
    }

    public BigDecimal getFitness() {
        return fitness;
    }

    public void setFitness(BigDecimal fitness) {
        this.fitness = fitness;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getActualPercentage() {
        return actualPercentage;
    }

    public void setActualPercentage(double actualPercentage) {
        this.actualPercentage = actualPercentage;
    }
}
