package rankingcs.adapter.in.persistence.valverepository;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.persistence.valverepository.repository.ReadmeRepository;

@Component
public class FindValveFileRepository {

    private final ReadmeRepository readmeRepository;

    public FindValveFileRepository(ReadmeRepository readmeRepository) {
        this.readmeRepository = readmeRepository;
    }

    public boolean lastDateUpdate(String date) {
        return readmeRepository.existsByCreationDate(date);
    }
}
