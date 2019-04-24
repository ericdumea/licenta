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

import static java.lang.System.gc;

@Service
public class FireflyImplementation {

    private final ConsumerService consumerService;
    private final ProviderService providerService;


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
        return sum.orElse(BigDecimal.ZERO).subtract(demandedEnergy);
    }

    private SimpleProvider simpleProviderClone(SimpleProvider simpleProvider) {
        SimpleProvider copy = new SimpleProvider();
        copy.setFlag(false);
        copy.setEnergy(simpleProvider.getEnergy());
        copy.setId(simpleProvider.getId());
        copy.setType(simpleProvider.getType());
        return copy;
    }

    private SimpleProvider simpleProviderCloneWithoutFlag(SimpleProvider simpleProvider) {
        SimpleProvider copy = new SimpleProvider();
        copy.setFlag(simpleProvider.isFlag());
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
        temporaryList.get(ThreadLocalRandom.current().nextInt(0, temporaryList.size())).setFlag(true);
        temporaryList.get(ThreadLocalRandom.current().nextInt(0, temporaryList.size())).setFlag(true);
        return temporaryList;
    }

    private List<SimpleProvider> crossoverByNumberOfPoints(List<SimpleProvider> fsolI, List<SimpleProvider> fsolJ, long r) {
        if (r == 0) {
            r++;
        }

        //r = 5;
        //we assume that r = number of points to be crossover-ed

        //LOG.info("(After Crossover) Fitness I: {}, Fitness J: {}", fitness(fsolI), fitness(fsolJ));
        for (int i = 0; i < r; i++) {
            int idx = ThreadLocalRandom.current().nextInt(0, fsolI.size());
            SimpleProvider simpleProviderI = fsolI.get(idx);
            SimpleProvider simpleProviderJ = fsolJ.get(idx);
            boolean aux = simpleProviderI.isFlag();
            simpleProviderI.setFlag(simpleProviderJ.isFlag());
            simpleProviderJ.setFlag(aux);

            fsolI.set(idx, simpleProviderI);
            fsolJ.set(idx, simpleProviderJ);
        }

        BigDecimal fitnessI = fitness(fsolI).abs();
        BigDecimal fitnessJ = fitness(fsolJ).abs();

        //LOG.info("(After Crossover) Fitness I: {}, Fitness J: {}", fitnessI, fitnessJ);

        return fitnessI.compareTo(fitnessJ) < 0 ? fsolI : fsolJ;

    }

    private List<SimpleProvider> crossover(List<SimpleProvider> fsolI, List<SimpleProvider> fsolJ, int r) {

        List<SimpleProvider> list1 = new ArrayList<>();
        list1.addAll(fsolI.subList(0, r));
        list1.addAll(fsolJ.subList(r, fsolJ.size()));

        List<SimpleProvider> list2 = new ArrayList<>();
        list2.addAll(fsolJ.subList(0, r));
        list2.addAll(fsolI.subList(r, fsolI.size()));

        //LOG.info("LIST1: {}, LIST2: {}, CROSSOVER SIZE: {}", fsolI.size(), fsolJ.size(), list1.size());

        BigDecimal fitnessI = fitness(fsolI).abs();
        BigDecimal fitnessJ = fitness(fsolJ).abs();

        return fitnessI.compareTo(fitnessJ) < 0 ? list1 : list2;
    }


    public List<SimpleProvider> doAlgorithm(int idx, int numberOfFireflies, int numberOfIterations) {
        LOG.info("<<<< Started the algorithm.");
        List<SimpleProvider> energyProductionSet = providerService.getAllProviders().stream()
                .map(SimpleProvider::new)
                .collect(Collectors.toList());
        demandedEnergy = consumerService.loadConsumersFromFile().get(idx);
        List<List<SimpleProvider>> finalSol = new ArrayList<>();

        List<List<SimpleProvider>> solBest = new ArrayList<>();

        for (int i = 0; i < numberOfFireflies; i++) {
            finalSol.add(generateRandomSolution(energyProductionSet));
        }

        int iteration = 0;
        BigDecimal fitnessValue;

        do {
            for (int i = 0; i < numberOfFireflies; i++) {
                for (int j = i + 1; j < numberOfFireflies; j++) {
                    BigDecimal fitnessI = fitness(finalSol.get(i));
                    BigDecimal fitnessJ = fitness(finalSol.get(j));

                    if (fitnessI.compareTo(fitnessJ) > 0) {
                        long r = fitnessI.subtract(fitnessJ).longValue();

                        //LOG.info("R: {}, iteration: {}", fitnessI.subtract(fitnessJ), iteration);

                        finalSol.set(i, crossoverByNumberOfPoints(finalSol.get(i), finalSol.get(j), (int) r));

                        mutation(finalSol.get(i), fitnessI);
                    }
                }
            }

            List<SimpleProvider> bestSolutionByFitnessList = getBestSolutionByFitness(finalSol);
            List<SimpleProvider> bestSolutionOfIteration = bestSolutionByFitnessList.stream().map(this::simpleProviderCloneWithoutFlag).collect(Collectors.toList());

            solBest.add(bestSolutionOfIteration);
            fitnessValue = fitness(bestSolutionByFitnessList);
            mutation(bestSolutionByFitnessList, fitnessValue);
            iteration++;

        } while (iteration < numberOfIterations); // (fitnessValue.compareTo(BigDecimal.valueOf(threshold)) < 0 && fitnessValue.compareTo(BigDecimal.valueOf(0 - threshold)) > 0) <- optim local

        List<SimpleProvider> bestSolutionByFitness = getBestSolutionByFitness(solBest);
        LOG.info("Number of providers activated: {}", bestSolutionByFitness.stream().filter(SimpleProvider::isFlag).count());
        LOG.info("Final fitness value: {}", fitness(bestSolutionByFitness));

        return bestSolutionByFitness;
    }

    public BigDecimal findFitnessValue(int numberOfFireflies, int numberOfIterations) {
        //LOG.info("<<<< Started the algorithm.");
        List<SimpleProvider> energyProductionSet = providerService.getAllProviders().stream()
                .map(SimpleProvider::new)
                .collect(Collectors.toList());
        demandedEnergy = consumerService.loadConsumersFromFile().get(10);
        List<List<SimpleProvider>> finalSol = new ArrayList<>();

        List<List<SimpleProvider>> solBest = new ArrayList<>();

        for (int i = 0; i < numberOfFireflies; i++) {
            finalSol.add(generateRandomSolution(energyProductionSet));
        }

        int iteration = 0;
        BigDecimal fitnessValue;

        do {
            for (int i = 0; i < numberOfFireflies; i++) {
                for (int j = i + 1; j < numberOfFireflies; j++) {
                    BigDecimal fitnessI = fitness(finalSol.get(i));
                    BigDecimal fitnessJ = fitness(finalSol.get(j));

                    if (fitnessI.compareTo(fitnessJ) > 0) {
                        long r = fitnessI.subtract(fitnessJ).longValue();

                        //LOG.info("R: {}, iteration: {}", fitnessI.subtract(fitnessJ), iteration);

                        finalSol.set(i, crossoverByNumberOfPoints(finalSol.get(i), finalSol.get(j), (int) r));

                        mutation(finalSol.get(i), fitnessI);
                    }
                }
            }

            List<SimpleProvider> bestSolutionByFitnessList = getBestSolutionByFitness(finalSol);
            List<SimpleProvider> bestSolutionOfIteration = bestSolutionByFitnessList.stream().map(this::simpleProviderCloneWithoutFlag).collect(Collectors.toList());

            solBest.add(bestSolutionOfIteration);
            fitnessValue = fitness(bestSolutionByFitnessList);
            mutation(bestSolutionByFitnessList, fitnessValue);
            iteration++;

        } while (iteration < numberOfIterations); // (fitnessValue.compareTo(BigDecimal.valueOf(threshold)) < 0 && fitnessValue.compareTo(BigDecimal.valueOf(0 - threshold)) > 0) <- optim local

        List<SimpleProvider> bestSolutionByFitness = getBestSolutionByFitness(solBest);
        //LOG.info("Number of providers activated: {}", bestSolutionByFitness.stream().filter(SimpleProvider::isFlag).count());
        //LOG.info("Final fitness value: {}", fitness(bestSolutionByFitness));

        return fitness(bestSolutionByFitness);
    }

    private void mutation(List<SimpleProvider> providers, BigDecimal fitnessValue) {
        int index;

        if(providers.stream().noneMatch(SimpleProvider::isFlag)) {
            index = ThreadLocalRandom.current().nextInt(0, providers.size());
        } else {
            if (fitnessValue.compareTo(BigDecimal.ZERO) < 0) {
                do {
                    index = ThreadLocalRandom.current().nextInt(0, providers.size());
                } while (providers.get(index).isFlag());
            } else {
                do {
                    index = ThreadLocalRandom.current().nextInt(0, providers.size());
                } while (!providers.get(index).isFlag());
            }
        }
        providers.get(index).setFlag(!providers.get(index).isFlag());
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
        BigDecimal minimal = fitness(fSol.get(0)).abs();

        for (List<SimpleProvider> list : fSol) {
            BigDecimal fitnessValue = fitness(list).abs();
            if ((fitnessValue.compareTo(minimal)) < 0) {
                minimal = fitness(list);
                choice = fSol.indexOf(list);
            }
        }
        return fSol.get(choice);
    }
}
