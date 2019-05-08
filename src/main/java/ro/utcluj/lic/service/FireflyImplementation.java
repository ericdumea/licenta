package ro.utcluj.lic.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.SimpleProvider;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private final BigDecimal PENALTY = new BigDecimal(2.25);
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private BigDecimal demandedEnergy;

    public FireflyImplementation(ConsumerService consumerService, ProviderService providerService) {
        this.consumerService = consumerService;
        this.providerService = providerService;
    }

    private BigDecimal fitness(List<SimpleProvider> sol, String type, Double percentage) {
        Optional<BigDecimal> sum = sol.stream()
                .filter(SimpleProvider::isFlag)
                .map(SimpleProvider::getEnergy)
                .reduce(BigDecimal::add);

        long numberOfProvidersOfType = sol.stream()
                .filter(simpleProvider -> StringUtils.equals(simpleProvider.getType(), type))
                .filter(SimpleProvider::isFlag)
                .count();

        long numberOfProvidersActivated = sol.stream()
                .filter(SimpleProvider::isFlag)
                .count();

        double actualPercentage = numberOfProvidersActivated != 0 ? (numberOfProvidersOfType * 100.0) / numberOfProvidersActivated : 0.0;

        BigDecimal penalty = PENALTY.multiply(new BigDecimal(1.0 - actualPercentage / 100.0).abs());

        BigDecimal fitnessValue = sum.orElse(BigDecimal.ZERO).subtract(demandedEnergy);

        //if(actualPercentage < percentage) {
        if (fitnessValue.compareTo(BigDecimal.ZERO) < 0) {
            fitnessValue = fitnessValue.subtract(penalty);
        } else {
            fitnessValue = fitnessValue.add(penalty);
        }
        //}
        return fitnessValue;
    }


    private BigDecimal fitness(List<SimpleProvider> sol) {
        Optional<BigDecimal> sum = sol.stream()
                .filter(SimpleProvider::isFlag)
                .map(SimpleProvider::getEnergy)
                .reduce(BigDecimal::add);
        return sum.orElse(BigDecimal.ZERO).subtract(demandedEnergy);
    }


    private SimpleProvider initializeSimpleProviderViaClone(SimpleProvider simpleProvider) {
        SimpleProvider copy = new SimpleProvider();
        copy.setFlag(false);
        copy.setEnergy(simpleProvider.getEnergy());
        copy.setId(simpleProvider.getId());
        copy.setType(simpleProvider.getType());
        return copy;
    }

    private SimpleProvider simpleProviderClone(SimpleProvider simpleProvider) {
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
            temporaryList.add(initializeSimpleProviderViaClone(provider));
        }
        temporaryList.get(ThreadLocalRandom.current().nextInt(0, temporaryList.size())).setFlag(true);
        temporaryList.get(ThreadLocalRandom.current().nextInt(0, temporaryList.size())).setFlag(true);
        return temporaryList;
    }

    private List<SimpleProvider> crossoverByNumberOfPoints(List<SimpleProvider> fsolI, List<SimpleProvider> fsolJ, long r, String type, Double percentage) {
        if (r == 0) {
            r++;
        }

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

        BigDecimal fitnessI = fitness(fsolI, type, percentage).abs();
        BigDecimal fitnessJ = fitness(fsolJ, type, percentage).abs();

        //LOG.info("(After Crossover) Fitness I: {}, Fitness J: {}", fitnessI, fitnessJ);

        return fitnessI.compareTo(fitnessJ) < 0 ? fsolI : fsolJ;
    }

    public List<SimpleProvider> doAlgorithm(int idx, int numberOfFireflies, int numberOfIterations, String type, Double percentage) {
        LOG.info("<<<< Started the algorithm.");
        List<SimpleProvider> bestSolutionByFitness = fireflyAlgorithm(numberOfFireflies, numberOfIterations, idx, type, percentage);
        LOG.info("Number of providers activated: {}", bestSolutionByFitness.stream().filter(SimpleProvider::isFlag).count());
        LOG.info("Final fitness value: {}", fitness(bestSolutionByFitness));
        long numberOfProvidersOfType = bestSolutionByFitness.stream()
                .filter(simpleProvider -> StringUtils.equals(simpleProvider.getType(), type))
                .filter(SimpleProvider::isFlag)
                .count();
        LOG.info("Number of providers of type : {} activated: {}", type, numberOfProvidersOfType);
        LOG.info("Percentage desired, minimum of {}, actual: {}", percentage, (numberOfProvidersOfType * 100.0) / bestSolutionByFitness.stream().filter(SimpleProvider::isFlag).count());
        return bestSolutionByFitness;
    }

    //method for testing the algorithm
    public BigDecimal findFitnessValue(int numberOfFireflies, int numberOfIterations, String type, Double percentage) {
        //LOG.info("<<<< Started the algorithm.");
        List<SimpleProvider> bestSolutionByFitness = fireflyAlgorithm(numberOfFireflies, numberOfIterations, 10, type, percentage);
        //LOG.info("Number of providers activated: {}", bestSolutionByFitness.stream().filter(SimpleProvider::isFlag).count());
        //LOG.info("Final fitness value: {}", fitness(bestSolutionByFitness));

        return fitness(bestSolutionByFitness, type, percentage);
    }

    private List<SimpleProvider> fireflyAlgorithm(int numberOfFireflies, int numberOfIterations, int i2, String type, Double percentage) {
        List<SimpleProvider> energyProductionSet = providerService.getAllProviders().stream()
                .map(SimpleProvider::new)
                .collect(Collectors.toList());
        demandedEnergy = consumerService.loadConsumersFromFile().get(i2);
        //LOG.info("Consumer {}, demanded energy: {}",i2, demandedEnergy);
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
                    BigDecimal fitnessI = fitness(finalSol.get(i), type, percentage);
                    BigDecimal fitnessJ = fitness(finalSol.get(j), type, percentage);

                    if (fitnessI.compareTo(fitnessJ) > 0) {
                        long r = fitnessI.subtract(fitnessJ).longValue();
                        //LOG.info("R: {}, iteration: {}", fitnessI.subtract(fitnessJ), iteration);
                        finalSol.set(i, crossoverByNumberOfPoints(finalSol.get(i), finalSol.get(j), (int) r, type, percentage));
                        mutation(finalSol.get(i), fitnessI);
                    }
                }
            }

            List<SimpleProvider> bestSolutionByFitnessList = getBestSolutionByFitness(finalSol, type, percentage);
            List<SimpleProvider> bestSolutionOfIteration = bestSolutionByFitnessList.stream().map(this::simpleProviderClone).collect(Collectors.toList());

            solBest.add(bestSolutionOfIteration);
            fitnessValue = fitness(bestSolutionByFitnessList, type, percentage);
            mutation(bestSolutionByFitnessList, fitnessValue);
            iteration++;

        } while (iteration < numberOfIterations); // (fitnessValue.compareTo(BigDecimal.valueOf(threshold)) < 0 && fitnessValue.compareTo(BigDecimal.valueOf(0 - threshold)) > 0) <- optim local
        return getBestSolutionByFitness(solBest, type, percentage);
    }

    private void mutation(List<SimpleProvider> providers, BigDecimal fitnessValue) {
        int index;

        if (providers.stream().noneMatch(SimpleProvider::isFlag)) {
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
    
    private List<SimpleProvider> getBestSolutionByFitness(List<List<SimpleProvider>> fSol, String type, Double percentage) {
        int choice = 0;
        BigDecimal minimal = fitness(fSol.get(0), type, percentage).abs();

        for (List<SimpleProvider> list : fSol) {
            BigDecimal fitnessValue = fitness(list, type, percentage).abs();
            if ((fitnessValue.compareTo(minimal)) < 0) {
                minimal = fitness(list, type, percentage);
                choice = fSol.indexOf(list);
            }
        }
        return fSol.get(choice);
    }
}
