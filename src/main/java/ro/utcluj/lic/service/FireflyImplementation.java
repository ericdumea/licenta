package ro.utcluj.lic.service;

import ro.utcluj.lic.domain.SimpleProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class FireflyImplementation {

    private final ConsumerService consumerService;

    private final ProviderService providerService;

    private final int SOLUTION_NUMBER = 100;

    private BigDecimal demandedEnergy;

    public FireflyImplementation(ConsumerService consumerService, ProviderService providerService) {
        this.consumerService = consumerService;
        this.providerService = providerService;
    }

    private BigDecimal fitness(SimpleProvider sol) {
        return sol.getEnergy().subtract(demandedEnergy);
    }

    private List<SimpleProvider> generateRandomSolution(List<SimpleProvider> providers) {

    }

    public List<SimpleProvider> doAlgorithm() {
        List<SimpleProvider> energyProductionSet = providerService.getAllProviders().stream()
                    .map(SimpleProvider::new)
                    .collect(Collectors.toList());

        demandedEnergy = consumerService.loadConsumersFromFile().get(0);

        List<SimpleProvider> temporaryList = new ArrayList<>();

        Iterator<SimpleProvider> iterator = ((ArrayList) energyProductionSet).iterator();
        while (iterator.hasNext()) {
            temporaryList.add((SimpleProvider) iterator.next().clone())
        }

        List<List<SimpleProvider>> finalSol = new ArrayList<>();

        for (int i = 0; i < SOLUTION_NUMBER; i++) {
            finalSol.add()
        }


        return null;
    }

}
