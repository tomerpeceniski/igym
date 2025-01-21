package igym.controller;

import igym.controllers.GymController;
import igym.entities.Gym;
import igym.services.GymService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GymController.class)  
@ExtendWith(MockitoExtension.class)
public class GymControllerTest {

    @Autowired
    private MockMvc mockMvc;  

    @SuppressWarnings("removal")
    @MockBean
    private GymService gymService;  

    @InjectMocks
    private GymController gymController;  

    @Test
    void testFindAllGymsSuccess() throws Exception {

        Gym gym1 = new Gym( "Location 1");
        Gym gym2 = new Gym("Location 2");
        List<Gym> gyms = Arrays.asList(gym1, gym2);

        when(gymService.findAllGyms()).thenReturn(gyms); 

        mockMvc.perform(get("/api/gyms"))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$[0].name").value("Location 1"))  
                .andExpect(jsonPath("$[1].name").value("Location 2")).andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testFindAllGymsNoContent() throws Exception {

        List<Gym> gyms = Arrays.asList();  

        when(gymService.findAllGyms()).thenReturn(gyms); 

        mockMvc.perform(get("/api/gyms"))
                .andExpect(status().isNoContent()).andDo(MockMvcResultHandlers.print()); 
    }

    @Test
    void testFindAllGymsError() throws Exception {

        when(gymService.findAllGyms()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/gyms"))
                .andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print()); 
    }
}