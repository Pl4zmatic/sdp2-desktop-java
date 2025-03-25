import domein.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Rollen;

import java.time.LocalDate;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    private User user;

    @BeforeEach
    public void setUp(){
        user = new User("Test", "De Tester", LocalDate.of(2000, 3, 25),"test@mail.com", "test", "Testlaan", "123", Rollen.TECHNIEKER);
    }

    @Test
    public void testConstructor(){
        assertNotNull(user);
        assertEquals("Test", user.getFirstName());
        assertEquals("De Tester", user.getLastName());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("Testlaan", user.getAdres());
        assertEquals("123", user.getGsmNummer());
        assertEquals(Rollen.TECHNIEKER, user.getRol());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", ""})
    void testSetFirstNameInvalid(String input) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setFirstName(input));
        assertTrue(exception.getMessage().contains("has to be filled in"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jane", "Alice"})
    void testSetFirstNameValid(String input) {
        user.setFirstName(input);
        assertEquals(input, user.getFirstName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", ""})
    void testSetLastNameInvalid(String input) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setLastName(input));
        assertTrue(exception.getMessage().contains("has to be filled in"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Doe", "Smith"})
    void testSetLastNameValid(String input) {
        user.setLastName(input);
        assertEquals(input, user.getLastName());
    }

    @ParameterizedTest
    @MethodSource("provideGsmNummerForTechnieker")
    void testSetGsmNummerForTechnieker(String gsm, boolean isValid) {
        user.setRol(Rollen.TECHNIEKER);
        if (isValid) {
            user.setGsmNummer(gsm);
            assertEquals(gsm, user.getGsmNummer());
        } else {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setGsmNummer(gsm));
            assertTrue(exception.getMessage().contains("has to be filled in"));
        }
    }

    private static Stream<Object[]> provideGsmNummerForTechnieker() {
        return Stream.of(
                new Object[]{"0123456789", true},
                new Object[]{null, false},
                new Object[]{"", false},
                new Object[]{" ", false}
        );
    }

    @ParameterizedTest
    @EnumSource(Rollen.class)
    void testSetRolValid(Rollen rol) {
        user.setRol(rol);
        assertEquals(rol, user.getRol());
    }

    @Test
    void testSetRolInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.setRol(null));
        assertEquals("Role has to be filled in", exception.getMessage());
    }

    @Test
    void testPasswordHashing() {
        user.setPassword("newpassword");
        assertTrue(user.getPassword().startsWith("$2a$"));
    }

    @Test
    void testGetFullName() {
        assertEquals("Test De Tester", user.getFullName());
    }
}
