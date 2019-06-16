package ro.utcluj.lic.service;

import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.utcluj.lic.domain.Provider;
import ro.utcluj.lic.service.dto.ExcelDataExportForAlgorithmDTO;
import ro.utcluj.lic.service.dto.ExcelExportWeightsDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExcelExportService {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelExportService.class);

    public void exportProvidersToExcel(List<Provider> providers) {
        File file = new File("providers-dump" + ".xlsx");

        AtomicInteger i = new AtomicInteger();
        try {
            PoiSpreadsheetBuilder.create(file).build(workbookDefinition ->
                    workbookDefinition.sheet("Sheet 1", sheet -> {
                        sheet.row(r -> {
                            r.cell("Id");
                            r.cell("Type");
                            r.cell("Energy");
                            r.cell("Price");
                        });

                        providers.forEach(provider -> {
                            sheet.row(row -> {
                                row.cell(i.getAndIncrement());
                                row.cell(provider.getType());
                                row.cell(provider.getEnergy().get(19));
                                row.cell(provider.getPrice());
                            });
                        });
                    }));

        } catch (FileNotFoundException e) {
            LOG.error("Error creating file.", e);
        }

    }

    public void exportProviderDataAsPDF(Map<Integer, List<ExcelDataExportForAlgorithmDTO>> exportDTOS, int fileNumber) {

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
                                value.forEach(excelDataExportForAlgorithmDTO -> {
                                    sheet.row(row -> {
                                        row.cell(key);
                                        row.cell(excelDataExportForAlgorithmDTO.getNumberOfIterations());
                                        row.cell(excelDataExportForAlgorithmDTO.getFitnessAverage());
                                        row.cell(excelDataExportForAlgorithmDTO.getTimingAverage());
                                    });
                                }));
                    }));

        } catch (FileNotFoundException e) {
            LOG.error("Error creating file.", e);
        }
    }

    public void exportDataForWeightTest(Map<Double, List<ExcelExportWeightsDTO>> data, String testCase) {

        File file = new File("experiment-" + testCase + ".xlsx");

        try {
            PoiSpreadsheetBuilder.create(file).build(workbookDefinition ->
                    workbookDefinition.sheet("Sheet 1", sheet -> {
                        sheet.row(r -> {
                            r.cell("Heterogeneity Weight");
                            r.cell("Price Weight");
                            r.cell("Av. Fitness");
                            r.cell("Av. Time");
                            r.cell("Actual Percentage");
                            r.cell("Actual Price");
                        });

                        data.forEach((key, value) ->
                                value.forEach(excelDataExportForAlgorithmDTO -> {
                                    sheet.row(row -> {
                                        row.cell(key);
                                        row.cell(excelDataExportForAlgorithmDTO.getPricePenalty());
                                        row.cell(excelDataExportForAlgorithmDTO.getFitnessAverage());
                                        row.cell(excelDataExportForAlgorithmDTO.getTimingAverage());
                                        row.cell(excelDataExportForAlgorithmDTO.getActualPercentage());
                                        row.cell(excelDataExportForAlgorithmDTO.getActualPrice());
                                    });
                                }));
                    }));

        } catch (FileNotFoundException e) {
            LOG.error("Error creating file.", e);
        }

    }
}
