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
import com.vaadin.flow.router.Route;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianDto;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianService;
import pl.mwiski.dieticianfrontend.clients.user.RoleType;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;
import pl.mwiski.dieticianfrontend.clients.visit.VisitDto;
import pl.mwiski.dieticianfrontend.clients.visit.VisitService;
import pl.mwiski.dieticianfrontend.views.utils.ViewsUtils;

import java.time.LocalDate;

import static pl.mwiski.dieticianfrontend.views.utils.ViewsUtils.*;

@Route("user/visits")
public class VisitsUserView extends VerticalLayout{

    private Grid<VisitDto> availableGrid = new Grid<>(VisitDto.class);
    private Grid<VisitDto> userVisitsGrid = new Grid<>(VisitDto.class);
    private VisitService visitsService;
    private DieticianService dieticianService;
    private long visitId;
    private long userVisitId;
    private UserDto userDto;
    private DieticianDto dieticianDto;
    private DatePicker searchedDate;
    private H3 lookForVisitsHeader;
    private H4 yourVisitsHeader;
    private HorizontalLayout availableVisitsLayout;
    private HorizontalLayout userVisitsLayout;
    private Button searchButton;
    private Button showMyVisitsButton;

    protected VisitsUserView(VisitService visitsService,
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
        Button scheduleButton = new Button("Schedule");
        scheduleButton.setVisible(false);
        Button cancelUserVisitButton = new Button("Cancel");

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");
        userVisitsGrid.setMinHeight("400px");
        userVisitsGrid.setSizeFull();
        availableGrid.setColumns("dateTime", "dietician.name", "dietician.lastName");
        userVisitsGrid.setColumns("dateTime", "dietician.name", "dietician.lastName", "dietician.phoneNumber", "dietician.mail", "user.name", "user.lastName");

        Button recipesButton = getRoute("Recipes", "recipes");
        Button mainButton = getRoute("Main", "");
        HorizontalLayout layout = new HorizontalLayout(mainButton, recipesButton);
        addLogButtons(userDto, layout);
        VerticalLayout availableVisitLayout = new VerticalLayout();
        availableVisitLayout.setVisible(false);
        VerticalLayout userVisitLayout = new VerticalLayout();
        userVisitLayout.setVisible(false);

        availableGrid.setSizeFull();
        availableGrid.setMinHeight("500px");
        availableVisitsLayout = new HorizontalLayout(availableGrid, availableVisitLayout);
        availableVisitsLayout.setSizeFull();
        availableVisitLayout.setMinHeight("500px");

        userVisitsLayout = new HorizontalLayout(userVisitsGrid, userVisitLayout);
        userVisitsLayout.setSizeFull();
        userVisitLayout.setMinHeight("500px");

        lookForVisitsHeader = new H3("Look for visits and schedule them");
        lookForVisitsHeader.setVisible(false);
        yourVisitsHeader = new H4("Look for your visits:");
        yourVisitsHeader.setVisible(false);

        add(
                layout,
                header,
                lookForVisitsHeader,
                searchedDate,
                searchButton,
                availableVisitsLayout,
                scheduleButton,
                yourVisitsHeader,
                showMyVisitsButton,
                userVisitsLayout
        );
        showMyVisitsButton.addClickListener(e -> getMyVisits());
        searchForAvailableVisits(searchButton);
        scheduleVisit(scheduleButton, availableVisitLayout);
        cancelUserVisit(cancelUserVisitButton, userVisitLayout);
        setSizeFull();
    }

    private void getMyVisits() {
        userVisitsGrid.setItems(visitsService.getUserVisits(userDto.getId()));
    }

    private void scheduleVisit(Button scheduleButton, VerticalLayout availableVisitLayout) {
        availableGrid.asSingleSelect().addValueChangeListener(event -> {
            if (userDto.getName() != null && userDto.getRoleType() == RoleType.USER) {
                VisitDto selectedVisit = availableGrid.asSingleSelect().getValue();
                if (selectedVisit == null) {
                    return;
                }
                availableVisitLayout.removeAll();
                availableVisitLayout.setVisible(true);
                scheduleButton.setVisible(true);
                visitId = selectedVisit.getId();
                availableVisitLayout.add("Date and time: " + selectedVisit.getDateTime());
                availableVisitLayout.add("Dietician: " + selectedVisit.getDietician().getName() + " " + selectedVisit.getDietician().getLastName());
                scheduleButton.addClickListener(e -> {
                    visitsService.schedule(visitId, userDto.getId());
                    LocalDate localDate = searchedDate.getValue();
                    availableGrid.setItems(visitsService.getAvailableVisits(localDate.toString()));
                    availableGrid.asSingleSelect().clear();
                });
            } else {
                availableVisitLayout.setVisible(false);
                LocalDate localDate = searchedDate.getValue();
                visitsService.getAvailableVisits(localDate.toString());
                availableGrid.asSingleSelect().clear();
            }
        });
    }

    private void searchForAvailableVisits(Button button) {
        button.addClickListener(click -> {
            if (userDto.getName() != null) {
                LocalDate localDate = searchedDate.getValue();
                availableGrid.setItems(visitsService.getAvailableVisits(localDate.toString()));
            }
        });
    }

    private void cancelUserVisit(Button cancelUserVisitButton, VerticalLayout userVisitLayout) {
        userVisitsGrid.asSingleSelect().addValueChangeListener(event -> {
            VisitDto selectedVisit = userVisitsGrid.asSingleSelect().getValue();
            if (selectedVisit == null) {
                return;
            }
            userVisitLayout.removeAll();
            userVisitLayout.setVisible(true);
            userVisitId = selectedVisit.getId();
            userVisitLayout.add("Date and time: " + selectedVisit.getDateTime());
            userVisitLayout.add("Dietician: " + selectedVisit.getDietician().getName() + " " + selectedVisit.getDietician().getLastName() +
                    ",\nphone number " + selectedVisit.getDietician().getPhoneNumber() + ",\n mail: " + selectedVisit.getDietician().getMail());
            userVisitLayout.add(cancelUserVisitButton);
            cancelUserVisitButton.addClickListener(e -> {
                visitsService.cancel(userVisitId);
                LocalDate localDate = searchedDate.getValue();
                availableGrid.setItems(visitsService.getAvailableVisits(localDate.toString()));
                userVisitsGrid.setItems(visitsService.getUserVisits(userDto.getId()));
                userVisitsGrid.asSingleSelect().clear();
                userVisitLayout.setVisible(false);
            });
        });
    }

    public void addLogButtons(UserDto userDto, HorizontalLayout layout) {
        Button logoutButton = new Button("Logout");
        logoutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().setLocation("logout")));
        layout.add(logoutButton);
    }
}
