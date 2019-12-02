package pl.mwiski.dieticianfrontend.views.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianDto;
import pl.mwiski.dieticianfrontend.clients.dietician.DieticianService;
import pl.mwiski.dieticianfrontend.clients.user.UserDto;
import pl.mwiski.dieticianfrontend.clients.user.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ViewsUtils {

    private static String getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public static UserDto getLoggedUser(final UserService userService) {
        Optional<UserDto> userDto;
        String username = getAuthUser();
        if (username != null && !username.equals("anonymousUser")) {
            userDto = userService.getUserByLogin(username);
            return userDto.orElseGet(UserDto::new);
        }
        return new UserDto();
    }

    public static DieticianDto getLoggedDietician(final DieticianService dieticianService) {
        Optional<DieticianDto> dieticianDto;
        String username = getAuthUser();
        if (username != null && !username.equals("anonymousUser")) {
            dieticianDto = dieticianService.getDieticianByLogin(username);
            return dieticianDto.orElseGet(DieticianDto::new);
        }
        return new DieticianDto();
    }

    public static Button getRoute(final String name, final String location) {
        Button button = new Button(name);
        button.addClickListener(e ->
                button.getUI().ifPresent(ui ->
                        ui.navigate(location))
        );
        return button;
    }

    public static int convertStringToInt(final String input) {

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

    public static void validateFields(final List<TextField> inputs) {
        List<TextField> emptyFields = inputs.stream()
                .filter(i -> i.getValue().equals(""))
                .collect(Collectors.toList());
        emptyFields.forEach(f -> {
            Notification.show(f.getLabel() + " field is empty!");
        });
    }

    public static String dateToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Date of visit cannot be empty!");
        }
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
