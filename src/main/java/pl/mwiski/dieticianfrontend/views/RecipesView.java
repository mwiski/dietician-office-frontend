package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianDto;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianService;
import pl.mwiski.dieticianfrontend.clients.spoonacular.FoundRecipeDto;
import pl.mwiski.dieticianfrontend.clients.spoonacular.FoundResultDto;
import pl.mwiski.dieticianfrontend.clients.spoonacular.SpoonacularService;
import pl.mwiski.dieticianfrontend.clients.user.RoleType;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;
import pl.mwiski.dieticianfrontend.views.utils.ViewsUtils;

import static pl.mwiski.dieticianfrontend.views.utils.ViewsUtils.*;

@Route("recipes")
public class RecipesView extends VerticalLayout {

    private Grid<FoundResultDto> grid = new Grid<>(FoundResultDto.class);
    private SpoonacularService spoonacularService;

    protected RecipesView(SpoonacularService spoonacularService,
                          UserService userService,
                          DieticianService dieticianService,
                          ViewsUtils viewsUtils) {
        this.spoonacularService = spoonacularService;
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setSizeFull();

        final UserDto userDto = getLoggedUser(userService);
        final DieticianDto dieticianDto = getLoggedDietician(dieticianService);

        TextField query = new TextField();
        query.setClearButtonVisible(true);

        grid.setColumns("title", "readyInMinutes", "servings");

        ListBox listBox = new ListBox();

        listBox.setVisible(false);
        grid.setMinHeight("450px");
        Button search = new Button("Search");
        search.addClickShortcut(Key.ENTER);
        search.addClickListener(click -> {
            grid.setItems(spoonacularService.getRecipes(query.getValue()));
            grid.asSingleSelect().clear();
            listBox.setVisible(false);
        });

        H1 header = new H1("Dietician Office App");
        header.getElement().getThemeList().add("dark");

        Button mainButton = getRoute("Main", "");
        HorizontalLayout layout = new HorizontalLayout(mainButton);
        addLogButtons(userDto, dieticianDto, layout);

        selectRecipe(listBox);

        add(
                layout,
                header,
                new H3("Here you can look for recipes! Enter ingredients that searched recipe should have:"),
                new HorizontalLayout(query, search),
                grid,
                listBox
        );
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
            layout.add(logoutButton);
            layout.add(visitsButton);
        } else {
            layout.add(loginButton);
            layout.add(registration);
        }
    }

    private void selectRecipe(ListBox listBox) {
        grid.asSingleSelect().addValueChangeListener(event -> {
            FoundResultDto foundResultDto = grid.asSingleSelect().getValue();
            if (foundResultDto == null) {

                return;
            }
            listBox.setVisible(true);
            int recipeId = foundResultDto.getId();
            FoundRecipeDto foundRecipeDto = spoonacularService.getRecipe(recipeId);
            String name = "Name: " + foundRecipeDto.getName();
            String vegetarian = "Vegetarian: " + foundRecipeDto.isVegetarian();
            String vegan = "Vegan: " + foundRecipeDto.isVegan();
            String glutenFree = "Gluten free: " + foundRecipeDto.isGlutenFree();
            String dairyFree = "Dairy free: " + foundRecipeDto.isDairyFree();
            String ingredients = "Ingredients: " + String.join(", ", foundRecipeDto.getIngredients());
            String readyInMinutes = "Ready in minutes: " + foundRecipeDto.getReadyInMinutes();
            String servings = "Servings: " + foundRecipeDto.getServings();
            String dishTypes = "Dish types: " + String.join(", ", foundRecipeDto.getDishTypes());
            String instructions = "Instructions: " + foundRecipeDto.getInstructions();
            listBox.setItems(
                    name,
                    vegetarian,
                    vegan,
                    glutenFree,
                    dairyFree,
                    ingredients,
                    readyInMinutes,
                    servings,
                    dishTypes,
                    instructions
                    );
            grid.asSingleSelect().clear();
        });
    }
}
