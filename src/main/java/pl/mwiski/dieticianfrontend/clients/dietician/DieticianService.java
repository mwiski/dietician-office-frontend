package pl.mwiski.dieticianfrontend.clients.dietician;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DieticianService {

    private final DieticianClient dieticianClient;

    public List<DieticianDto> getAll() {
        return dieticianClient.getAll();
    }

    public DieticianDto get(final long dieticianId) {
        return dieticianClient.get(dieticianId);
    }

    public DieticianDto add(final DieticianDto dieticianDto) {
        return dieticianClient.add(dieticianDto);
    }

    public DieticianDto update(final DieticianDto dieticianDto) {
        return dieticianClient.update(dieticianDto);
    }

    public void delete(final long dieticianId) {
        dieticianClient.delete(dieticianId);
    }

}
