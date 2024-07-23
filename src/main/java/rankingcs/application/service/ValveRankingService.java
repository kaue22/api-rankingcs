package rankingcs.application.service;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.persistence.extractorvalve.ExtractorValveRepository;
import rankingcs.application.domain.ReadmeDomain;
import rankingcs.application.service.impl.ConvertFileToJsonService;
import rankingcs.port.in.SaveReadmePortIn;
import rankingcs.port.out.SaveRankingPortOut;

import java.util.List;

@Component
public class ValveRankingService implements SaveReadmePortIn {

    private final SaveRankingPortOut saveRankingPortOut;
    private final ExtractorValveRepository extractorValveRepository;
    private final ConvertFileToJsonService convertFileToJsonService;

    public ValveRankingService(SaveRankingPortOut saveRankingPortOut, ExtractorValveRepository extractorValveRepository, ConvertFileToJsonService convertFileToJsonService) {
        this.saveRankingPortOut = saveRankingPortOut;
        this.extractorValveRepository = extractorValveRepository;
        this.convertFileToJsonService = convertFileToJsonService;
    }


    @Override
    public String processReadmeFiles() {
        this.saveRankingPortOut.saveReadmeFiles();
        List<ReadmeDomain> readmeDomain = this.extractorValveRepository.findByContextValve();
        for (ReadmeDomain readmeDomain1 : readmeDomain) {
            this.convertFileToJsonService.saveRankingFromContent(readmeDomain1.getContent());
            System.out.println(readmeDomain.get(0).getContent());
        }
        return "OK";
    }
}
