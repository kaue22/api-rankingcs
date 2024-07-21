package rankingcs.adapter.in.persistence;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.gateway.GitHubReadmeGateway;
import rankingcs.adapter.in.persistence.dto.ReadmeEntity;
import rankingcs.adapter.in.persistence.repository.ReadmeRepository;
import rankingcs.port.out.SaveRankingPortOut;

import java.util.List;
@Component
public class SaveValveFileRepository implements SaveRankingPortOut {

    private final GitHubReadmeGateway gitHubReadmeGateway;
    private final ReadmeRepository readmeRepository;

    public SaveValveFileRepository(GitHubReadmeGateway gitHubReadmeGateway, ReadmeRepository readmeRepository) {
        this.gitHubReadmeGateway = gitHubReadmeGateway;
        this.readmeRepository = readmeRepository;
    }

    @Override
    public void saveReadmeFiles() {
        String repoUrl = "https://github.com/ValveSoftware/counter-strike_regional_standings";
        List<String> mdFiles = gitHubReadmeGateway.fetchMdFiles(repoUrl);
        for (String mdFileUrl : mdFiles) {
            String content = gitHubReadmeGateway.fetchReadme(mdFileUrl);
            ReadmeEntity readmeEntity = new ReadmeEntity(content);
            readmeRepository.save(readmeEntity);
        }
    }
}