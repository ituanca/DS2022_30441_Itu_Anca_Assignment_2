//package ro.tuc.ds2022.services;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.jdbc.Sql;
//import ro.tuc.ds2022.Ds2022TestConfig;
//import ro.tuc.ds2022.dtos.PersonDTO;
//import ro.tuc.ds2022.dtos.PersonDetailsDTO;
//
//import static org.springframework.test.util.AssertionErrors.assertEquals;
//
//import java.util.List;
//import java.util.UUID;
//
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/test-sql/create.sql")
//@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/test-sql/delete.sql")
//public class PersonServiceIntegrationTests extends Ds2022TestConfig {
//
//    @Autowired
//    PersonService personService;
//
////    @Test
////    public void testGetCorrect() {
////        List<PersonDetailsDTO> personDTOList = personService.findPersons();
////        assertEquals("Test Insert Person", 1, personDTOList.size());
////    }
//
////    @Test
////    public void testInsertCorrectWithGetById() {
////        PersonDetailsDTO p = new PersonDetailsDTO("John", "john","Somewhere Else street", 22, "client");
////        String response = personService.insert(p);
////
////        PersonDetailsDTO insertedPerson = new PersonDetailsDTO(p.getId(), p.getName(), p.getUsername(), p.getAddress(), p.getAge());
////        PersonDetailsDTO fetchedPerson = personService.findPersonById(p.getId());
////
////        assertEquals("Test Inserted Person", insertedPerson, fetchedPerson);
////    }
//
////    @Test
////    public void testInsertCorrectWithGetAll() {
////        PersonDetailsDTO p = new PersonDetailsDTO("John", "john","Somewhere Else street", 22, "client");
////        personService.insert(p);
////
////        List<PersonDetailsDTO> personDTOList = personService.findPersons();
////        assertEquals("Test Inserted Persons", 2, personDTOList.size());
////    }
//}
