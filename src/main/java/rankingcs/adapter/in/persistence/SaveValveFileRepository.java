package rankingcs.adapter.in.persistence;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.gateway.GitHubReadmeGateway;
import rankingcs.adapter.in.persistence.dto.ReadmeEntity;
import rankingcs.adapter.in.persistence.repository.ReadmeRepository;
import rankingcs.adapter.in.utils.DateValidator;
import rankingcs.port.out.SaveRankingPortOut;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaveValveFileRepository implements SaveRankingPortOut {

    private final GitHubReadmeGateway gitHubReadmeGateway;
    private final ReadmeRepository readmeRepository;
    private final FindValveFileRepository findValveFileRepository;
    private final DateValidator dateValidator;

    public SaveValveFileRepository(GitHubReadmeGateway gitHubReadmeGateway, ReadmeRepository readmeRepository, FindValveFileRepository findValveFileRepository, DateValidator dateValidator) {
        this.gitHubReadmeGateway = gitHubReadmeGateway;
        this.readmeRepository = readmeRepository;
        this.findValveFileRepository = findValveFileRepository;
        this.dateValidator = dateValidator;
    }

    @Override
    public void saveReadmeFiles() {
        String repoUrl = "https://github.com/ValveSoftware/counter-strike_regional_standings";
        List<String> mdFiles = gitHubReadmeGateway.fetchMdFiles(repoUrl);
        for (String mdFileUrl : mdFiles) {
            String content = gitHubReadmeGateway.fetchReadme(mdFileUrl);
            ReadmeEntity readmeEntity = new ReadmeEntity(content);
            if (content.startsWith("### Regional")) {

              // Divida a string em partes, separando pelos espaços e caracteres não numéricos
//                String[] partes = content.split("\\D+");
//
//                String dateConverter = Arrays.stream(partes)
//                        .filter(parte -> !parte.isEmpty())
//                        .peek(numero -> System.out.println("Número encontrado: " + numero))
//                        .collect(Collectors.joining(", "));

                var date = dateValidator.findDate(content);
                findValveFileRepository.lastDateUpdate(date);
            }
            readmeRepository.save(readmeEntity);
        }

    }
}