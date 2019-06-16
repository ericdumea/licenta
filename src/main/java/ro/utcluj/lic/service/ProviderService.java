package ro.utcluj.lic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.Constants;
import ro.utcluj.lic.domain.Provider;
import ro.utcluj.lic.repository.ProviderRepository;
import ro.utcluj.lic.service.dto.ProviderUploadDTO;

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

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

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

    public ResponseEntity<Void> insertUploadedProviders(ProviderUploadDTO providerUploadDTO) {
        BufferedReader reader = null;
        List<Provider> providers = new ArrayList<>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(
                    Constants.FILE_DIRECTORY + "\\" + providerUploadDTO.getFileName()))));
            String line;
            String[] numbers;

            while ((line = reader.readLine()) != null) {
                numbers = line.split("\\s+");
                List<BigDecimal> finalNumbersRead = new ArrayList<>();
                Arrays.stream(numbers).forEach(s ->
                        finalNumbersRead.add(new BigDecimal(s)));
                Provider provider = createProvider(finalNumbersRead, providerUploadDTO.getPrice(), providerUploadDTO.getProviderType());
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
        if(!providers.isEmpty()) {
            providerRepository.insert(providers);
            return ok().build();
        }
        return badRequest().build();
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
                Provider provider = createRandomProvider(finalNumbersRead);
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

    private Provider createRandomProvider(List<BigDecimal> finalNumbersRead) {
        Provider provider = new Provider();
        provider.setEnergy(finalNumbersRead);
        provider.setFlag(false);
        provider.setType(PROVIDER_TYPES[ThreadLocalRandom.current().nextInt(0, 4)]);
        provider.setPrice(ThreadLocalRandom.current().nextDouble(0,100));
        return provider;
    }

    private Provider createProvider(List<BigDecimal> finalNumbersRead, Double price, String providerType) {
        Provider provider = new Provider();
        provider.setEnergy(finalNumbersRead);
        provider.setFlag(false);
        provider.setType(providerType);
        provider.setPrice(price);
        return provider;
    }


}
