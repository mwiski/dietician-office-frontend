package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianDto;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianService;
import pl.mwiski.dieticianfrontend.clients.user.*;
import pl.mwiski.dieticianfrontend.views.utils.ViewsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static pl.mwiski.dieticianfrontend.views.utils.ViewsUtils.*;

@Route("admin")
public class AdminView extends VerticalLayout {

    private Grid<UserDto> userGrid = new Grid<>(UserDto.class);
    private Grid<DieticianDto> dieticianGrid = new Grid<>(DieticianDto.class);
    private UserService userService;
    private DieticianService dieticianService;
    private long userId;
    private long dieticianId;
    private Button deleteUserButton;
    private Button deleteDieticianButton;
    private Button addDieticianButton;
    private Button addAdminButton;
    private TextField dieticianLogin = new TextField("Login");
    private TextField dieticianPassword = new TextField("Password");
    private TextField dieticianName = new TextField("Name");
    private TextField dieticianLastName = new TextField("Last name");
    private TextField dieticianPhoneNumber = new TextField("Phone number");
    private TextField dieticianMail = new TextField("E-mail");
    private TextField adminLogin = new TextField("Login");
    private TextField adminPassword = new TextField("Password");
    private TextField adminName = new TextField("Name");
    private TextField adminLastName = new TextField("Last name");
    private TextField adminAge = new TextField("Age");
    private ComboBox<SexType> adminSex = new ComboBox<>("Sex");
    private TextField adminCity = new TextField("City");
    private TextField adminPostalCode = new TextField("Postal code");
    private TextField adminStreet = new TextField("Street");
    private TextField adminBuildingNumber = new TextField("Building number");
    private TextField adminApartmentNumber = new TextField("Apartment number");
    private TextField adminPhoneNumber = new TextField("Phone number");
    private TextField adminMail = new TextField("E-mail");

    protected AdminView(UserService userService,
                       DieticianService dieticianService,
                       ViewsUtils viewsUtils) {
        this.userService = userService;
        this.dieticianService = dieticianService;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        final UserDto userDto = getLoggedUser(userService);
        final DieticianDto dieticianDto = getLoggedDietician(dieticianService);

        deleteUserButton = new Button("Delete user");
        deleteDieticianButton = new Button("Delete dietician");
        addDieticianButton = new Button("Add Dietician");
        addAdminButton = new Button("Add Admin");

        userGrid.setColumns("id", "login", "name", "lastName", "roleType", "age", "sex", "phoneNumber", "mail");
        userGrid.setSizeFull();
        userGrid.setMinHeight("400px");

        dieticianGrid.setColumns("id", "login", "name", "lastName", "phoneNumber", "mail");
        dieticianGrid.setSizeFull();
        dieticianGrid.setMinHeight("400px");

        VerticalLayout addDieticianLayout = new VerticalLayout();
        addDieticianLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        VerticalLayout addAdminLayout = new VerticalLayout();
        addAdminLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Button recipesButton = getRoute("Recipes", "recipes");
        HorizontalLayout layout = new HorizontalLayout(recipesButton);
        addLogButtons(userDto, dieticianDto, layout);

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");

        add(
                layout,
                header,
                new H3("Search for users"),
                userGrid,
                deleteUserButton,
                dieticianGrid,
                deleteDieticianButton,
                new H4("Add dietician"),
                addDieticianLayout,
                addDieticianButton,
                new H4("Add admin"),
                addAdminLayout,
                addAdminButton
        );
        adminSex.setItems(SexType.values());
        addFieldsToAddDieticianLayout(addDieticianLayout);
        addFieldsToAddAdminLayout(addAdminLayout);
        userGrid.setItems(userService.getAll());
        dieticianGrid.setItems(dieticianService.getAll());
        deleteUser(deleteUserButton);
        deleteDietician(deleteDieticianButton);
        addDieticianButton.addClickListener(e -> saveDietician());
        addAdminButton.addClickListener(e -> saveAdmin());
        setSizeFull();
    }

    private void deleteUser(Button deleteUserButton) {
        userGrid.asSingleSelect().addValueChangeListener(event -> {
            UserDto selectedUser = userGrid.asSingleSelect().getValue();
            if (selectedUser == null) return;
            userId = selectedUser.getId();
            deleteUserButton.addClickListener(e -> {
                userService.delete(userId);
                userGrid.setItems(userService.getAll());
                userGrid.asSingleSelect().clear();
            });
            userGrid.setItems(userService.getAll());
            userGrid.asSingleSelect().clear();
            });
    }

    private void deleteDietician(Button deleteDieticianButton) {
        dieticianGrid.asSingleSelect().addValueChangeListener(event -> {
                DieticianDto selectedDetician = dieticianGrid.asSingleSelect().getValue();
                if (selectedDetician == null) return;
                dieticianId = selectedDetician.getId();
                deleteDieticianButton.addClickListener(e -> {
                    dieticianService.delete(dieticianId);
                    dieticianGrid.setItems(dieticianService.getAll());
                    dieticianGrid.asSingleSelect().clear();
                });
                dieticianGrid.setItems(dieticianService.getAll());
                dieticianGrid.asSingleSelect().clear();
        });
    }

    public void addLogButtons(UserDto userDto, DieticianDto dieticianDto, HorizontalLayout layout) {
        Button loginButton = getRoute("Login", "login");
        Button logoutButton = new Button("Logout");
        Button registration = getRoute("Registration", "registration");
        Button visitsButton = getRoute("Visits", "admin/visits");
        logoutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().setLocation("logout")));

        if (userDto.getName() != null || dieticianDto.getName() != null) {
            layout.add(logoutButton);
            layout.add(visitsButton);
        } else {
            layout.add(loginButton);
            layout.add(registration);
        }
    }

    private void addFieldsToAddDieticianLayout(VerticalLayout addDieticianLayout) {
        addDieticianLayout.add(
                dieticianLogin,
                dieticianPassword,
                dieticianName,
                dieticianLastName,
                dieticianPhoneNumber,
                dieticianMail
        );
    }

    private void saveDietician() {
        DieticianDto dieticianDto = new DieticianDto(
                -1,
                dieticianLogin.getValue(),
                dieticianPassword.getValue(),
                dieticianName.getValue(),
                dieticianLastName.getValue(),
                RoleType.DIETICIAN,
                dieticianPhoneNumber.getValue(),
                dieticianMail.getValue()
        );
        validateFields(new ArrayList<>(Arrays.asList(dieticianLogin, dieticianPassword, dieticianName, dieticianLastName, dieticianPhoneNumber, dieticianMail)));
        dieticianService.add(dieticianDto);
        Optional<DieticianDto> newDietician = dieticianService.getDieticianByLogin(dieticianDto.getLogin());
        if (!newDietician.isPresent()) {
            Notification.show("Failed to add new dietician!");
        } else {
            Notification.show("Dietician " + dieticianDto.getLogin() + " has been added!");
        }
        dieticianGrid.setItems(dieticianService.getAll());
    }

    private void addFieldsToAddAdminLayout(VerticalLayout addAdminLayout) {
        addAdminLayout.add(
                adminLogin,
                adminPassword,
                adminName,
                adminLastName,
                adminAge,
                adminSex,
                adminCity,
                adminPostalCode,
                adminStreet,
                adminBuildingNumber,
                adminApartmentNumber,
                adminPhoneNumber,
                adminMail
        );
    }

    private void saveAdmin() {
        UserDto userDto = new UserDto(
                -1,
                adminLogin.getValue(),
                adminPassword.getValue(),
                adminName.getValue(),
                adminLastName.getValue(),
                RoleType.ADMIN,
                convertStringToInt(adminAge.getValue()),
                adminSex.getValue(),
                new AddressDto(adminCity.getValue(), adminPostalCode.getValue(), adminStreet.getValue(), adminBuildingNumber.getValue(), adminApartmentNumber.getValue()),
                adminPhoneNumber.getValue(),
                adminMail.getValue()
        );
        validateFields(new ArrayList<>(Arrays.asList(adminLogin,
                adminPassword,
                adminName,
                adminLastName,
                adminCity,
                adminPostalCode,
                adminStreet,
                adminBuildingNumber,
                adminPhoneNumber,
                adminMail)));
        userService.addAdmin(userDto);
        Optional<UserDto> newAdmin = userService.getUserByLogin(userDto.getLogin());
        if (!newAdmin.isPresent()) {
            Notification.show("Failed to add new admin!");
        } else {
            Notification.show("Admin " + userDto.getLogin() + " has been added!");
        }
        userGrid.setItems(userService.getAll());
    }
}
