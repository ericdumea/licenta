package ro.utcluj.lic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.Consumer;
import ro.utcluj.lic.domain.Provider;
import ro.utcluj.lic.domain.ProviderType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class FireflyImplementation {

    private final ConsumerService consumerService;
    private final ProviderService providerService;
    private final BigDecimal PENALTY = new BigDecimal(2.2);
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private Consumer consumer;

    public FireflyImplementation(ConsumerService consumerService, ProviderService providerService) {
        this.consumerService = consumerService;
        this.providerService = providerService;
    }

    private BigDecimal fitness(List<Provider> sol, List<ProviderType> providerTypes) {
        Optional<BigDecimal> sum = sol.stream()
                .filter(Provider::isFlag)
                .map(Provider::getEnergy)
                .map(list -> list.get(0))
                .reduce(BigDecimal::add);

        long numberOfProvidersOfType = sol.stream()
                //.filter(simpleProvider -> StringUtils.equals(simpleProvider.getType(), type))
                .filter(Provider::isFlag)
                .count();

        long numberOfProvidersActivated = sol.stream()
                .filter(Provider::isFlag)
                .count();

        double actualPercentage = numberOfProvidersActivated != 0 ? (numberOfProvidersOfType * 100.0) / numberOfProvidersActivated : 0.0;

        BigDecimal penalty = PENALTY.multiply(new BigDecimal(1.0 - actualPercentage / 100.0).abs());

        BigDecimal energyFitnessValue = consumer.getPower().get(0).subtract(sum.orElse(BigDecimal.ZERO)).abs();

        //if (actualPercentage < percentage) {
            if (energyFitnessValue.compareTo(BigDecimal.ZERO) < 0) {
                energyFitnessValue = energyFitnessValue.subtract(penalty);
            } else {
                energyFitnessValue = energyFitnessValue.add(penalty);
            }
       // }
        return energyFitnessValue;
    }

    private BigDecimal fitnessByEnergy(List<Provider> sol) {
        Optional<BigDecimal> sum = sol.stream()
                .filter(Provider::isFlag)
                .map(Provider::getEnergy)
                .map(list -> list.get(0))
                .reduce(BigDecimal::add);
        return sum.orElse(BigDecimal.ZERO).subtract(consumer.getPower().get(0));
    }

    private List<Provider> generateRandomSolution(List<Provider> providers) {
        List<Provider> temporaryList = new ArrayList<>();
        for (Provider provider : providers) {
            temporaryList.add(initializeSimpleProviderViaClone(provider));
        }
        temporaryList.get(ThreadLocalRandom.current().nextInt(0, temporaryList.size())).setFlag(true);
        temporaryList.get(ThreadLocalRandom.current().nextInt(0, temporaryList.size())).setFlag(true);
        return temporaryList;
    }

    private List<Provider> crossoverByNumberOfPoints(List<Provider> fsolI, List<Provider> fsolJ, long r, List<ProviderType> providerTypes) {
        if (r == 0) {
            r++;
        }

        //LOG.info("(After Crossover) Fitness I: {}, Fitness J: {}", fitnessByEnergy(fsolI), fitnessByEnergy(fsolJ));
        for (int i = 0; i < r; i++) {
            int idx = ThreadLocalRandom.current().nextInt(0, fsolI.size());
            Provider providerI = fsolI.get(idx);
            Provider providerJ = fsolJ.get(idx);
            boolean aux = providerI.isFlag();
            providerI.setFlag(providerJ.isFlag());
            providerJ.setFlag(aux);

            fsolI.set(idx, providerI);
            fsolJ.set(idx, providerJ);
        }

        BigDecimal fitnessI = fitness(fsolI, providerTypes).abs();
        BigDecimal fitnessJ = fitness(fsolJ, providerTypes).abs();

        //LOG.info("(After Crossover) Fitness I: {}, Fitness J: {}", fitnessI, fitnessJ);

        return fitnessI.compareTo(fitnessJ) < 0 ? fsolI : fsolJ;
    }

    public List<Provider> doAlgorithm(int idx, int numberOfFireflies, int numberOfIterations, List<ProviderType> providerTypes) {
        LOG.info("<<<< Started the algorithm.");
        List<Provider> bestSolutionByFitness = fireflyAlgorithm(numberOfFireflies, numberOfIterations, idx, providerTypes);
        LOG.info("Number of providers activated: {}", bestSolutionByFitness.stream().filter(Provider::isFlag).count());
        LOG.info("Final fitnessByEnergy value: {}", fitnessByEnergy(bestSolutionByFitness));
//        long numberOfProvidersOfType = bestSolutionByFitness.stream()
//                .filter(simpleProvider -> StringUtils.equals(simpleProvider.getType(), type))
//                .filter(SimpleProvider::isFlag)
//                .count();
//        LOG.info("Number of providers of type : {} activated: {}", type, numberOfProvidersOfType);
//        LOG.info("Percentage desired, minimum of {}, actual: {}", percentage, (numberOfProvidersOfType * 100.0) / bestSolutionByFitness.stream().filter(SimpleProvider::isFlag).count());
        return bestSolutionByFitness;
    }

    //method for testing the algorithm

    public BigDecimal findFitnessValue(int numberOfFireflies, int numberOfIterations, String type, Double percentage) {
        //LOG.info("<<<< Started the algorithm.");
        List<Provider> bestSolutionByFitness = fireflyAlgorithm(numberOfFireflies, numberOfIterations, 10, new ArrayList<ProviderType>());
        //LOG.info("Number of providers activated: {}", bestSolutionByFitness.stream().filter(SimpleProvider::isFlag).count());
        //LOG.info("Final fitnessByEnergy value: {}", fitnessByEnergy(bestSolutionByFitness));

        return fitness(bestSolutionByFitness, new ArrayList<>());
    }
    private List<Provider> fireflyAlgorithm(int numberOfFireflies, int numberOfIterations, int idx, List<ProviderType> providerTypes) {

        List<Provider> energyProductionSet = providerService.getAllProviders();

        consumer = Optional.ofNullable(consumerService.getAllConsumers().get(0)).orElseThrow( () ->  {
            LOG.error("No consumers found in database");
            return new IllegalArgumentException();
        });

        List<List<Provider>> finalSol = new ArrayList<>();

        List<List<Provider>> solBest = new ArrayList<>();

        for (int i = 0; i < numberOfFireflies; i++) {
            finalSol.add(generateRandomSolution(energyProductionSet));
        }

        int iteration = 0;
        BigDecimal fitnessValue;

        do {
            for (int i = 0; i < numberOfFireflies; i++) {
                for (int j = i + 1; j < numberOfFireflies; j++) {
                    BigDecimal fitnessI = fitness(finalSol.get(i), providerTypes);
                    BigDecimal fitnessJ = fitness(finalSol.get(j), providerTypes);

                    if (fitnessI.compareTo(fitnessJ) > 0) {
                        long r = fitnessI.subtract(fitnessJ).longValue();
                        //LOG.info("R: {}, iteration: {}", fitnessI.subtract(fitnessJ), iteration);
                        finalSol.set(i, crossoverByNumberOfPoints(finalSol.get(i), finalSol.get(j), (int) r, providerTypes));
                        mutation(finalSol.get(i), fitnessI);
                    }
                }
            }

            List<Provider> bestSolutionByFitnessList = getBestSolutionByFitness(finalSol, providerTypes);
            List<Provider> bestSolutionOfIteration = bestSolutionByFitnessList.stream().map(this::simpleProviderClone).collect(Collectors.toList());

            solBest.add(bestSolutionOfIteration);
            fitnessValue = fitness(bestSolutionByFitnessList, providerTypes);
            mutation(bestSolutionByFitnessList, fitnessValue);
            iteration++;

        } while (iteration < numberOfIterations); // (fitnessValue.compareTo(BigDecimal.valueOf(threshold)) < 0 && fitnessValue.compareTo(BigDecimal.valueOf(0 - threshold)) > 0) <- optim local
        return getBestSolutionByFitness(solBest, providerTypes);
    }

    private void mutation(List<Provider> providers, BigDecimal fitnessValue) {
        int index;

        if (providers.stream().noneMatch(Provider::isFlag)) {
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

    private Provider initializeSimpleProviderViaClone(Provider provider) {
        Provider copy = new Provider();
        copy.setFlag(false);
        copy.setEnergy(provider.getEnergy());
        copy.setId(provider.getId());
        copy.setType(provider.getType());
        return copy;
    }

    private Provider simpleProviderClone(Provider provider) {
        Provider copy = new Provider();
        copy.setFlag(provider.isFlag());
        copy.setEnergy(provider.getEnergy());
        copy.setId(provider.getId());
        copy.setType(provider.getType());
        return copy;
    }

    private List<Provider> getBestSolutionByFitness(List<List<Provider>> fSol, List<ProviderType> providerTypes) {
        int choice = 0;
        BigDecimal minimal = fitness(fSol.get(0), providerTypes).abs();

        for (List<Provider> list : fSol) {
            BigDecimal fitnessValue = fitness(list, providerTypes).abs();
            if ((fitnessValue.compareTo(minimal)) < 0) {
                minimal = fitness(list, providerTypes);
                choice = fSol.indexOf(list);
            }
        }
        return fSol.get(choice);
    }
}
