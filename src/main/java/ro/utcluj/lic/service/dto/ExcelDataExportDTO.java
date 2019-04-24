package ro.utcluj.lic.service.dto;

import java.util.List;
import java.util.Objects;

public class ExcelDataExportDTO {

    private int numberOfIterations;

    private Double fitnessAverage;

    private Double timingAverage;

    public ExcelDataExportDTO() {
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
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
        ExcelDataExportDTO that = (ExcelDataExportDTO) o;
        return numberOfIterations == that.numberOfIterations &&
                Objects.equals(fitnessAverage, that.fitnessAverage) &&
                Objects.equals(timingAverage, that.timingAverage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfIterations, fitnessAverage, timingAverage);
    }
}
