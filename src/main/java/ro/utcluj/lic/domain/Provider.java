package ro.utcluj.lic.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "producer")
public class Provider {

    private ObjectId id;

    private String type;

    private boolean flag;

    private List<BigDecimal> energy;

    private double price;

    public Provider() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public List<BigDecimal> getEnergy() {
        return energy;
    }

    public void setEnergy(List<BigDecimal> energy) {
        this.energy = energy;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
