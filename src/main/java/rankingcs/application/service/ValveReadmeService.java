package rankingcs.application.service;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.gateway.GitHubReadmeGateway;
import rankingcs.adapter.out.persistence.ValveRankingRepository;
import rankingcs.adapter.out.persistence.entity.ValveRankingEntity;
import rankingcs.port.in.SaveReadmePortIn;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValveReadmeService implements SaveReadmePortIn {

    private final GitHubReadmeGateway gitHubReadmeGateway;
    private final ValveRankingRepository valveRankingRepository;

    public ValveReadmeService(GitHubReadmeGateway gitHubReadmeGateway, ValveRankingRepository valveRankingRepository) {
        this.gitHubReadmeGateway = gitHubReadmeGateway;
        this.valveRankingRepository = valveRankingRepository;
    }

    @Override
    public String saveReadmeFiles() {
        String repoUrl = "https://github.com/ValveSoftware/counter-strike_regional_standings";
        List<String> mdFiles = gitHubReadmeGateway.fetchMdFiles(repoUrl);
        for (String mdFileUrl : mdFiles) {
            String content = gitHubReadmeGateway.fetchReadme(mdFileUrl);
            saveRankingFromContent(content, mdFileUrl);
        }
        return "N-Ok";
    }


    private void saveRankingFromContent(String content, String detailsLink) {
        String[] lines = content.split("\n");

        boolean isParsing = false;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            if (!isParsing) {
                if (line.contains("| Standing") && line.contains("| Points") && line.contains("| Team Name") && line.contains("| Roster")) {
                    isParsing = true;
                    continue;
                }
            }

            if (isParsing && line.matches("\\|\\s*:-+\\s*\\|\\s*-+:\\s*\\|\\s*:-+\\s*\\|\\s*:-+\\s*\\|.*")) {
                continue;
            }

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
