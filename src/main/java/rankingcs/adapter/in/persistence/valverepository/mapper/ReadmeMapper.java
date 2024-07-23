package rankingcs.adapter.in.persistence.valverepository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rankingcs.adapter.in.persistence.valverepository.dto.ReadmeDto;
import rankingcs.adapter.in.persistence.valverepository.entity.ReadmeEntity;

@Mapper
public interface ReadmeMapper {
    ReadmeMapper INSTANCE = Mappers.getMapper(ReadmeMapper.class);

}
