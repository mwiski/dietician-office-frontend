package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import pl.mwiski.dieticianfrontend.clients.opinion.OpinionDto;
import pl.mwiski.dieticianfrontend.clients.opinion.OpinionService;
import pl.mwiski.dieticianfrontend.clients.user.RoleType;
import pl.mwiski.dieticianfrontend.clients.user.SimpleUserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;
import pl.mwiski.dieticianfrontend.views.utils.ViewsUtils;
import static pl.mwiski.dieticianfrontend.views.utils.ViewsUtils.*;

@Route
public class MainView extends VerticalLayout {

    private Grid<OpinionDto> opinionGrid = new Grid<>(OpinionDto.class);
    private OpinionService opinionService;
    private long opinionId;

    protected MainView(OpinionService opinionService,
                       UserService userService,
                       DieticianService dieticianService,
                       ViewsUtils viewsUtils) {
        this.opinionService = opinionService;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        addClassName("main-view");

        final UserDto userDto = getLoggedUser(userService);
        final DieticianDto dieticianDto = getLoggedDietician(dieticianService);

        TextField opinion = new TextField();

        Button addOpinionButton = new Button("Add");
        addOpinionButton.addClickShortcut(Key.ENTER);
        addOpinion(opinionService, userDto, opinion, addOpinionButton, opinionGrid);

        Button editButton = new Button("Edit");
        TextField edit = new TextField("Edit opinion");
        Button deleteButton = new Button("Delete");

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");
        opinionGrid.setColumns("addedAt", "opinion", "user.user");

        Button recipesButton = getRoute("Recipes", "recipes");
        HorizontalLayout layout = new HorizontalLayout(recipesButton);
        addLogButtons(userDto, dieticianDto, layout);
        FormLayout formLayout = new FormLayout();
        formLayout.add(edit, editButton, deleteButton);
        formLayout.setVisible(false);
        opinionGrid.setSizeFull();
        HorizontalLayout horizontalLayout = new HorizontalLayout(opinionGrid, formLayout);
        horizontalLayout.setSizeFull();

        add(
                layout,
                header,
                new H3("With this app you can look for visits to dieticians and schedule them!"),
                new H4("Opinions about our dieticians:"),
                horizontalLayout,
                new H4("Add your opinion:"),
                new HorizontalLayout(opinion, addOpinionButton)
        );
        setEditAndDeleteOpinion(userDto, editButton, edit, deleteButton, formLayout);
        setSizeFull();
        refresh();
    }

    private void setEditAndDeleteOpinion(UserDto userDto, Button editButton, TextField edit, Button deleteButton, FormLayout formLayout) {
        opinionGrid.asSingleSelect().addValueChangeListener(event -> {
            if (userDto.getName() != null) {
                OpinionDto selectedOpinion = opinionGrid.asSingleSelect().getValue();
                if (selectedOpinion == null) {
                    return;
                } else if (selectedOpinion.getUser().getUser().equals(userDto.getLogin()) ||
                        userDto.getRoleType() == RoleType.ADMIN) {
                    formLayout.setVisible(true);
                    opinionId = selectedOpinion.getId();
                    editButton.addClickListener(e -> {
                        editOpinion(edit);
                        refresh();
                    });
                    deleteButton.addClickListener(e -> {
                        deleteOpinion();
                        refresh();
                    });
                } else {
                    formLayout.setVisible(false);
                    refresh();
                }
        }});
    }

    private void addOpinion(OpinionService opinionService, UserDto userDto, TextField textField, Button button, Grid grid) {
        button.addClickListener(click -> {
            if (userDto.getName() != null) {
                if (textField.getValue().length() > 1) {
                    opinionService.add(
                            new OpinionDto(
                                    textField.getValue(), new SimpleUserDto(
                                    userDto.getId(),
                                    userDto.getName(),
                                    userDto.getLastName(),
                                    userDto.getLogin(),
                                    userDto.getRoleType(),
                                    userDto.getPhoneNumber(),
                                    userDto.getMail())));
                    opinionGrid.setItems(opinionService.getAll());
                } else {
                    Notification.show("Opinion is too short!");
                }
            } else {
                Notification.show("Only logged in users can add opinions!");
            }
        });
    }

    private void editOpinion(TextField textField) {
        if (textField.getValue().length() > 1) {
            opinionService.edit(opinionId, textField.getValue());
            opinionGrid.setItems(opinionService.getAll());
        } else {
            Notification.show("Opinion is too short!");
        }
    }

    private void deleteOpinion() {
        opinionService.delete(opinionId);
        opinionGrid.setItems(opinionService.getAll());
    }

    public void addLogButtons(UserDto userDto, DieticianDto dieticianDto, HorizontalLayout layout) {
        Button loginButton = getRoute("Login", "login");
        Button logoutButton = new Button("Logout");
        Button registration = getRoute("Registration", "registration");
        Button adminButton = getRoute("Admin", "admin");
        Button visitsButton = getRoute("Visits", "visits");
        logoutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().setLocation("logout")));
        if (userDto.getName() != null && userDto.getRoleType() == RoleType.ADMIN) {
            layout.add(adminButton);
        }
        if (userDto.getName() != null || dieticianDto.getName() != null) {
            layout.add(visitsButton);
            layout.add(logoutButton);
        } else {
            layout.add(loginButton);
            layout.add(registration);
        }
    }

    public void refresh() {
        opinionGrid.setItems(opinionService.getAll());
        opinionGrid.asSingleSelect().clear();
    }
}
