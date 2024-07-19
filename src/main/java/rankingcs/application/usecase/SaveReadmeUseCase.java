package rankingcs.application.usecase;

import rankingcs.application.service.ReadmeService;
import org.springframework.stereotype.Service;

@Service
public class SaveReadmeUseCase {

    private final ReadmeService readmeService;

    public SaveReadmeUseCase(ReadmeService readmeService) {
        this.readmeService = readmeService;
    }

    public void execute() {
        readmeService.saveReadmeFiles();
    }
}
