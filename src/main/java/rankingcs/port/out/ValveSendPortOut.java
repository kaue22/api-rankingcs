package rankingcs.port.out;

import org.springframework.http.ResponseEntity;
import rankingcs.application.domain.ValveRankingDomain;

import java.util.List;

public interface ValveSendPortOut {
    ResponseEntity<String> processReadmeFiles(List<ValveRankingDomain> rankingDomains);
}
