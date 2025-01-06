package igym.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GymTest {

    String name = "Gym Name";
    Gym gym = new Gym();

    @Test
    @DisplayName("should return a empty gym with no name and no identifier") 
    public void testEmptyGym(){
        assertNull(gym.getName(), "Gym name should be null by default");
        assertNull(gym.getId(), "Gym id should be null before persistence");
    }

   @Test
   @DisplayName("should set the name when a valid name is provided")
    public void testSetValidName() {
         gym.setName(name);
         assertEquals(name, gym.getName());
         assertNull(gym.getId(), "Gym id should be null before persistence");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when a null name is provided")
    public void testSetNullName(){
        name = null;
        assertThrows(IllegalArgumentException.class, () -> gym.setName(name));
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when a blank string is provided")
    public void testSetEmptyString(){
        name = "";
        assertThrows(IllegalArgumentException.class, () -> gym.setName(name));
    }

}
