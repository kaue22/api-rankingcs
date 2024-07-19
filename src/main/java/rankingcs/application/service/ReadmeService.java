package rankingcs.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rankingcs.adapter.gateway.GitHubReadmeGateway;
import rankingcs.infrastructure.persistence.ReadmeEntity;
import rankingcs.infrastructure.repository.ReadmeRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReadmeService {
    @Value("repourl.valve")
    private String urlValve;
    private final GitHubReadmeGateway gitHubReadmeGateway;
    private final ReadmeRepository readmeRepository;

    public ReadmeService(GitHubReadmeGateway gitHubReadmeGateway, ReadmeRepository readmeRepository) {
        this.gitHubReadmeGateway = gitHubReadmeGateway;
        this.readmeRepository = readmeRepository;
    }

    public void saveReadmeFiles() {
//        String repoUrl = "https://github.com/{ValveSoftware}/{counter-strike_regional_standings}"; // Substitua com a URL correta
        String repoUrl = "https://github.com/ValveSoftware/counter-strike_regional_standings"; // Substitua com a URL correta
        List<String> mdFiles = gitHubReadmeGateway.fetchMdFiles(repoUrl);
        for (String mdFileUrl : mdFiles) {
            String content = gitHubReadmeGateway.fetchReadme(mdFileUrl);
            ReadmeEntity readmeEntity = new ReadmeEntity(content);
            readmeEntity.setCreationDate(LocalDateTime.now());
            readmeRepository.save(readmeEntity);
        }
    }
}