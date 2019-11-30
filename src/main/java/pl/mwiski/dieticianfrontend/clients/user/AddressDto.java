package pl.mwiski.dieticianfrontend.clients.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto {

    @NotEmpty
    private String city;
    @NotEmpty
    private String postalCode;
    @NotEmpty
    private String street;
    @NotEmpty
    private String buildingNumber;
    private String apartmentNumber;
}
