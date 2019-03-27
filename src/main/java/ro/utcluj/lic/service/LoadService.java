package ro.utcluj.lic.service;

import javafx.util.converter.BigDecimalStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
public class LoadService {

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    public void loadConsumersFromFile() throws IOException {
        BufferedReader reader = null;
        BigDecimalStringConverter bigDecimalStringConverter = new BigDecimalStringConverter();

        try {
            // use buffered reader to read line by line
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(
                    "C:\\school\\Likenta\\licenta\\consumer.txt"))));

            List<BigDecimal> numbersRead = new ArrayList<>();
            String line = null;
            String[] numbers = null;
            // read line by line till end of file
            while ((line = reader.readLine()) != null) {
                int i = 0;
                numbers = line.split("\\d\\s+");
                Arrays.stream(numbers).forEach(s ->
                        numbersRead.add(bigDecimalStringConverter.fromString(s)));

                numbersRead.forEach(bigDecimal -> LOG.info(bigDecimal + " "));
                LOG.info(numbersRead.size() + " ");

            }
        } catch (Exception ignored) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.error("Exception: {} ", e.toString());
                }
            }
        }
    }

}
