package rankingcs.application.service;

import org.springframework.stereotype.Component;
import rankingcs.application.service.impl.ConvertFileToJsonService;
import rankingcs.port.in.SaveReadmePortIn;
import rankingcs.port.out.SaveRankingPortOut;

@Component
public class ValveRankingService implements SaveReadmePortIn {


    private final SaveRankingPortOut saveRankingPortOut;

    private final ConvertFileToJsonService convertFileToJsonService;

    public ValveRankingService(SaveRankingPortOut saveRankingPortOut, ConvertFileToJsonService convertFileToJsonService) {
        this.saveRankingPortOut = saveRankingPortOut;
        this.convertFileToJsonService = convertFileToJsonService;
    }


    @Override
    public String processReadmeFiles() {
        this.saveRankingPortOut.saveReadmeFiles();
        return "OK";
    }

    public void apiReturnRankingValve() {
        var context = "";
        this.convertFileToJsonService.saveRankingFromContent(context);
    }

}
