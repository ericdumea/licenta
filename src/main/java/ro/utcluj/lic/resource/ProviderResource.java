package ro.utcluj.lic.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.utcluj.lic.domain.Provider;
import ro.utcluj.lic.service.ProviderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/provider")
public class ProviderResource {

    private final ProviderService providerService;

    public ProviderResource(ProviderService providerService) {
        this.providerService = providerService;
    }




    @GetMapping("/load-provider-data")
    public ResponseEntity<List<Provider>> loadProducerDataInDB(){
            return ok().body(providerService.insertProviders());
    }

    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders() {
        return ok().body(providerService.getAllProviders());
    }

}
