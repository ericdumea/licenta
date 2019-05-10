package ro.utcluj.lic.api;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.utcluj.lic.domain.Consumer;
import ro.utcluj.lic.service.ConsumerService;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerResource {

    private final ConsumerService consumerService;


    public ConsumerResource(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping("/load-consumer-data")
    public ResponseEntity<List<BigDecimal>> loadConsumerDataInDB() {
        return ok().body(consumerService.loadConsumersFromFile());
    }

    @PostMapping("/save-in-db")
    public ResponseEntity<Void> insertConsumerDataInDB() {
        consumerService.batchInsertFromFile();
        return ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Consumer>> getAllConsumers() {
        return ok().body(consumerService.getAllConsumers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consumer> getConsumerById(@PathVariable ObjectId id) {
        return ok().build(); //TODO get by id, consumer
    }
}
