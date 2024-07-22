package rankingcs.adapter.in.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rankingcs.adapter.in.persistence.dto.ReadmeDto;
import rankingcs.adapter.in.persistence.entity.ReadmeEntity;

@Mapper
public interface ReadmeMapper {
    ReadmeMapper INSTANCE = Mappers.getMapper(ReadmeMapper.class);

    ReadmeDto toDto(ReadmeEntity entity);

    ReadmeEntity toEntity(ReadmeDto dto);
}
