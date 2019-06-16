package ro.utcluj.lic.service.dto;

import org.bson.types.ObjectId;
import ro.utcluj.lic.domain.Provider;

import java.util.List;

public class ProviderDTO {

    private ObjectId id;

    private String tyoe;

    private Double price;

    private List<Double> energy;

    public ProviderDTO() {
    }

    public ProviderDTO(Provider provider) {

    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTyoe() {
        return tyoe;
    }

    public void setTyoe(String tyoe) {
        this.tyoe = tyoe;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Double> getEnergy() {
        return energy;
    }

    public void setEnergy(List<Double> energy) {
        this.energy = energy;
    }
}
