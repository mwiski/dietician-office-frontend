package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import pl.mwiski.dieticianfrontend.clients.user.*;

import java.util.ArrayList;
import java.util.Arrays;

import static pl.mwiski.dieticianfrontend.views.ViewsUtils.*;

@Route("registration")
public class RegistrationView extends VerticalLayout {

    private UserService userService;
    private TextField login = new TextField("Login");
    private TextField password = new TextField("Password");
    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last name");
    private TextField age = new TextField("Age");
    private ComboBox<SexType> sex = new ComboBox<>("Sex");
    private TextField city = new TextField("City");
    private TextField postalCode = new TextField("Postal code");
    private TextField street = new TextField("Street");
    private TextField buildingNumber = new TextField("Building number");
    private TextField apartmentNumber = new TextField("Apartment number");
    private TextField phoneNumber = new TextField("Phone number");
    private TextField mail = new TextField("E-mail");

    protected RegistrationView(UserService userService, ViewsUtils viewsUtils) {
        Button register = new Button("Register");
        this.userService = userService;
        Button mainButton = getRoute("Main", "");
        Button recipesButton = getRoute("Recipes", "recipes");
        HorizontalLayout layout = new HorizontalLayout(mainButton, recipesButton);
        sex.setItems(SexType.values());
        register.addClickListener(e -> save());
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(
                layout,
                new H3("Registration"),
                login,
                password,
                name,
                lastName,
                age,
                sex,
                city,
                postalCode,
                street,
                buildingNumber,
                apartmentNumber,
                phoneNumber,
                mail,
                register
                );
    }

    private void save() {
        UserDto userDto = new UserDto(
                -1,
                login.getValue(),
                password.getValue(),
                name.getValue(),
                lastName.getValue(),
                RoleType.USER,
                convertStringToInt(age.getValue()),
                sex.getValue(),
                new AddressDto(city.getValue(), postalCode.getValue(), street.getValue(), buildingNumber.getValue(), apartmentNumber.getValue()),
                phoneNumber.getValue(),
                mail.getValue()
        );
        validateFields(new ArrayList<>(Arrays.asList(login, password, name, lastName, city, postalCode, street, buildingNumber, phoneNumber, mail)));
        userService.add(userDto);
        UserDto newUser = userService.getUserByName(userDto.getLogin());
        if (newUser == null) {
            Notification.show("Failed to register new user!");
        } else {
            Notification.show("User " + userDto.getLogin() + " has been registered!");
            getUI().ifPresent(ui -> ui.getPage().setLocation("/"));
        }
    }


}
