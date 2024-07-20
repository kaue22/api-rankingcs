package rankingcs.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import rankingcs.adapter.out.persistence.entity.ValveRankingEntity;

@Repository
public interface ValveRankingRepository extends MongoRepository<ValveRankingEntity, String> {
}