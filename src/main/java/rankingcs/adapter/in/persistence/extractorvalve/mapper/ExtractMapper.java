package rankingcs.adapter.in.persistence.extractorvalve.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rankingcs.adapter.in.persistence.valverepository.entity.ReadmeEntity;
import rankingcs.application.domain.ReadmeDomain;

import java.util.List;

@Mapper
public interface ExtractMapper {
    ExtractMapper INSTANCE = Mappers.getMapper(ExtractMapper.class);

    ReadmeDomain mapReadmeToDomain(ReadmeEntity readmeEntity);

    List<ReadmeDomain> mapReadmeListToDomainList(List<ReadmeEntity> readmeEntities);
}