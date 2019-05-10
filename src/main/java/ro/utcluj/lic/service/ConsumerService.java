package ro.utcluj.lic.service;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.Consumer;
import ro.utcluj.lic.repository.ConsumerRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConsumerService {

    private final ConsumerRepository consumerRepository;
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    public List<BigDecimal> loadConsumersFromFile() {
        try {
            return readFileAndExtractConsumers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<BigDecimal> readFileAndExtractConsumers() throws IOException {
        BufferedReader reader = null;
        List<BigDecimal> numbersRead = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(
                    "C:\\school\\Likenta\\licenta\\consumer.txt"))));
            String line;
            String[] numbers;
            while ((line = reader.readLine()) != null) {
                numbers = line.split("\\s+");
                Arrays.stream(numbers).forEach(s ->
                        numbersRead.add(new BigDecimal(s)));
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.error("Exception: {} ", e.toString());
                }
            }
        }
        return numbersRead;
    }

    public void batchInsertFromFile() {
        List<BigDecimal> consumersData = null;
        try {
            consumersData = readFileAndExtractConsumers();
        } catch (IOException e) {
            LOG.error("Consumers couldn't have been loaded from file.", e);
        }
        Consumer consumer = new Consumer();
        consumer.setId(new ObjectId());
        consumer.setPower(consumersData);

        consumerRepository.insert(consumer);
    }

    public List<Consumer> getAllConsumers() {
        return consumerRepository.findAll();
    }

}
