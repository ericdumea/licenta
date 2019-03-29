package ro.utcluj.lic.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "producer")
public class Provider {

    private ObjectId id;

    private String producerType;

    private Boolean flag;

    //FIXME to be converted to a list of 24, represinting the hours
    private List<BigDecimal> energy;

    public Provider() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getProducerType() {
        return producerType;
    }

    public void setProducerType(String producerType) {
        this.producerType = producerType;
    }

    public Boolean getFlag() {
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
}
