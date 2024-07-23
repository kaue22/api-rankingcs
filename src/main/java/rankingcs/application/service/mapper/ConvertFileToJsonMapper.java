package rankingcs.application.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import rankingcs.application.domain.ReadmeDomain;
import rankingcs.application.domain.ValveRankingDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mapper
public interface ConvertFileToJsonMapper {
    ConvertFileToJsonMapper INSTANCE = Mappers.getMapper(ConvertFileToJsonMapper.class);

    @Mapping(target = "standing", source = "standing")
    @Mapping(target = "points", source = "points")
    @Mapping(target = "teamName", source = "teamName")
    @Mapping(target = "roster", source = "roster")
    @Mapping(target = "detailsLink", source = "detailsLink")
    ValveRankingDomain toValveRankingDomain(int standing, int points, String teamName, List<String> roster, String detailsLink);

    default List<ValveRankingDomain> convertContentFromString(ReadmeDomain readmeDomain) {
        List<ValveRankingDomain> rankings = new ArrayList<>();
        String content = readmeDomain.getContent();
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
                        String detailsLink = columns[5];

                        ValveRankingDomain rankingDomain = toValveRankingDomain(standing, points, teamName, roster, detailsLink);
                        rankings.add(rankingDomain);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line: " + line);
                    }
                }
            }
        }
        return rankings;
    }

//    default String extractDetailsLink(String column) {
//        Pattern pattern = Pattern.compile("\\[details\\]\\(([^)]+)\\)");
//        Matcher matcher = pattern.matcher(column);
//        if (matcher.find()) {
//            return matcher.group(1);
//        }
//        return null;
//    }
}