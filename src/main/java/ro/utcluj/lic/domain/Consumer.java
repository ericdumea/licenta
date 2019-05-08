package ro.utcluj.lic.domain;

import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.List;

public class Consumer {
    private ObjectId id;

    private List<BigDecimal> power;

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
}
