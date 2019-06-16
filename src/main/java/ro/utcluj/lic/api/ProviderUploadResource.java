package ro.utcluj.lic.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ro.utcluj.lic.service.FileService;
import ro.utcluj.lic.service.ProviderService;
import ro.utcluj.lic.service.dto.ProviderUploadDTO;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/provider-upload")
public class ProviderUploadResource {

    public static final Logger LOG = LoggerFactory.getLogger(ProviderUploadResource.class);

    private final FileService fileService;

    private final ProviderService providerService;

    public ProviderUploadResource(FileService fileService, ProviderService providerService) {
        this.fileService = fileService;
        this.providerService = providerService;
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file) {
        return fileService.storeFile(file);
    }

    @PostMapping("/insert")
    public ResponseEntity<Void> insertUploadedProvider(@RequestBody ProviderUploadDTO providerUploadDTO) {
        LOG.info(providerUploadDTO.toString());
        return providerService.insertUploadedProviders(providerUploadDTO);
    }
}
