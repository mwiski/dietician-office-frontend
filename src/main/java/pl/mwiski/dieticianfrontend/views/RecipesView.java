package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.mwiski.dieticianfrontend.clients.spoonacular.FoundResultDto;
import pl.mwiski.dieticianfrontend.clients.spoonacular.SpoonacularService;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;

import static pl.mwiski.dieticianfrontend.views.ViewsUtils.getRoute;

@Route("recipes")
public class RecipesView extends VerticalLayout {

    private Grid<FoundResultDto> grid = new Grid<>(FoundResultDto.class);

    protected RecipesView(SpoonacularService spoonacularService,
                          UserService userService,
                          ViewsUtils viewsUtils) {
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setSizeFull();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDto userDto = new UserDto();
        if (username != null && !username.equals("anonymousUser")) {
            userDto = userService.getUserByName(username);
        }
        final UserDto finalUserDto = userDto;

        TextField query = new TextField();
        query.setClearButtonVisible(true);

        Button search = new Button("Search");
        search.addClickShortcut(Key.ENTER);
        search.addClickListener(click -> {
            grid.setItems(spoonacularService.getRecipes(query.getValue()));
        });

        grid.setColumns("title", "readyInMinutes", "servings");

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");

        Button mainButton = getRoute("Main", "");
        HorizontalLayout layout = new HorizontalLayout(mainButton);
        addLogButtons(userDto, layout);

        add(
                layout,
                header,
                new H3("Here you can look for recipes! Enter ingredients that searched recipe should have:"),
                new HorizontalLayout(
                        query,
                        search
                ),
                grid
        );
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
}
