package rankingcs.port.in;

import rankingcs.application.domain.ReadmeDomain;

import java.util.List;

public interface ExtractorValvePortIn {
    List<ReadmeDomain> findByContextValve();
}
