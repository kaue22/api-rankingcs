package rankingcs.adapter.in.persistence.valverepository.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rankingcs.adapter.in.persistence.valverepository.entity.ReadmeEntity;

@Repository
public interface ReadmeRepository extends MongoRepository<ReadmeEntity, String> {
    boolean existsByCreationDate(String creationDate);
}
