package ro.utcluj.lic.service.dto;

import org.bson.types.ObjectId;
import ro.utcluj.lic.domain.Consumer;

import java.math.BigDecimal;
import java.util.List;

public class ConsumerDTO {

    private String name;

    private String id;

    private List<BigDecimal> power;

    private double price;

    public ConsumerDTO() {
    }

    public ConsumerDTO(Consumer consumer) {
        this.name = consumer.getName();
        this.power = consumer.getPower();
        this.price = consumer.getPrice();
        this.id = consumer.getId().toHexString();
    }

    public List<BigDecimal> getPower() {
        return power;
    }

    public void setPower(List<BigDecimal> power) {
        this.power = power;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
