//package rankingcs.adapter.out.web;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import rankingcs.adapter.out.web.dto.RankingDTO;
//import rankingcs.adapter.in.persistence.valverepository.entity.ValveRankingEntity;
//import rankingcs.adapter.out.persistence.ValveRankingRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/valve-rankings")
//public class ValveRankingController{
//
//    private final ValveRankingRepository rankingRepository;
//
//    public ValveRankingController(ValveRankingRepository rankingRepository) {
//        this.rankingRepository = rankingRepository;
//    }
//
//    @GetMapping
//    public List<RankingDTO> getAllRankings() {
//        return rankingRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @GetMapping("/latest")
//    public List<RankingDTO> getLatestRankings() {
//        return rankingRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    public RankingDTO convertToDTO(ValveRankingEntity entity) {
//        return new RankingDTO(entity.getStanding(), entity.getPoints(), entity.getTeamName(), entity.getRoster(), entity.getModifiedDate());
//    }
//
//
//}