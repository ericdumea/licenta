package ro.utcluj.lic.service;

import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.service.dto.ExcelDataExportDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelExportService {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelExportService.class);

    public void exportProviderDataAsPDF(Map<Integer, List<ExcelDataExportDTO>> exportDTOS, int fileNumber) {

        File file = new File("experiment-" + fileNumber + ".xlsx");

        try {
            PoiSpreadsheetBuilder.create(file).build(workbookDefinition ->
                    workbookDefinition.sheet("Sheet 1", sheet -> {
                        sheet.row(r -> {
                            r.cell("NoFireflies/NoIterations");
                            List<Integer> iterations = exportDTOS.get(110).stream().map(ExcelDataExportDTO::getNumberOfIterations).distinct().collect(Collectors.toList());
                            iterations.forEach(r::cell);
                        });

                        exportDTOS.forEach((key, value) -> sheet.row(r -> {
                            r.cell(key);
                            value.forEach(val -> r.cell(val.getFitnessAverage()));
                        }));
                    }));

        } catch (FileNotFoundException e) {
            LOG.error("Error creating file.", e);
        }
    }

}
