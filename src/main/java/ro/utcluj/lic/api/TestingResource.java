package ro.utcluj.lic.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.utcluj.lic.service.ExcelExportService;
import ro.utcluj.lic.service.ProviderService;
import ro.utcluj.lic.service.TestAlgorithmParametersService;

@RestController
@RequestMapping("/api/test")
public class TestingResource {

    private final TestAlgorithmParametersService testAlgorithmParametersService;

    private final ProviderService providerService;

    private final ExcelExportService excelExportService;

    public TestingResource(TestAlgorithmParametersService testAlgorithmParametersService, ProviderService providerService, ExcelExportService excelExportService) {
        this.testAlgorithmParametersService = testAlgorithmParametersService;
        this.providerService = providerService;
        this.excelExportService = excelExportService;
    }

    @PostMapping("/weights")
    public ResponseEntity<Void> testAlgoForWeights(@RequestParam("testCase") String testCase) {
        return testAlgorithmParametersService.testWeightValues(testCase);
    }

    @GetMapping("/providers-dump")
    public void dumpProviders() {
        excelExportService.exportProvidersToExcel(providerService.getAllProviders());
    }
}
