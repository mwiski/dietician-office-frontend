package pl.mwiski.dieticianfrontend.clients.dietician;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mwiski.dieticianfrontend.clients.user.RoleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DieticianDto {

    private long id;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private RoleType roleType;
    private String phoneNumber;
    private String mail;
}
