package rankingcs.infrastructure.repository;

import rankingcs.infrastructure.persistence.ReadmeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadmeRepository extends MongoRepository<ReadmeEntity, String> {
}