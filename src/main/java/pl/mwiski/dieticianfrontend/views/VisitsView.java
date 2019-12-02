package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static pl.mwiski.dieticianfrontend.views.utils.ViewsUtils.*;

@Route("visits")
public class VisitsView extends VerticalLayout {

    private Grid<VisitDto> availableGrid = new Grid<>(VisitDto.class);
    private Grid<VisitDto> userVisitsGrid = new Grid<>(VisitDto.class);
    private Grid<VisitDto> dieticianVisitsGrid = new Grid<>(VisitDto.class);
    private Grid<VisitDto> adminGrid = new Grid<>(VisitDto.class);
    private VisitService visitsService;
    private DieticianService dieticianService;
    private long visitId;
    private long userVisitId;
    private long dieticianVisitId;
    private long adminVisitId;
    private UserDto userDto;
    private DieticianDto dieticianDto;
    private DatePicker searchedDate;
    private DatePicker newVisitDate;
    private TimePicker newVisitTime;
    private TextField dieticianLogin;
    private H3 lookForVisitsHeader;
    private H3 lookForVisitsAdminHeader;
    private H4 yourVisitsHeader;
    private H4 addVisitHeader;
    private HorizontalLayout availableVisitsLayout;
    private HorizontalLayout userVisitsLayout;
    private HorizontalLayout dieticianVisitsLayout;
    private HorizontalLayout adminVisitsLayout;
    private Button addVisitByDieticianButton;
    private Button addVisitByAdminButton;
    private Button searchAdminButton;
    private Button searchButton;
    private Button showMyVisitsButton;

    protected VisitsView(VisitService visitsService,
                       UserService userService,
                       DieticianService dieticianService,
                       ViewsUtils viewsUtils) {
        this.visitsService = visitsService;
        this.dieticianService = dieticianService;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        userDto = getLoggedUser(userService);
        dieticianDto = getLoggedDietician(dieticianService);

        searchedDate = new DatePicker("Searched visit date");
        searchedDate.setVisible(false);

        searchButton = new Button("Search");
        searchButton.setVisible(false);
        searchAdminButton = new Button("Search");
        searchAdminButton.setVisible(false);
        showMyVisitsButton = new Button("Show my Visits");
        showMyVisitsButton.setVisible(false);
        Button scheduleButton = new Button("Schedule");
        Button cancelUserVisitButton = new Button("Cancel");
        Button cancelDieticianVisitButton = new Button("Cancel");
        Button cancelVisitButton = new Button("Cancel");
        addVisitByDieticianButton = new Button("Add visit");
        addVisitByDieticianButton.setVisible(false);
        addVisitByAdminButton = new Button("Add visit");
        addVisitByAdminButton.setVisible(false);

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");
        userVisitsGrid.setMinHeight("400px");
        dieticianVisitsGrid.setMinHeight("400px");
        adminGrid.setMinHeight("400px");
        availableGrid.setColumns("dateTime", "dietician.name", "dietician.lastName");
        userVisitsGrid.setColumns("dateTime", "dietician.name", "dietician.lastName", "dietician.phoneNumber", "dietician.mail", "user.name", "user.lastName");
        dieticianVisitsGrid.setColumns("dateTime", "dietician.name", "dietician.lastName", "user.name", "user.lastName", "user.phoneNumber", "user.mail");
        adminGrid.setColumns("id", "dateTime", "dietician.name", "dietician.lastName", "dietician.dietician",  "available");

        Button recipesButton = getRoute("Recipes", "recipes");
        Button mainButton = getRoute("Main", "");
        HorizontalLayout layout = new HorizontalLayout(mainButton, recipesButton);
        addLogButtons(userDto, layout);
        VerticalLayout availableVisitLayout = new VerticalLayout();
        availableVisitLayout.setVisible(false);
        VerticalLayout userVisitLayout = new VerticalLayout();
        userVisitLayout.setVisible(false);
        VerticalLayout dieticianVisitLayout = new VerticalLayout();
        dieticianVisitLayout.setVisible(false);
        VerticalLayout visitLayout = new VerticalLayout();
        visitLayout.setVisible(false);
        newVisitDate = new DatePicker("New Visit date");
        newVisitTime = new TimePicker("New Visit time");
        dieticianLogin = new TextField("Dietician login");
        newVisitDate.setVisible(false);
        newVisitTime.setVisible(false);
        dieticianLogin.setVisible(false);

        availableGrid.setSizeFull();
        availableGrid.setMinHeight("400px");
        availableVisitsLayout = new HorizontalLayout(availableGrid, availableVisitLayout);
        availableVisitsLayout.setSizeFull();
        availableVisitsLayout.setVisible(false);

        userVisitsLayout = new HorizontalLayout(userVisitsGrid, userVisitLayout);
        userVisitsLayout.setSizeFull();
        userVisitsLayout.setVisible(false);
        userVisitLayout.setMinHeight("500px");

        dieticianVisitsLayout = new HorizontalLayout(dieticianVisitsGrid, dieticianVisitLayout);
        dieticianVisitsLayout.setSizeFull();
        dieticianVisitsLayout.setVisible(false);
        dieticianVisitLayout.setMinHeight("500px");

        adminVisitsLayout = new HorizontalLayout(adminGrid, visitLayout);
        adminVisitsLayout.setSizeFull();
        adminVisitsLayout.setVisible(true);
        adminVisitsLayout.setMinHeight("500px");

        lookForVisitsHeader = new H3("Look for visits and schedule them");
        lookForVisitsHeader.setVisible(false);
        yourVisitsHeader = new H4("Look for your visits:");
        yourVisitsHeader.setVisible(false);
        lookForVisitsAdminHeader = new H3("Look for visits");
        lookForVisitsAdminHeader.setVisible(false);
        addVisitHeader = new H4("Add new Visit");
        addVisitHeader.setVisible(false);

        add(
                layout,
                header,
                lookForVisitsHeader,
                lookForVisitsAdminHeader,
                searchedDate,
                searchButton,
                searchAdminButton,
                adminVisitsLayout,
                availableVisitsLayout,
                yourVisitsHeader,
                showMyVisitsButton,
                userVisitsLayout,
                dieticianVisitsLayout,
                addVisitHeader,
                newVisitDate,
                newVisitTime,
                dieticianLogin,
                addVisitByAdminButton,
                addVisitByDieticianButton
        );
        setVisibility();
        showMyVisitsButton.addClickListener(e -> getMyVisits());
        searchForAvailableVisits(searchButton);
        searchAdminButton.addClickListener(e -> getAllVisits());
        scheduleVisit(scheduleButton, availableVisitLayout);
        cancelUserVisit(cancelUserVisitButton, userVisitLayout);
        cancelDieticianVisit(cancelDieticianVisitButton, dieticianVisitLayout);
        cancelVisit(cancelVisitButton, visitLayout);
        addVisitByAdminButton.addClickListener(e -> addVisitByAdmin());
        addVisitByDieticianButton.addClickListener(e -> addVisitByDietcian());
        setSizeFull();
    }

    private void getMyVisits() {
        if (userDto.getName() != null && userDto.getRoleType() == RoleType.USER) {
            userVisitsGrid.setItems(visitsService.getUserVisits(userDto.getId()));
        } else if (dieticianDto.getName() != null) {
            dieticianVisitsGrid.setItems(visitsService.getDieticianVisits(dieticianDto.getId()));
        }
    }

    private void getAllVisits() {
        if (userDto.getName() != null && userDto.getRoleType() == RoleType.ADMIN)
            adminGrid.setItems(visitsService.getAll());
    }

    private void setVisibility() {
        if (userDto.getRoleType() == RoleType.USER) {
            showMyVisitsButton.setVisible(true);
            searchedDate.setVisible(true);
            searchButton.setVisible(true);
            availableGrid.setVisible(true);
            lookForVisitsHeader.setVisible(true);
            yourVisitsHeader.setVisible(true);
            availableVisitsLayout.setVisible(true);
            userVisitsLayout.setVisible(true);
        } else if (dieticianDto.getName() != null) {
            showMyVisitsButton.setVisible(true);
            searchedDate.setVisible(true);
            searchButton.setVisible(true);
            availableGrid.setVisible(true);
            newVisitDate.setVisible(true);
            newVisitTime.setVisible(true);
            addVisitByDieticianButton.setVisible(true);
            lookForVisitsHeader.setVisible(true);
            yourVisitsHeader.setVisible(true);
            availableVisitsLayout.setVisible(true);
            addVisitHeader.setVisible(true);
            dieticianVisitsLayout.setVisible(true);
        } else if (userDto.getRoleType() == RoleType.ADMIN) {
            searchAdminButton.setVisible(true);
            newVisitDate.setVisible(true);
            newVisitTime.setVisible(true);
            dieticianLogin.setVisible(true);
            addVisitByAdminButton.setVisible(true);
            lookForVisitsAdminHeader.setVisible(true);
            addVisitHeader.setVisible(true);
            adminVisitsLayout.setVisible(true);
        }
    }

    private void scheduleVisit(Button scheduleButton, VerticalLayout availableVisitLayout) {
        availableGrid.asSingleSelect().addValueChangeListener(event -> {
            if (userDto.getName() != null && userDto.getRoleType() == RoleType.USER) {
                VisitDto selectedVisit = availableGrid.asSingleSelect().getValue();
                if (selectedVisit == null) {
                    return;
                }
                availableVisitLayout.setVisible(true);
                visitId = selectedVisit.getId();
                availableVisitLayout.add("Date and time: " + selectedVisit.getDateTime());
                availableVisitLayout.add("Dietician: " + selectedVisit.getDietician().getName() + " " + selectedVisit.getDietician().getLastName());
                availableVisitLayout.add(scheduleButton);
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
            if (userDto.getName() != null || dieticianDto.getName() != null) {
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

    private void cancelDieticianVisit(Button cancelDieticianVisitButton, VerticalLayout dieticianVisitLayout) {
        dieticianVisitsGrid.asSingleSelect().addValueChangeListener(event -> {
            VisitDto selectedVisit = dieticianVisitsGrid.asSingleSelect().getValue();
            if (selectedVisit == null) {
                return;
            }
            dieticianVisitLayout.setVisible(true);
            dieticianVisitId = selectedVisit.getId();
            dieticianVisitLayout.add("Date and time: " + selectedVisit.getDateTime());
            dieticianVisitLayout.add("User: " + selectedVisit.getUser().getName() + " " + selectedVisit.getUser().getLastName() +
                    ",\nphone number " + selectedVisit.getUser().getPhoneNumber() + ",\n mail: " + selectedVisit.getUser().getMail());
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
        if (userDto.getName() != null && userDto.getRoleType() == RoleType.ADMIN) {
            layout.add(adminButton);
        }
        layout.add(logoutButton);

    }
}
