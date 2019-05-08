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

@Service
public class ExcelExportService {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelExportService.class);

    public void exportProviderDataAsPDF(Map<Integer, List<ExcelDataExportDTO>> exportDTOS, int fileNumber) {

        File file = new File("experiment-" + fileNumber + ".xlsx");

        try {
            PoiSpreadsheetBuilder.create(file).build(workbookDefinition ->
                    workbookDefinition.sheet("Sheet 1", sheet -> {
                        sheet.row(r -> {
                            r.cell("NoFireflies");
                            r.cell("NoIterations");
                            r.cell("Av. Fitness");
                            r.cell("Av. Time");
                        });

                        exportDTOS.forEach((key, value) ->
                                value.forEach(excelDataExportDTO -> {
                                    sheet.row(row -> {
                                        row.cell(key);
                                        row.cell(excelDataExportDTO.getNumberOfIterations());
                                        row.cell(excelDataExportDTO.getFitnessAverage());
                                        row.cell(excelDataExportDTO.getTimingAverage());
                                    });
                                }));
                    }));

        } catch (FileNotFoundException e) {
            LOG.error("Error creating file.", e);
        }
    }

}
