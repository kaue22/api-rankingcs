package rankingcs.adapter.in.persistence;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rankingcs.adapter.in.gateway.GitHubReadmeGateway;
import rankingcs.adapter.in.persistence.entity.ReadmeEntity;
import rankingcs.adapter.in.persistence.repository.ReadmeRepository;
import rankingcs.adapter.in.utils.DateValidator;
import rankingcs.port.out.SaveRankingPortOut;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaveValveFileRepository implements SaveRankingPortOut {

    private final GitHubReadmeGateway gitHubReadmeGateway;
    private final ReadmeRepository readmeRepository;
    private final FindValveFileRepository findValveFileRepository;
    private final DateValidator dateValidator;
    private final String repoUrl;

    public SaveValveFileRepository(
            GitHubReadmeGateway gitHubReadmeGateway,
            ReadmeRepository readmeRepository,
            FindValveFileRepository findValveFileRepository,
            DateValidator dateValidator,
            @Value("${routes.valve}") String repoUrl) {
        this.gitHubReadmeGateway = gitHubReadmeGateway;
        this.readmeRepository = readmeRepository;
        this.findValveFileRepository = findValveFileRepository;
        this.dateValidator = dateValidator;
        this.repoUrl = repoUrl;
    }

    @Override
    public void saveReadmeFiles() {
        List<String> mdFiles = gitHubReadmeGateway.fetchMdFiles(repoUrl);

        // Process files in parallel and collect entities to be saved
        List<ReadmeEntity> readmeEntities = mdFiles.parallelStream()
                .map(mdFileUrl -> {
                    String content = gitHubReadmeGateway.fetchReadme(mdFileUrl);
                    if (content.startsWith("### Regional") || content.startsWith("### Standings")) {
                        String date = dateValidator.findDate(content);
                        if (findValveFileRepository.lastDateUpdate(date)) {
                            return null;
                        } else {
                            ReadmeEntity readmeEntity = new ReadmeEntity(content);
                            readmeEntity.setCreationDate(date);
                            return readmeEntity;
                        }
                    } else {
                        return new ReadmeEntity(content);
                    }
                })
                .filter(readmeEntity -> readmeEntity != null) // Filter out nulls
                .collect(Collectors.toList());

        // Batch insert entities
        readmeRepository.saveAll(readmeEntities);
    }
}
