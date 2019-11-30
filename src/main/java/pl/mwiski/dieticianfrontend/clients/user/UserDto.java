package pl.mwiski.dieticianfrontend.clients.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private long id;
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastName;
    @NotNull
    private RoleType roleType;
    @NotNull
    private int age;
    private SexType sex;
    @NotEmpty
    private AddressDto address;
    @NotEmpty
    private String phoneNumber;
    @Email
    @NotEmpty
    private String mail;
}
