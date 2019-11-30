package pl.mwiski.dieticianfrontend.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewsUtils {

    private static String getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public static UserDto getLoggedUser(UserService userService) {
        UserDto userDto = new UserDto();
        String username = getAuthUser();
        if (username != null && !username.equals("anonymousUser")) {
            userDto = userService.getUserByName(username);
        }
        return userDto;
    }

    public static Button getRoute(String name, String location) {
        Button button = new Button(name);
        button.addClickListener(e ->
                button.getUI().ifPresent(ui ->
                        ui.navigate(location))
        );
        return button;
    }

    public static int convertStringToInt(String input) {

        int i = 0;
        try {
            i = Integer.parseInt(input.trim());
        } catch (NumberFormatException nfe)
        {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }

        if (i == 0) {
            Notification.show("Wrong format for age!");
            throw new IllegalArgumentException("Wrong format for age!");
        } else if (i < 1) {
            Notification.show("Age cannot be smaller than 1!");
            throw new IllegalArgumentException("Age cannot be smaller than 1!");
        } else if (i > 100) {
            Notification.show("Age cannot be bigger than 100!");
            throw new IllegalArgumentException("Age cannot be bigger than 100!");
        }
        return i;
    }

    public static void validateFields(List<TextField> inputs) {
        List<TextField> emptyFields = inputs.stream()
                .filter(i -> i.getValue().equals(""))
                .collect(Collectors.toList());
        emptyFields.forEach(f -> {
            Notification.show(f.getLabel() + " field is empty!");
        });
    }
}
