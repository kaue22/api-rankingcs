package rankingcs.adapter.in.persistence;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.gateway.GitHubReadmeGateway;
import rankingcs.adapter.in.persistence.entity.ReadmeEntity;
import rankingcs.adapter.in.persistence.repository.ReadmeRepository;
import rankingcs.adapter.in.utils.DateValidator;
import rankingcs.port.out.SaveRankingPortOut;

import java.util.List;

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
        for (String mdFileUrl : mdFiles) { //refatorar
            String content = gitHubReadmeGateway.fetchReadme(mdFileUrl);
            ReadmeEntity readmeEntity = new ReadmeEntity(content);
            if (content.startsWith("### Regional")) {
                var date = dateValidator.findDate(content);
                findValveFileRepository.lastDateUpdate(date);
            }

//            if(date.equals(readmeEntity.getCreationDate()))
            readmeRepository.save(readmeEntity);
        }

    }
}