package rankingcs.adapter.in.persistence.extractorvalve;

import org.springframework.stereotype.Component;
import rankingcs.adapter.in.persistence.extractorvalve.mapper.ExtractMapper;
import rankingcs.adapter.in.persistence.extractorvalve.repository.ExtractorRepository;
import rankingcs.adapter.in.persistence.valverepository.entity.ReadmeEntity;
import rankingcs.application.domain.ReadmeDomain;
import rankingcs.port.in.ExtractorValvePortIn;

import java.util.List;

@Component
public class ExtractorValveRepository implements ExtractorValvePortIn {
    private final ExtractorRepository extractorRepository;

    public ExtractorValveRepository(ExtractorRepository extractorRepository) {
        this.extractorRepository = extractorRepository;
    }
    @Override
    public List<ReadmeDomain> findByContextValve() {
        List<ReadmeEntity> readmeEntities = extractorRepository.findAll();
        return ExtractMapper.INSTANCE.mapReadmeListToDomainList(readmeEntities);
    }
}
