package pl.mwiski.dieticianfrontend.clients.opinion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpinionService {

    private final OpinionClient opinionClient;

    public List<OpinionDto> getAll() {
        return opinionClient.getAll();
    }

    public List<OpinionDto> getUserOpinions(final long userId) {
        return opinionClient.getUserOpinions(userId);
    }

    public OpinionDto add(final OpinionDto opinionDto) {
        return opinionClient.add(opinionDto);
    }

    public OpinionDto edit(final long opinionId, final String content) {
        return opinionClient.edit(opinionId, content);
    }

    public void delete(final long opinionId) {
        opinionClient.delete(opinionId);
    }
}
