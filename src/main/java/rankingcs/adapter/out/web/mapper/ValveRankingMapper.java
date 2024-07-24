package rankingcs.adapter.out.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import rankingcs.adapter.out.web.dto.ValveRankingDto;
import rankingcs.application.domain.ValveRankingDomain;

@Mapper
public interface ValveRankingMapper {
    ValveRankingMapper INSTANCE = Mappers.getMapper(ValveRankingMapper.class);

    ValveRankingDto toValveRankingDto(ValveRankingDomain domain);
}
