package pl.mwiski.dieticianfrontend.clients.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleUserDto {

    private long id;
    private String name;
    private String lastName;
    private String user;
    private RoleType roleType;
    private String phoneNumber;
    private String mail;
}
