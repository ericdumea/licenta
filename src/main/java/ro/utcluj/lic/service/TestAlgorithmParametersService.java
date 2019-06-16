package ro.utcluj.lic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.service.dto.ExcelDataExportForAlgorithmDTO;
import ro.utcluj.lic.service.dto.ExcelExportWeightsDTO;
import ro.utcluj.lic.service.dto.WeightTestDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestAlgorithmParametersService {

    private final ExcelExportService excelExportService;

    private final FireflyImplementation fireflyImplementation;

    private static final Logger LOG = LoggerFactory.getLogger(TestAlgorithmParametersService.class);

    public TestAlgorithmParametersService(ExcelExportService excelExportService, FireflyImplementation fireflyImplementation) {
        this.excelExportService = excelExportService;
        this.fireflyImplementation = fireflyImplementation;
    }

    public ResponseEntity<Void> testParamsForAlgorithm() {

        Map<Integer, List<ExcelDataExportForAlgorithmDTO>> data = new HashMap<>();

        /// 10  - 300

        for (int fireflies = 10; fireflies < 200; fireflies += 10) {
            List<ExcelDataExportForAlgorithmDTO> exportDTOList = new ArrayList<>();

            //50 - 2000

            for (int nrIterations = 50; nrIterations < 300; nrIterations += 50) {
                ExcelDataExportForAlgorithmDTO exportDTO = new ExcelDataExportForAlgorithmDTO();
                exportDTO.setNumberOfIterations(nrIterations);

                //@Timed
                long timeSum = 0L;
                BigDecimal sum = BigDecimal.ZERO;

                for (int tests = 0; tests < 3; tests++) {
                    long timeBefore = System.currentTimeMillis();
                    sum = sum.add(fireflyImplementation.findFitnessValue(fireflies, nrIterations, "WIND", 20.0).getFitness());
                    timeSum += (System.currentTimeMillis() - timeBefore);
                }

                exportDTO.setTimingAverage(timeSum / 3.0);
                BigDecimal average = sum.abs().divide(BigDecimal.valueOf(3), 10, BigDecimal.ROUND_FLOOR);
                exportDTO.setFitnessAverage(average.doubleValue());
                exportDTOList.add(exportDTO);

                //LOG.info("{}   {}", average, timeSum/5.0);

            }
            data.put(fireflies, exportDTOList);
            LOG.info("Added entry for key: {} / 200", fireflies);
        }

        //excel insert
        LOG.info("Excel dump started");

        excelExportService.exportProviderDataAsPDF(data, 8);

        return ResponseEntity.ok().build();

    }

    public ResponseEntity<Void> testWeightValues(String testCase) {

        Map<Double, List<ExcelExportWeightsDTO>> data = new HashMap<>();

        for(double heterogeneityPenalty = 0.0; heterogeneityPenalty < 5.0; heterogeneityPenalty = heterogeneityPenalty + 0.1) {
            List<ExcelExportWeightsDTO> excelExportWeightsDTOS = new ArrayList<>();
            for(double pricePenalty = 0.0; pricePenalty < 0.1; pricePenalty += 0.005) {
                ExcelExportWeightsDTO exportWeightsDTO = new ExcelExportWeightsDTO();
                exportWeightsDTO.setPricePenalty(pricePenalty);

                long timeSum = 0L;
                BigDecimal sum = BigDecimal.ZERO;
                double averagePrice = 0.0;
                double averagePercentage = 0.0;

                for (int tests = 0; tests < 3; tests++) {
                    long timeBefore = System.currentTimeMillis();
                    WeightTestDTO weightTestDTO = fireflyImplementation.findFitnessValue(heterogeneityPenalty, pricePenalty, "WIND", 20.0);
                    sum = sum.add(weightTestDTO.getFitness());
                    timeSum += (System.currentTimeMillis() - timeBefore);
                    averagePercentage += weightTestDTO.getActualPercentage();
                    averagePrice += weightTestDTO.getActualPrice();
                }

                exportWeightsDTO.setTimingAverage(timeSum / 3.0);
                BigDecimal average = sum.abs().divide(BigDecimal.valueOf(3),10, BigDecimal.ROUND_FLOOR);
                exportWeightsDTO.setFitnessAverage(average.doubleValue());
                exportWeightsDTO.setActualPrice(averagePrice / 3.0);
                exportWeightsDTO.setActualPercentage(averagePercentage / 3.0);
                excelExportWeightsDTOS.add(exportWeightsDTO);
            }
            data.put(heterogeneityPenalty, excelExportWeightsDTOS);
            LOG.info("added entry for heterogeneity penalty weight : {}", heterogeneityPenalty);
        }

        LOG.info("Excel dump started for {}", testCase);

        excelExportService.exportDataForWeightTest(data, testCase);


        return ResponseEntity.ok().build();
    }
}
