package rankingcs.adapter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rankingcs.application.usecase.SaveReadmeUseCase;

@RestController
public class ReadmeController {

    private final SaveReadmeUseCase saveReadmeUseCase;

    public ReadmeController(SaveReadmeUseCase saveReadmeUseCase) {
        this.saveReadmeUseCase = saveReadmeUseCase;
    }

    @GetMapping("/readme")
    public String saveReadme() {
        saveReadmeUseCase.execute();
        return "Arquivos .md salvos com sucesso!";
    }
}