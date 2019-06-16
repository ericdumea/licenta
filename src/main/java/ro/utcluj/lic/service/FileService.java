package ro.utcluj.lic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.utcluj.lic.domain.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import static org.springframework.http.ResponseEntity.badRequest;

@Service
public class FileService {

    public static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public ResponseEntity<Void> storeFile(MultipartFile file) {

        String[] fileName = file.getOriginalFilename().split(".");
        Arrays.stream(fileName).forEach(stri-> LOGGER.info("FILE: {}", stri));

        Path filePath = Paths.get(Constants.FILE_DIRECTORY + "\\" + file.getOriginalFilename());

        LOGGER.info("The file will be added: {}", file.getOriginalFilename());

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Error in file upload", e);
            return badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
