package igym.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserEntityTest {

    @Test
    @DisplayName("Testing getting user name")
    public void userNameTest() {
        String name1 = "Maria Clown";
        UserEntity user1 = new UserEntity();
        user1.setName(name1);
        assertEquals(user1.getName(), name1);

        String name2 = "John Textor";
        UserEntity user2 = new UserEntity(name2);
        assertEquals(user2.getName(), name2);

        UserEntity user3 = new UserEntity();
        assertNull(user3.getName());
    }

    @Test
    @DisplayName("Testing ID from UserEntity")
    public void userIdTest() {
        String name1 = "Maria Clown";
        UserEntity user1 = new UserEntity(name1);
        assertNull(user1.getId(), "When a UserEntity is created, the ID should be null");
    }

    @Test
    @DisplayName("Testing comparing objects UserEntity")
    public void userComparationTest() {
        String name1 = "Maria Clown";
        UserEntity user1 = new UserEntity(name1);
        UserEntity user2 = new UserEntity(name1);
        assertFalse(user1.equals(user2));

        user2 = user1;
        assertTrue(user1.equals(user2));
    }
}
