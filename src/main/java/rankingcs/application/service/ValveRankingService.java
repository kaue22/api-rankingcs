package rankingcs.application.service;

import org.springframework.stereotype.Component;
import rankingcs.application.service.impl.SaveRankingFromContentService;
import rankingcs.port.in.SaveReadmePortIn;
import rankingcs.port.out.SaveRankingPortOut;

@Component
public class ValveRankingService implements SaveReadmePortIn {

    private final SaveRankingFromContentService saveRankingFromContentService;

    private final SaveRankingPortOut saveRankingPortOut;

    public ValveRankingService(final SaveRankingFromContentService saveRankingFromContentService, final SaveRankingPortOut saveRankingPortOut) {
        this.saveRankingFromContentService = saveRankingFromContentService;
        this.saveRankingPortOut = saveRankingPortOut;
    }

    @Override
    public String processReadmeFiles() {
        this.saveRankingPortOut.saveReadmeFiles();
        return "OK";
    }


}
