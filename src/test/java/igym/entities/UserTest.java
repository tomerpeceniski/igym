package igym.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void userCreationTest() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        String name = "joao pedro";
        user.setName(name);

        assertEquals(user.getId(), id);
        assertEquals(user.getName(), name);
    }

    @Test
    public void equalUserTest() {
        UUID id = UUID.randomUUID();
        User user1 = new User(id, "Jonny Segal");
        User user2 = new User(id, "Donald Biden");

        assertEquals(user1, user2);
    }
}
