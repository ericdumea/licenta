package ro.utcluj.lic.service.dto;

import java.util.Objects;

public class ExcelExportWeightsDTO {

    private Double heterogenityPenalty;

    private Double pricePenalty;

    private Double fitnessAverage;

    private Double timingAverage;

    private Double actualPrice;

    private Double actualPercentage;

    public ExcelExportWeightsDTO() {
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Double getActualPercentage() {
        return actualPercentage;
    }

    public void setActualPercentage(Double actualPercentage) {
        this.actualPercentage = actualPercentage;
    }

    public Double getHeterogenityPenalty() {
        return heterogenityPenalty;
    }

    public void setHeterogenityPenalty(Double heterogenityPenalty) {
        this.heterogenityPenalty = heterogenityPenalty;
    }

    public Double getPricePenalty() {
        return pricePenalty;
    }

    public void setPricePenalty(Double pricePenalty) {
        this.pricePenalty = pricePenalty;
    }

    public Double getFitnessAverage() {
        return fitnessAverage;
    }

    public void setFitnessAverage(Double fitnessAverage) {
        this.fitnessAverage = fitnessAverage;
    }

    public Double getTimingAverage() {
        return timingAverage;
    }

    public void setTimingAverage(Double timingAverage) {
        this.timingAverage = timingAverage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExcelExportWeightsDTO that = (ExcelExportWeightsDTO) o;
        return Objects.equals(heterogenityPenalty, that.heterogenityPenalty) &&
                Objects.equals(pricePenalty, that.pricePenalty) &&
                Objects.equals(fitnessAverage, that.fitnessAverage) &&
                Objects.equals(timingAverage, that.timingAverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heterogenityPenalty, pricePenalty, fitnessAverage, timingAverage);
    }
}
