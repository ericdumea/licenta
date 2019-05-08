package ro.utcluj.lic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.service.dto.ExcelDataExportDTO;

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

    public ResponseEntity<Void> testAlgorithm() {

        Map<Integer, List<ExcelDataExportDTO>> data = new HashMap<>();

        /// 10  - 300

        for (int fireflies = 10; fireflies < 250; fireflies += 10) {
            List<ExcelDataExportDTO> exportDTOList = new ArrayList<>();

            //50 - 2000

            for (int nrIterations = 50; nrIterations < 1000; nrIterations += 50) {
                ExcelDataExportDTO exportDTO = new ExcelDataExportDTO();
                exportDTO.setNumberOfIterations(nrIterations);

                //@Timed
                long timeSum = 0L;
                BigDecimal sum = BigDecimal.ZERO;

                for (int tests = 0; tests < 3; tests++) {
                    long timeBefore = System.currentTimeMillis();
                    sum = sum.add(fireflyImplementation.findFitnessValue(fireflies, nrIterations, "WIND", 20.0));
                    timeSum += (System.currentTimeMillis() - timeBefore);
                }

                exportDTO.setTimingAverage(timeSum / 3.0);
                BigDecimal average = sum.abs().divide(BigDecimal.valueOf(3), BigDecimal.ROUND_FLOOR);
                exportDTO.setFitnessAverage(average.doubleValue());
                exportDTOList.add(exportDTO);

                //LOG.info("{}   {}", average, timeSum/5.0);

            }
            data.put(fireflies, exportDTOList);
            LOG.info("Added entry for key: {} / 250", fireflies);
        }

        //excel insert
        LOG.info("Excel dump started");

        excelExportService.exportProviderDataAsPDF(data, 5);

        return ResponseEntity.ok().build();

    }
}
