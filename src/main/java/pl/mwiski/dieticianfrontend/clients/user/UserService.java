package pl.mwiski.dieticianfrontend.clients.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserClient userClient;

    public List<UserDto> getAll() {
        return userClient.getAll();
    }

    public UserDto get(final long userId) {
        return userClient.get(userId);
    }

    public Optional<UserDto> getUserByLogin(final String username) {
        if (username.equals("")) {
            return Optional.empty();
        }
        return userClient.getUserByLogin(username);
    }

    public UserDto add(final UserDto userDto) {
        return userClient.add(userDto);
    }

    public void addAdmin(final UserDto userDto) {
        userClient.addAdmin(userDto);
    }

    public UserDto update(final UserDto userDto) {
        return userClient.update(userDto);
    }

    public void delete(final long userId) {
        userClient.delete(userId);
    }
}
