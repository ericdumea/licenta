package ro.utcluj.lic.domain;

import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.Objects;

public class SimpleProvider {

    private ObjectId id;

    private String type;

    private BigDecimal energy;

    private boolean flag;

    public SimpleProvider() {
    }

    public SimpleProvider(Provider provider, int idx) {
        this.id = provider.getId();
        this.type = provider.getProducerType();
        this.energy = provider.getEnergy().get(idx);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleProvider that = (SimpleProvider) o;
        return flag == that.flag &&
                Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(energy, that.energy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, energy, flag);
    }
}
