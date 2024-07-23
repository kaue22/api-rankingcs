package rankingcs.application.service;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.persistence.extractorvalve.ExtractorValveRepository;
import rankingcs.application.domain.ReadmeDomain;
import rankingcs.application.domain.ValveRankingDomain;
import rankingcs.application.service.mapper.ConvertFileToJsonMapper;
import rankingcs.port.in.SaveReadmePortIn;
import rankingcs.port.out.SaveRankingPortOut;

import java.util.List;

@Component
public class ValveRankingService implements SaveReadmePortIn {

    private final SaveRankingPortOut saveRankingPortOut;
    private final ExtractorValveRepository extractorValveRepository;

    public ValveRankingService(SaveRankingPortOut saveRankingPortOut, ExtractorValveRepository extractorValveRepository) {
        this.saveRankingPortOut = saveRankingPortOut;
        this.extractorValveRepository = extractorValveRepository;
    }


    @Override
    public String processReadmeFiles() {
        this.saveRankingPortOut.saveReadmeFiles();
        List<ReadmeDomain> readmeDomainList = this.extractorValveRepository.findByContextValve();

        for (ReadmeDomain rd : readmeDomainList) {
            List<ValveRankingDomain> rankingDomains = ConvertFileToJsonMapper.INSTANCE.convertContentFromString(rd);
            for (ValveRankingDomain domain : rankingDomains) {
                // Processar o objeto ValveRankingDomain conforme necessário
                System.out.println(domain.getTeamName());
            }
        }
        return "";
    }
}
