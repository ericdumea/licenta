package ro.utcluj.lic.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.utcluj.lic.service.LoadService;

import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/data")
public class MainResource {

    private final LoadService loadService;

    public MainResource(LoadService loadService) {
        this.loadService = loadService;
    }


    @GetMapping("/load-data")
    public ResponseEntity<Void> loadDataInDB(){
        try {
            loadService.loadConsumersFromFile();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return ok().build();
    }

}
