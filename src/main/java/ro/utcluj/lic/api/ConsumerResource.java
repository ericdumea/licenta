package ro.utcluj.lic.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<List<BigDecimal>> loadConsumerDataInDB(){
//        try {
            return ok().body(consumerService.loadConsumersFromFile());
//        } catch (IOException e) {
//            return badRequest().build();
//        }
    }

}
