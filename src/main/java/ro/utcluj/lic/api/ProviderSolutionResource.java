package ro.utcluj.lic.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.utcluj.lic.domain.ProviderSolution;
import ro.utcluj.lic.service.ProviderSolutionService;
import ro.utcluj.lic.service.dto.ResultsDTO;

@RestController
@RequestMapping("/api")
public class ProviderSolutionResource {

    private final ProviderSolutionService providerSolutionService;

    public ProviderSolutionResource(ProviderSolutionService providerSolutionService) {
        this.providerSolutionService = providerSolutionService;
    }

    @GetMapping("/get-solution/{id}")
    public ResultsDTO getResultsBySolutionIdString(@PathVariable String id) {

        return providerSolutionService.getResultsById(id);

    }

}
