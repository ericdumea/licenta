package ro.utcluj.lic.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.utcluj.lic.domain.Constants;
import ro.utcluj.lic.domain.Provider;
import ro.utcluj.lic.domain.SimpleProvider;
import ro.utcluj.lic.service.FireflyImplementation;
import ro.utcluj.lic.service.ProviderService;
import ro.utcluj.lic.service.TestAlgorithmParametersService;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/provider")
public class ProviderResource {

    private final ProviderService providerService;

    private final FireflyImplementation fireflyImplementation;

    private final TestAlgorithmParametersService testAlgorithmParametersService;

    public ProviderResource(ProviderService providerService, FireflyImplementation fireflyImplementation, TestAlgorithmParametersService testAlgorithmParametersService) {
        this.providerService = providerService;
        this.fireflyImplementation = fireflyImplementation;
        this.testAlgorithmParametersService = testAlgorithmParametersService;
    }

    @GetMapping("/test-algo")
    public List<List<SimpleProvider>> doAlgorithm(@RequestParam String type, @RequestParam double percentage) {
        for (int i = 0; i < 23; i++) {
            fireflyImplementation.doAlgorithm(i, Constants.NO_F, Constants.NO_ITERATIONS, type, percentage);
          //  gc();
        }
        return Collections.singletonList(fireflyImplementation.doAlgorithm(19, Constants.NO_F, Constants.NO_ITERATIONS, type, percentage));
    }

    //FIXME to be converted to POST
    @GetMapping("/excel")
    public ResponseEntity<Void> dumpToExcelTest() {
        return testAlgorithmParametersService.testAlgorithm();
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
