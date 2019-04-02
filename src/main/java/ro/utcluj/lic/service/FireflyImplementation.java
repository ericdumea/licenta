package ro.utcluj.lic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.SimpleProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class FireflyImplementation {

    private final ConsumerService consumerService;
    private final ProviderService providerService;
    private final int NO_F = 20;
    private final int NUMBER_OF_ITERATIONS = 100;
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private BigDecimal demandedEnergy;

    public FireflyImplementation(ConsumerService consumerService, ProviderService providerService) {
        this.consumerService = consumerService;
        this.providerService = providerService;
    }

    private BigDecimal fitness(List<SimpleProvider> sol) {
        Optional<BigDecimal> sum = sol.stream()
                .filter(SimpleProvider::isFlag)
                .map(SimpleProvider::getEnergy)
                .reduce(BigDecimal::add);
        return sum.orElseThrow(() -> new RuntimeException("Not allowed to do the op")).subtract(demandedEnergy);
    }

    private SimpleProvider simpleProviderClone(SimpleProvider simpleProvider) {
        SimpleProvider copy = new SimpleProvider();
        copy.setFlag(ThreadLocalRandom.current().nextBoolean());
        copy.setEnergy(simpleProvider.getEnergy());
        copy.setId(simpleProvider.getId());
        copy.setType(simpleProvider.getType());
        return copy;
    }

    private List<SimpleProvider> generateRandomSolution(List<SimpleProvider> providers) {
        List<SimpleProvider> temporaryList = new ArrayList<>();
        for (SimpleProvider provider : providers) {
            temporaryList.add(simpleProviderClone(provider));
        }
        return temporaryList;
    }

    private List<SimpleProvider> crossover(List<SimpleProvider> fsolI, List<SimpleProvider> fsolJ, int r) {

        List<SimpleProvider> list1 = new ArrayList<>();
        list1.addAll(fsolI.subList(0, r));
        list1.addAll(fsolJ.subList(r, fsolJ.size()));

        List<SimpleProvider> list2 = new ArrayList<>();
        list2.addAll(fsolJ.subList(0, r));
        list2.addAll(fsolI.subList(r, fsolI.size()));

        //LOG.info("LIST1: {}, LIST2: {}, CROSSOVER SIZE: {}", fsolI.size(), fsolJ.size(), list1.size());

        return getBestSolutionByFitness(Arrays.asList(list1, list2));
    }

    public List<SimpleProvider> doAlgorithm() {
        List<SimpleProvider> energyProductionSet = providerService.getAllProviders().stream()
                .map(SimpleProvider::new)
                .collect(Collectors.toList());

        demandedEnergy = consumerService.loadConsumersFromFile().get(0);

        List<List<SimpleProvider>> finalSol = new ArrayList<>();

        List<List<SimpleProvider>> solBest = new ArrayList<>();

        for (int i = 0; i < NO_F; i++) {
            finalSol.add(generateRandomSolution(energyProductionSet));
        }

        int iteration = 0;

        do {
            for (int i = 0; i < NO_F; i++) {
                for (int j = i + 1; j < NO_F; j++) {

                    BigDecimal fitnessI = fitness(finalSol.get(i));
                    BigDecimal fitnessJ = fitness(finalSol.get(j));

                    //LOG.info("{}   {}  ", fitnessI, fitnessJ);

                    if (fitnessI.compareTo(fitnessJ) > 0) {
                        int r = fitnessI.subtract(fitnessJ).intValue();
                        //LOG.info("R: {}, iteration: {}", r, iteration);

                        finalSol.set(i, crossover(finalSol.get(i), finalSol.get(j), r));
                        mutation(finalSol.get(i));

                    }
                }
            }
            List<SimpleProvider> bestSolutionOfIteration = getBestSolutionByFitness(finalSol);
            solBest.add(bestSolutionOfIteration);
            mutation(bestSolutionOfIteration);
            iteration++;
            LOG.info("{}", iteration);
        } while (iteration < NUMBER_OF_ITERATIONS);

        return getBestSolutionByFitness(solBest);
    }


    private List<SimpleProvider> getBestSolution(List<List<SimpleProvider>> fSol) {
        int choice = 0, countGoodProviders = 0;
        for (List<SimpleProvider> list : fSol) {

            Map<String, List<SimpleProvider>> simpleProviders = list.stream()
                    .filter(SimpleProvider::isFlag)
                    .collect(Collectors.groupingBy(SimpleProvider::getType));

            Map<String, Integer> integerMap = new HashMap<>();
            for (Map.Entry<String, List<SimpleProvider>> stringListEntry : simpleProviders.entrySet()) {
                integerMap.put(stringListEntry.getKey(), stringListEntry.getValue().size());
            }
        }
        return fSol.get(choice);
    }

    private List<SimpleProvider> getBestSolutionByFitness(List<List<SimpleProvider>> fSol) {
        int choice = 0;

        BigDecimal minimal = new BigDecimal(0.0);

        for (List<SimpleProvider> list : fSol) {
            if (minimal.compareTo(fitness(list)) < 0) {
                choice = fSol.indexOf(list);
            }
        }

        return fSol.get(choice);
    }

    private void mutation(List<SimpleProvider> providers) {
        int index = ThreadLocalRandom.current().nextInt(0, providers.size());
        providers.get(index).setFlag(!providers.get(index).isFlag());
    }

}
