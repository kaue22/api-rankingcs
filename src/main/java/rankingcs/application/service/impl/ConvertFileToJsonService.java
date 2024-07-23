package rankingcs.application.service.impl;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.persistence.valverepository.entity.ValveRankingEntity;
import rankingcs.application.domain.ValveRankingDomain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConvertFileToJsonService {

    public void saveRankingFromContent(String content) {
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

                        ValveRankingDomain valveRankingDomain = new ValveRankingDomain(standing, points, teamName, roster, details);
                        System.out.println(valveRankingDomain);
                        // valveRankingRepository.save(valveRankingEntity);
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
