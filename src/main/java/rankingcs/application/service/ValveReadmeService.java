package rankingcs.application.service;

import org.springframework.stereotype.Service;
import rankingcs.adapter.gateway.GitHubReadmeGateway;
import rankingcs.infrastructure.persistence.ValveRankingEntity;
import rankingcs.infrastructure.persistence.ValveRankingRepository;
import rankingcs.infrastructure.repository.ReadmeRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValveReadmeService {

    private final GitHubReadmeGateway gitHubReadmeGateway;
    private final ReadmeRepository readmeRepository;
    private final ValveRankingRepository valveRankingRepository;

    public ValveReadmeService(GitHubReadmeGateway gitHubReadmeGateway, ValveRankingRepository valveRankingRepository, ReadmeRepository readmeRepository) {
        this.gitHubReadmeGateway = gitHubReadmeGateway;
        this.valveRankingRepository = valveRankingRepository;
        this.readmeRepository = readmeRepository;
    }

    public void saveReadmeFiles() {
        String repoUrl = "https://github.com/ValveSoftware/counter-strike_regional_standings";
        List<String> mdFiles = gitHubReadmeGateway.fetchMdFiles(repoUrl);
        for (String mdFileUrl : mdFiles) {
            String content = gitHubReadmeGateway.fetchReadme(mdFileUrl);
            saveRankingFromContent(content, mdFileUrl);
        }
    }

    private void saveRankingFromContent(String content, String detailsLink) {
        String[] lines = content.split("\n");

        boolean isParsing = false;
        for (String line : lines) {
            // Skip empty lines
            if (line.trim().isEmpty()) {
                continue;
            }

            // Detect the header row of the table
            if (!isParsing) {
                if (line.contains("| Standing") && line.contains("| Points") && line.contains("| Team Name") && line.contains("| Roster")) {
                    isParsing = true;
                    continue;
                }
            }

            // Skip lines that are just separators
            if (isParsing && line.matches("\\|\\s*:-+\\s*\\|\\s*-+:\\s*\\|\\s*:-+\\s*\\|\\s*:-+\\s*\\|.*")) {
                continue;
            }

            // Process data rows
            if (isParsing && line.startsWith("|") && line.split("\\|").length >= 5) {
                String[] columns = line.split("\\|");
                if (columns.length >= 5) {
                    try {
                        int standing = Integer.parseInt(columns[1].trim());
                        int points = Integer.parseInt(columns[2].trim());
                        String teamName = columns[3].trim();
                        List<String> roster = Arrays.asList(columns[4].trim().split(",\\s*"));
                        String details = extractDetailsLink(columns[5]);

                        ValveRankingEntity valveRankingEntity = new ValveRankingEntity(standing, points, teamName, roster, details);
                        valveRankingEntity.setCreatedDate(LocalDateTime.now());
                        valveRankingEntity.setModifiedDate(LocalDateTime.now());

                        valveRankingRepository.save(valveRankingEntity);
                    } catch (NumberFormatException e) {
                        // Handle parse error
                        System.err.println("Error parsing line: " + line);
                    }
                }
            }
        }
    }

    private String extractDetailsLink(String column) {
        Pattern pattern = Pattern.compile("\\[details\\]\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(column);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
