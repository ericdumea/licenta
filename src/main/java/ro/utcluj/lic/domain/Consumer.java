package ro.utcluj.lic.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "consumer")
public class Consumer {
    private ObjectId id;

    private List<BigDecimal> power;

    private double price;

    private String name;

    public Consumer() {
    }

    public Consumer(List<BigDecimal> power) {
        this.power = power;
    }

    public List<BigDecimal> getPower() {
        return power;
    }

    public void setPower(List<BigDecimal> power) {
        this.power = power;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
