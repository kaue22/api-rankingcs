package rankingcs.adapter.out.web.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import rankingcs.adapter.out.web.dto.ValveRankingDto;

import java.util.List;

@FeignClient(name = "valveRankingClient", url = "${api.valve-ranking.url}")
public interface ValveRankingFeignClient {

    @PostMapping("/valve-rankings")
    String sendRankingData(@RequestHeader("Authorization") String token, @RequestBody ValveRankingDto[] rankingDtos);

//    @GetMapping("/valve-rankings/search")
//    List<ValveRankingDto> findByDate(@RequestHeader("Authorization") String token, @RequestParam("date") String date);
}
