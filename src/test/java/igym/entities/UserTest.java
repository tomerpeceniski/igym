package igym.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void userCreationTest() {
        User user = new User();
        String name = "joao pedro";
        user.setName(name);

        assertEquals(user.getName(), name);
    }

}
