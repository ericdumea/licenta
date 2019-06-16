package ro.utcluj.lic.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "provider_solution")
public class ProviderSolution {

    private ObjectId id;

    private List<Provider> providers;

    private Double fitnessValue;

    private Consumer consumer;

    public ProviderSolution() {
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public Double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(Double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
