package rankingcs.adapter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rankingcs.adapter.controller.dto.RankingDTO;
import rankingcs.infrastructure.persistence.ValveRankingEntity;
import rankingcs.infrastructure.persistence.ValveRankingRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/valve-rankings")
public class ValveRankingController {

    private final ValveRankingRepository rankingRepository;

    public ValveRankingController(ValveRankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    @GetMapping
    public List<RankingDTO> getAllRankings() {
        return rankingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/latest")
    public List<RankingDTO> getLatestRankings() {
        // Fetch the latest rankings, assuming you have logic to determine this
        return rankingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RankingDTO convertToDTO(ValveRankingEntity entity) {
        return new RankingDTO(
                entity.getStanding(),
                entity.getPoints(),
                entity.getTeamName(),
                entity.getRoster(),
                entity.getModifiedDate()
        );
    }
}