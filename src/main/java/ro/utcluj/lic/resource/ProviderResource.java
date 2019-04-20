package ro.utcluj.lic.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.utcluj.lic.domain.Provider;
import ro.utcluj.lic.domain.SimpleProvider;
import ro.utcluj.lic.service.FireflyImplementation;
import ro.utcluj.lic.service.ProviderService;

import java.util.Collections;
import java.util.List;

import static java.lang.System.gc;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/provider")
public class ProviderResource {

    private final ProviderService providerService;

    private final FireflyImplementation fireflyImplementation;

    public ProviderResource(ProviderService providerService, FireflyImplementation fireflyImplementation) {
        this.providerService = providerService;
        this.fireflyImplementation = fireflyImplementation;
    }

    @GetMapping("/test-algo")
    public List<List<SimpleProvider>> doAlgorithm() {
//        for (int i = 0; i < 100; i++) {
//            fireflyImplementation.doAlgorithm(i);
//          //  gc();
//        }
        return Collections.singletonList(fireflyImplementation.doAlgorithm(19));
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
