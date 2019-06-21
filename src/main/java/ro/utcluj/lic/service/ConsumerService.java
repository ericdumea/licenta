package ro.utcluj.lic.service;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.Constants;
import ro.utcluj.lic.domain.Consumer;
import ro.utcluj.lic.repository.ConsumerRepository;
import ro.utcluj.lic.service.dto.ConsumerDTO;
import ro.utcluj.lic.service.dto.ConsumerUploadDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Service
public class ConsumerService {

    private final ConsumerRepository consumerRepository;
    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    public List<BigDecimal> loadConsumersFromFile() {
        try {
            return readFileAndExtractConsumers("C:\\school\\Likenta\\licenta\\consumer.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<Void> uploadConsumer(ConsumerUploadDTO consumerUploadDTO) {
        List<BigDecimal> consumerValues;
        try {
            consumerValues = readFileAndExtractConsumers(Constants.FILE_DIRECTORY + "\\" + consumerUploadDTO.getFileName());
        } catch (IOException e) {
            LOG.error("Error, consumer upload: ", e);
            return badRequest().build();
        }
        Consumer consumer = new Consumer();
        consumer.setName(consumerUploadDTO.getName());
        consumer.setPrice(consumerUploadDTO.getPrice());
        consumer.setPower(consumerValues);

        consumerRepository.insert(consumer);
        return ok().build();
    }

    private List<BigDecimal> readFileAndExtractConsumers(String path) throws IOException {
        BufferedReader reader = null;
        List<BigDecimal> numbersRead = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(
                    path))));
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
            consumersData = readFileAndExtractConsumers("C:\\school\\Likenta\\licenta\\consumer.txt");
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

    public List<ConsumerDTO> getAllConsumerDTOs() {
        return consumerRepository.findAll().stream().map(ConsumerDTO::new).collect(Collectors.toList());
    }

}
