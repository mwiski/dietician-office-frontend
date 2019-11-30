package pl.mwiski.dieticianfrontend.clients.opinion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mwiski.dieticianfrontend.clients.user.SimpleUserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpinionDto {

    private long id;
    private String opinion;
    private String addedAt;
    private SimpleUserDto user;

    public OpinionDto(String opinion, SimpleUserDto user) {
        this.opinion = opinion;
        this.user = user;
    }
}
