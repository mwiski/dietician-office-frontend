package pl.mwiski.dieticianfrontend.clients.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitClient visitClient;
    
    public List<VisitDto> getAll() {
        return visitClient.getAll();
    }

    public List<VisitDto> getAvailableVisits(final String date) {
        return visitClient.getAvailableVisits(date);
    }

    public List<VisitDto> getUserVisits(final long userId) {
        return visitClient.getUserVisits(userId);
    }

    public List<VisitDto> getDieticianVisits(final long dieticianId) {
        return visitClient.getDieticianVisits(dieticianId);
    }

    public VisitDto get(final long visitId) {
        return visitClient.get(visitId);
    }

    public VisitDto add(final VisitDto visitDto) {
        return visitClient.add(visitDto);
    }

    public VisitDto schedule(final long visitId, long userId) {
        return visitClient.schedule(visitId, userId);
    }

    public void cancel(final long visitId) {
        visitClient.cancel(visitId);
    }
}
