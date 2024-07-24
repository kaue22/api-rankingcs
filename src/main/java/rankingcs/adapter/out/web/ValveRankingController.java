package rankingcs.adapter.out.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rankingcs.adapter.out.web.dto.ValveRankingDto;
import rankingcs.adapter.out.web.feign.ValveRankingFeignClient;
import rankingcs.adapter.out.web.mapper.ValveRankingMapper;
import rankingcs.application.domain.ValveRankingDomain;
import rankingcs.port.out.ValveSendPortOut;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/ranking")
public class ValveRankingController implements ValveSendPortOut {
    @Value("${api.valve-ranking.token}")
    private String token;

    private final ValveRankingFeignClient feignClient;

    public ValveRankingController(ValveRankingFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public ResponseEntity<String> processReadmeFiles(@RequestBody List<ValveRankingDomain> rankingDomains) {
        try {
            // Converter para DTO
            List<ValveRankingDto> dtos = rankingDomains.stream()
                    .map(ValveRankingMapper.INSTANCE::toValveRankingDto)
                    .collect(Collectors.toList());

            // Enviar dados via Feign Client
            String response = feignClient.sendRankingData("Bearer " + token, dtos.toArray(new ValveRankingDto[0]));

            // Retornar resposta JSON
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(dtos), HttpStatus.OK);

        } catch (Exception e) {
            // Tratar erro de comunicação ou outros problemas
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //FAZ DEPOIS POIS PRECISO ALTERAR A FORMA QUE PEGAMOS DO BANCO DE DADOS,para no futuro fazer pesquisas por datas
//    @GetMapping("/search")
//    public ResponseEntity<List<ValveRankingDomain>> findByDate(@RequestParam("date") String date) {
//        try {
//            List<ValveRankingDomain> rankingDomains = valveRankingService.findByDate(date);
//            return new ResponseEntity<>(rankingDomains, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
