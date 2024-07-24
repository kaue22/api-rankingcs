package rankingcs.application.service;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.persistence.extractorvalve.ExtractorValveRepository;
import rankingcs.application.domain.ReadmeDomain;
import rankingcs.application.domain.ValveRankingDomain;
import rankingcs.application.service.mapper.ConvertFileToJsonMapper;
import rankingcs.port.in.SaveReadmePortIn;
import rankingcs.port.out.SaveRankingPortOut;
import rankingcs.port.out.ValveSendPortOut;

import java.util.List;

@Component
public class ValveRankingService implements SaveReadmePortIn {

    private final SaveRankingPortOut saveRankingPortOut;
    private final ExtractorValveRepository extractorValveRepository;
    private final ValveSendPortOut valveSendPortOut;

    public ValveRankingService(SaveRankingPortOut saveRankingPortOut, ExtractorValveRepository extractorValveRepository, ValveSendPortOut valveSendPortOut) {
        this.saveRankingPortOut = saveRankingPortOut;
        this.extractorValveRepository = extractorValveRepository;
        this.valveSendPortOut = valveSendPortOut;
    }


    @Override
    public String processReadmeFiles() {
        this.saveRankingPortOut.saveReadmeFiles();
        List<ReadmeDomain> readmeDomainList = this.extractorValveRepository.findByContextValve();

        for (ReadmeDomain rd : readmeDomainList) {
            List<ValveRankingDomain> rankingDomains = ConvertFileToJsonMapper.INSTANCE.convertContentFromString(rd);
            for (ValveRankingDomain domain : rankingDomains) {;
                // Enviar o ranking processado
                valveSendPortOut.processReadmeFiles(rankingDomains);
            }
        }
        return "";
    }
}
