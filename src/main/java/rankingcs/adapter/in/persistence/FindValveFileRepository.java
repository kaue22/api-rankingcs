package rankingcs.adapter.in.persistence;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.persistence.repository.ReadmeRepository;
import rankingcs.port.in.SaveRankingPortIn;

@Component
public class FindValveFileRepository implements SaveRankingPortIn {
    private final ReadmeRepository readmeRepository;

    public FindValveFileRepository(ReadmeRepository readmeRepository) {
        this.readmeRepository = readmeRepository;
    }

//    private final SaveRankingPortIn saveRankingPortIn;
//
//    public FindValveFileRepository(ReadmeRepository readmeRepository, SaveRankingPortIn saveRankingPortIn) {
//        this.readmeRepository = readmeRepository;
//        this.saveRankingPortIn = saveRankingPortIn;
//    }

    public void lastDateUpdate(String date) {
        readmeRepository.existsByDate(date);
//        this.saveRankingPortIn.lastDateUpdate(content);
    }
}
