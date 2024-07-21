package rankingcs.adapter.in.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rankingcs.adapter.in.persistence.dto.ReadmeEntity;

@Repository
public interface ReadmeRepository extends MongoRepository<ReadmeEntity, String> {
}
