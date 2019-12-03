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
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianDto;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianService;
import pl.mwiski.dieticianfrontend.clients.dietician.SimpleDieticianDto;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;
import pl.mwiski.dieticianfrontend.clients.visit.VisitDto;
import pl.mwiski.dieticianfrontend.clients.visit.VisitService;
import pl.mwiski.dieticianfrontend.views.utils.ViewsUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static pl.mwiski.dieticianfrontend.views.utils.ViewsUtils.*;

@Route("dietician/visits")
public class VisitsDieticianView extends VerticalLayout {

    private Grid<VisitDto> availableGrid = new Grid<>(VisitDto.class);
    private Grid<VisitDto> dieticianVisitsGrid = new Grid<>(VisitDto.class);
    private VisitService visitsService;
    private DieticianService dieticianService;
    private long dieticianVisitId;
    private UserDto userDto;
    private DieticianDto dieticianDto;
    private DatePicker searchedDate;
    private DatePicker newVisitDate;
    private TimePicker newVisitTime;
    private H3 lookForVisitsHeader;
    private H4 yourVisitsHeader;
    private H4 addVisitHeader;
    private HorizontalLayout availableVisitsLayout;
    private HorizontalLayout dieticianVisitsLayout;
    private Button addVisitByDieticianButton;
    private Button searchButton;
    private Button showMyVisitsButton;

    protected VisitsDieticianView(VisitService visitsService,
                         UserService userService,
                         DieticianService dieticianService,
                         ViewsUtils viewsUtils) {
        this.visitsService = visitsService;
        this.dieticianService = dieticianService;
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        userDto = getLoggedUser(userService);
        dieticianDto = getLoggedDietician(dieticianService);

        searchedDate = new DatePicker("Searched visit date");
        searchButton = new Button("Search");
        showMyVisitsButton = new Button("Show my Visits");
        Button cancelDieticianVisitButton = new Button("Cancel");
        addVisitByDieticianButton = new Button("Add visit");

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");
        dieticianVisitsGrid.setMinHeight("400px");
        dieticianVisitsGrid.setSizeFull();
        availableGrid.setColumns("dateTime", "dietician.name", "dietician.lastName");
        dieticianVisitsGrid.setColumns("dateTime", "dietician.name", "dietician.lastName", "available");

        Button recipesButton = getRoute("Recipes", "recipes");
        Button mainButton = getRoute("Main", "");
        HorizontalLayout layout = new HorizontalLayout(mainButton, recipesButton);
        addLogButtons(layout);
        VerticalLayout availableVisitLayout = new VerticalLayout();
        availableVisitLayout.setVisible(false);
        VerticalLayout dieticianVisitLayout = new VerticalLayout();
        dieticianVisitLayout.setVisible(false);
        newVisitDate = new DatePicker("New Visit date");
        newVisitTime = new TimePicker("New Visit time");

        availableGrid.setSizeFull();
        availableGrid.setMinHeight("400px");
        availableVisitsLayout = new HorizontalLayout(availableGrid, availableVisitLayout);
        availableVisitsLayout.setSizeFull();

        dieticianVisitsLayout = new HorizontalLayout(dieticianVisitsGrid, dieticianVisitLayout);
        dieticianVisitsLayout.setSizeFull();
        dieticianVisitLayout.setMinHeight("500px");

        lookForVisitsHeader = new H3("Look for visits and add them");
        yourVisitsHeader = new H4("Look for your visits:");
        addVisitHeader = new H4("Add new Visit");

        add(
                layout,
                header,
                lookForVisitsHeader,
                searchedDate,
                searchButton,
                availableVisitsLayout,
                yourVisitsHeader,
                showMyVisitsButton,
                dieticianVisitsLayout,
                addVisitHeader,
                newVisitDate,
                newVisitTime,
                addVisitByDieticianButton
        );
        showMyVisitsButton.addClickListener(e -> getMyVisits());
        searchForAvailableVisits(searchButton);
        cancelDieticianVisit(cancelDieticianVisitButton, dieticianVisitLayout);
        addVisitByDieticianButton.addClickListener(e -> addVisitByDietcian());
        setSizeFull();
    }

    private void getMyVisits() {
        dieticianVisitsGrid.setItems(visitsService.getDieticianVisits(dieticianDto.getId()));
    }

    private void searchForAvailableVisits(Button button) {
        button.addClickListener(click -> {
            if (dieticianDto.getName() != null) {
                LocalDate localDate = searchedDate.getValue();
                availableGrid.setItems(visitsService.getAvailableVisits(localDate.toString()));
            }
        });
    }

    private void cancelDieticianVisit(Button cancelDieticianVisitButton, VerticalLayout dieticianVisitLayout) {
        dieticianVisitsGrid.asSingleSelect().addValueChangeListener(event -> {
            VisitDto selectedVisit = dieticianVisitsGrid.asSingleSelect().getValue();
            if (selectedVisit == null) {
                return;
            }
            dieticianVisitLayout.setVisible(true);
            dieticianVisitId = selectedVisit.getId();
            dieticianVisitLayout.add("Date and time: " + selectedVisit.getDateTime());
            dieticianVisitLayout.add("Dietician: " + selectedVisit.getDietician().getName() + " " + selectedVisit.getDietician().getLastName() +
                    ",\nphone number " + selectedVisit.getDietician().getPhoneNumber() + ",\n mail: " + selectedVisit.getDietician().getMail() +
                    ", available: " + selectedVisit.isAvailable());
            dieticianVisitLayout.add(cancelDieticianVisitButton);
            cancelDieticianVisitButton.addClickListener(e -> {
                visitsService.cancel(dieticianVisitId);
                LocalDate localDate = searchedDate.getValue();
                availableGrid.setItems(visitsService.getAvailableVisits(localDate.toString()));
                dieticianVisitsGrid.setItems(visitsService.getDieticianVisits(dieticianDto.getId()));
                dieticianVisitsGrid.asSingleSelect().clear();
                dieticianVisitLayout.setVisible(false);
            });
        });
    }

    private void addVisitByDietcian() {
        SimpleDieticianDto simpleDieticianDto = new SimpleDieticianDto(
                dieticianDto.getId(),
                dieticianDto.getName(),
                dieticianDto.getLastName(),
                dieticianDto.getLogin(),
                dieticianDto.getRoleType(),
                dieticianDto.getPhoneNumber(),
                dieticianDto.getMail()
        );
        LocalDateTime localDateTime = LocalDateTime.of(newVisitDate.getValue(), newVisitTime.getValue());
        VisitDto newVisit = new VisitDto(
                dateToString(localDateTime),
                simpleDieticianDto,
                true);
        visitsService.add(newVisit);
        dieticianVisitsGrid.setItems(visitsService.getDieticianVisits(dieticianDto.getId()));

    }

    public void addLogButtons(HorizontalLayout layout) {
        Button logoutButton = new Button("Logout");
        logoutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().setLocation("logout")));
        layout.add(logoutButton);
    }
}
