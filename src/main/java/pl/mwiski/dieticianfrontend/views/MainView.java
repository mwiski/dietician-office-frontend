package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import pl.mwiski.dieticianfrontend.clients.opinion.OpinionDto;
import pl.mwiski.dieticianfrontend.clients.opinion.OpinionService;
import pl.mwiski.dieticianfrontend.clients.user.RoleType;
import pl.mwiski.dieticianfrontend.clients.user.SimpleUserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;
import static pl.mwiski.dieticianfrontend.views.ViewsUtils.getLoggedUser;
import static pl.mwiski.dieticianfrontend.views.ViewsUtils.getRoute;

@Route
public class MainView extends VerticalLayout {

    private Grid<OpinionDto> opinionGrid = new Grid<>(OpinionDto.class);
    private OpinionService opinionService;
    private long opinionId;

    protected MainView(OpinionService opinionService,
                       UserService userService,
                       ViewsUtils viewsUtils) {
        this.opinionService = opinionService;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();
        addClassName("main-view");

        final UserDto userDto = getLoggedUser(userService);

        TextField opinion = new TextField();

        Button addOpinionButton = new Button("Add");
        addOpinionButton.addClickShortcut(Key.ENTER);
        addOpinion(opinionService, userDto, opinion, addOpinionButton, opinionGrid);

        Button editButton = new Button("Edit");
        editButton.setVisible(false);
        TextField edit = new TextField("Edit opinion");
        edit.setVisible(false);
        Button deleteButton = new Button("Delete");
        deleteButton.setVisible(false);

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");
        opinionGrid.setColumns("addedAt", "opinion", "user.user");

        Button recipesButton = getRoute("Recipes", "recipes");
        HorizontalLayout layout = new HorizontalLayout(recipesButton);
        addLogButtons(userDto, layout);

        add(
                layout,
                header,
                new H3("With this app you can look for visits to dieticians and schedule them!"),
                new H4("Opinions about our dieticians:"),
                opinionGrid,
                new H4("Add your opinion:"),
                new HorizontalLayout(opinion, addOpinionButton, edit, editButton, deleteButton)
        );
        opinionGrid.asSingleSelect().addValueChangeListener(event -> {
            if (userDto.getName() != null) {
                OpinionDto selectedOpinion = opinionGrid.asSingleSelect().getValue();
                if (selectedOpinion == null) {
                    return;
                } else if (selectedOpinion.getUser().getUser().equals(userDto.getLogin()) ||
                        userDto.getRoleType() == RoleType.ADMIN) {
                    edit.setVisible(true);
                    editButton.setVisible(true);
                    deleteButton.setVisible(true);
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
                    edit.setVisible(false);
                    editButton.setVisible(false);
                    deleteButton.setVisible(false);
                    refresh();
                }
        }});
        refresh();
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

    public void addLogButtons(UserDto userDto, HorizontalLayout layout) {
        Button loginButton = getRoute("Login", "login");
        Button logoutButton = new Button("Logout");
        Button registration = getRoute("Registration", "registration");
        logoutButton.addClickListener(e -> getUI().ifPresent(ui -> {
            ui.getPage().setLocation("logout");
        }));

        if (userDto.getName() != null) {
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
