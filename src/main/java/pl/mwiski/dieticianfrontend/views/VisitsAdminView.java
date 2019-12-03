package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianDto;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianService;
import pl.mwiski.dieticianfrontend.clients.dietician.SimpleDieticianDto;
import pl.mwiski.dieticianfrontend.clients.user.RoleType;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;
import pl.mwiski.dieticianfrontend.clients.visit.VisitDto;
import pl.mwiski.dieticianfrontend.clients.visit.VisitService;
import pl.mwiski.dieticianfrontend.views.utils.ViewsUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static pl.mwiski.dieticianfrontend.views.utils.ViewsUtils.*;

@Route("admin/visits")
public class VisitsAdminView extends VerticalLayout {

    private Grid<VisitDto> adminGrid = new Grid<>(VisitDto.class);
    private VisitService visitsService;
    private DieticianService dieticianService;
    private long adminVisitId;
    private UserDto userDto;
    private DieticianDto dieticianDto;
    private DatePicker newVisitDate;
    private TimePicker newVisitTime;
    private TextField dieticianLogin;
    private H3 lookForVisitsAdminHeader;
    private H4 addVisitHeader;
    private HorizontalLayout adminVisitsLayout;
    private Button addVisitByAdminButton;
    private Button searchAdminButton;

    protected VisitsAdminView(VisitService visitsService,
                         UserService userService,
                         DieticianService dieticianService,
                         ViewsUtils viewsUtils) {
        this.visitsService = visitsService;
        this.dieticianService = dieticianService;
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        userDto = getLoggedUser(userService);
        dieticianDto = getLoggedDietician(dieticianService);

        searchAdminButton = new Button("Search");
        Button cancelVisitButton = new Button("Cancel");
        addVisitByAdminButton = new Button("Add visit");

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");
        adminGrid.setSizeFull();
        adminGrid.setMinHeight("400px");
        adminGrid.setColumns("id", "dateTime", "dietician.name", "dietician.lastName", "dietician.dietician",  "available");

        Button recipesButton = getRoute("Recipes", "recipes");
        Button mainButton = getRoute("Main", "");
        HorizontalLayout layout = new HorizontalLayout(mainButton, recipesButton);
        addLogButtons(userDto, layout);
        VerticalLayout visitLayout = new VerticalLayout();
        visitLayout.setVisible(false);
        newVisitDate = new DatePicker("New Visit date");
        newVisitTime = new TimePicker("New Visit time");
        dieticianLogin = new TextField("Dietician login");

        adminVisitsLayout = new HorizontalLayout(adminGrid, visitLayout);
        adminVisitsLayout.setSizeFull();
        adminVisitsLayout.setMinHeight("500px");

        lookForVisitsAdminHeader = new H3("Look for visits");
        addVisitHeader = new H4("Add new Visit");

        add(
                layout,
                header,
                lookForVisitsAdminHeader,
                searchAdminButton,
                adminVisitsLayout,
                addVisitHeader,
                newVisitDate,
                newVisitTime,
                dieticianLogin,
                addVisitByAdminButton
        );
        searchAdminButton.addClickListener(e -> getAllVisits());
        cancelVisit(cancelVisitButton, visitLayout);
        addVisitByAdminButton.addClickListener(e -> addVisitByAdmin());
        setSizeFull();
    }

    private void getAllVisits() {
        if (userDto.getName() != null && userDto.getRoleType() == RoleType.ADMIN)
            adminGrid.setItems(visitsService.getAll());
    }

    private void cancelVisit(Button cancelVisitButton, VerticalLayout visitLayout) {
        adminGrid.asSingleSelect().addValueChangeListener(event -> {
            VisitDto selectedVisit = adminGrid.asSingleSelect().getValue();
            if (selectedVisit == null) {
                return;
            }
            visitLayout.setVisible(true);
            adminVisitId = selectedVisit.getId();
            visitLayout.add("Visit ID: " + selectedVisit.getId());
            visitLayout.add("Date and time: " + selectedVisit.getDateTime());
            visitLayout.add("User: " + selectedVisit.getUser().getName() + " " + selectedVisit.getUser().getLastName());
            visitLayout.add("Dietician: " + selectedVisit.getDietician().getName() + " " + selectedVisit.getDietician().getLastName());
            visitLayout.add(cancelVisitButton);
            cancelVisitButton.addClickListener(e -> {
                visitsService.cancel(adminVisitId);
                adminGrid.setItems(visitsService.getAll());
                adminGrid.asSingleSelect().clear();
                visitLayout.setVisible(false);
            });
        });
    }

    private void addVisitByAdmin() {
        Optional<DieticianDto> optionalDieticianDto = dieticianService.getDieticianByLogin(dieticianLogin.getValue());
        if (!optionalDieticianDto.isPresent()) {
            return;
        }
        DieticianDto dieticianToVisit = optionalDieticianDto.get();

        SimpleDieticianDto simpleDieticianDto = new SimpleDieticianDto(
                dieticianToVisit.getId(),
                dieticianToVisit.getName(),
                dieticianToVisit.getLastName(),
                dieticianToVisit.getLogin(),
                dieticianToVisit.getRoleType(),
                dieticianToVisit.getPhoneNumber(),
                dieticianToVisit.getMail()
        );
        LocalDateTime localDateTime = LocalDateTime.of(newVisitDate.getValue(), newVisitTime.getValue());
        VisitDto newVisit = new VisitDto(
                dateToString(localDateTime),
                simpleDieticianDto,
                true);
        visitsService.add(newVisit);
        adminGrid.setItems(visitsService.getAll());
    }

    public void addLogButtons(UserDto userDto, HorizontalLayout layout) {
        Button logoutButton = new Button("Logout");
        Button adminButton = getRoute("Admin", "admin");
        logoutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().setLocation("logout")));
        layout.add(adminButton);
        layout.add(logoutButton);

    }
}
