package pl.mwiski.dieticianfrontend.clients.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.mwiski.dieticianfrontend.clients.user.RoleType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto {

    private long id;
    private String login;
    private String password;
    private RoleType role;

    public LoginDto(LoginDto login) {
    }
}
