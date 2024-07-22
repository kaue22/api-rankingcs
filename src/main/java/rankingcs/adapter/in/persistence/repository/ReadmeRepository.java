package rankingcs.adapter.in.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rankingcs.adapter.in.persistence.entity.ReadmeEntity;

@Repository
public interface ReadmeRepository extends MongoRepository<ReadmeEntity, String> {
    Boolean existsByDate(String date);
}
