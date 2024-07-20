package rankingcs.application.usecase;

import rankingcs.application.service.ValveReadmeService;
import org.springframework.stereotype.Service;

@Service
public class SaveReadmeUseCase {

    private final ValveReadmeService valveReadmeService;

    public SaveReadmeUseCase(ValveReadmeService valveReadmeService) {
        this.valveReadmeService = valveReadmeService;
    }

    public void execute() {
        valveReadmeService.saveReadmeFiles();
    }
}
