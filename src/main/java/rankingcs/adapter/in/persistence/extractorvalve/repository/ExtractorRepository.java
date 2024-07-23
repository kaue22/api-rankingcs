package rankingcs.adapter.in.persistence.extractorvalve.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rankingcs.adapter.in.persistence.valverepository.entity.ReadmeEntity;

@Repository
public interface ExtractorRepository extends MongoRepository<ReadmeEntity, String> {
}
