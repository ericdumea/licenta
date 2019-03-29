package ro.utcluj.lic.domain;

import org.bson.types.ObjectId;

import java.math.BigDecimal;

public class SimpleProvider {

    private ObjectId id;

    private String type;

    private BigDecimal energy;

    private boolean flag;

    public SimpleProvider() {
    }

    public SimpleProvider(Provider provider) {
        this.id = provider.getId();
        this.type = provider.getProducerType();
        this.energy = provider.getEnergy().get(0);
        this.setFlag(false);
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

    public BigDecimal getEnergy() {
        return energy;
    }

    public void setEnergy(BigDecimal energy) {
        this.energy = energy;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
