package com.example.unittest;

import com.example.unittest.controllers.UserController;
import com.example.unittest.entities.User;
import com.example.unittest.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class TestUserController {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Test
    void userController_isnotnull() {
        assertThat(userController).isNotNull();
    }

    private User createAUser() throws Exception {
        User user = new User();
        user.setFirstName("Manu");
        user.setLastName("Bassi");
        user.setEmail("Basso@gmail.com");

        return createAUser(user);
    }

    private User createAUser(User user) throws Exception {
        MvcResult result = createUserRequest(user);
        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getId()).isNotNull();

        return userFromResponse;
    }

    private MvcResult createUserRequest() throws Exception {
        User user = new User();
        user.setFirstName("Manu");
        user.setLastName("Bassi");
        user.setEmail("Basso@gmail.com");
        return createUserRequest(user);
    }

    private MvcResult createUserRequest(User user) throws Exception {
        String userJSON = objectMapper.writeValueAsString(user);

        return mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }


    @Test
    void createUserTest_ReturnUser() throws Exception {
        MvcResult result = createUserRequest();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertThat(userFromResponse.getId()).isNotNull();
        assertThat(userFromResponse.getId()).isGreaterThan(0);
    }

    @Test
    void readUserList() throws Exception {
        createUserRequest();

        MvcResult result = mockMvc.perform(get("/user/getAll"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        List<User> userListFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);

        assertThat(userListFromResponse.size()).isNotZero();
    }

    @Test
    void readSingleUser() throws Exception {
        User user = createAUser();
        assertThat(user).isNotNull();


        MvcResult result = mockMvc.perform(get("/user/" + user.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userFromResponse.getId()).isEqualTo(user.getId());
    }

    @Test
    void updatefirstName() throws Exception {
        User user = createAUser();
        assertThat(user).isNotNull();

        String newFirstName = "Modificato";
        user.setFirstName(newFirstName);
        String userJSON = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(put("/user/" + user.getId() + "/updateName" + "?firstName=Modificato")
                        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getFirstName()).isEqualTo(newFirstName);
    }

    @Test
    void updateEmail() throws Exception {
        User user = createAUser();
        assertThat(user).isNotNull();

        String newEmail = "Modificato@gmail.com";
        user.setFirstName(newEmail);
        String userJSON = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(patch("/user/" + user.getId() + "/updateEmail" + "?email=Modificato@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userFromResponse).isNotNull();
        assertThat(userFromResponse.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void deleteUser() throws Exception {
        User user = createAUser();
        assertThat(user).isNotNull();

        this.mockMvc.perform(delete("/user/" + user.getId() + "/delete"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        MvcResult result = mockMvc.perform(get("/user/" + user.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(userFromResponse).isNull();
    }

}
