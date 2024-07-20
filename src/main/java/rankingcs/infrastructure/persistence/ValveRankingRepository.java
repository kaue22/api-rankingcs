package rankingcs.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValveRankingRepository extends MongoRepository<ValveRankingEntity, String> {
}