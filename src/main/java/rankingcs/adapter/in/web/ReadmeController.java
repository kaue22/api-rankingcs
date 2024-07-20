package rankingcs.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rankingcs.port.in.SaveReadmePortIn;

@RestController
public class ReadmeController {

    private final SaveReadmePortIn saveReadmePortIn;

    public ReadmeController(SaveReadmePortIn saveReadmePortIn) {
        this.saveReadmePortIn = saveReadmePortIn;
    }

    @GetMapping("/readme")
    public String saveReadme() {
        this.saveReadmePortIn.saveReadmeFiles();
        return "OK";
    }
}