package ro.utcluj.lic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.Provider;
import ro.utcluj.lic.repository.ProviderRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;

    private final String[] PROVIDER_TYPES = {"WIND", "SOLAR", "TIDE", "TRADITIONAL"};

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    public List<Provider> insertProviders() {

        List<Provider> providers = readProvidersFromFile();
        return providerRepository.insert(providers);

    }

    private List<Provider> readProvidersFromFile() {
        BufferedReader reader = null;
        List<Provider> providers = new ArrayList<>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(
                    "C:\\school\\Likenta\\licenta\\providers.txt"))));
            String line;
            String[] numbers;

            while ((line = reader.readLine()) != null) {
                numbers = line.split("\\s+");
                List<BigDecimal> finalNumbersRead = new ArrayList<>();
                Arrays.stream(numbers).forEach(s ->
                        finalNumbersRead.add(new BigDecimal(s)));
                Provider provider = createProvider(finalNumbersRead);
                providers.add(provider);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.error("Exception: {} ", e.toString());
                }
            }
        }
        return providers;
    }

    private Provider createProvider(List<BigDecimal> finalNumbersRead) {
        Provider provider = new Provider();
        provider.setEnergy(finalNumbersRead);
        provider.setFlag(false);
        provider.setType(PROVIDER_TYPES[ThreadLocalRandom.current().nextInt(0, 4)]);
        provider.setPrice(ThreadLocalRandom.current().nextDouble(0,100));
        return provider;
    }


}
