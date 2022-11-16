//package ro.tuc.ds2022.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ro.tuc.ds2022.Ds2022TestConfig;
//import ro.tuc.ds2022.dtos.PersonDetailsDTO;
//import ro.tuc.ds2022.services.PersonService;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//public class PersonControllerUnitTest extends Ds2022TestConfig {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private PersonService service;
//
////    @Test
////    public void insertPersonTest() throws Exception {
////        ObjectMapper objectMapper = new ObjectMapper();
////        PersonDetailsDTO personDTO = new PersonDetailsDTO("John", "john","Somewhere Else street", 22, "client");
////
////        mockMvc.perform(post("/person")
////                .content(objectMapper.writeValueAsString(personDTO))
////                .contentType("application/json"))
////                .andExpect(status().isCreated());
////    }
//
////    @Test
////    public void insertPersonTestFailsDueToAge() throws Exception {
////        ObjectMapper objectMapper = new ObjectMapper();
////        PersonDetailsDTO personDTO = new PersonDetailsDTO("John", "john", "Somewhere Else street", 17, "client");
////
////        mockMvc.perform(post("/person")
////                .content(objectMapper.writeValueAsString(personDTO))
////                .contentType("application/json"))
////                .andExpect(status().isBadRequest());
////    }
//
////    @Test
////    public void insertPersonTestFailsDueToNull() throws Exception {
////        ObjectMapper objectMapper = new ObjectMapper();
////        PersonDetailsDTO personDTO = new PersonDetailsDTO("John", "john",null, 17, "client");
////
////        mockMvc.perform(post("/person")
////                .content(objectMapper.writeValueAsString(personDTO))
////                .contentType("application/json"))
////                .andExpect(status().isBadRequest());
////    }
//}
