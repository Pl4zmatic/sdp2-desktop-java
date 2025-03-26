import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;


import domein.machine.Machine;
import domein.machine.Maintenance;

public class MaintenanceTest {

    private Maintenance maintenance;
    private Machine machine;

//    @BeforeEach
//    public void setUp() {
//        machine = new Machine();
//        maintenance = new Maintenance(
//            machine.getCode(),
//            LocalDateTime.of(2025, 4, 17, 8, 0, 0),
//            LocalDateTime.of(2025, 4, 17, 19, 0, 0),
//            "John Doe", "Rusted part", "Report"
//        );
//    }

    @Test
    public void testConstructor() {
        assertNotNull(maintenance);
        assertNotNull(machine);
        assertEquals(LocalDateTime.of(2025, 4, 17, 8, 0, 0), maintenance.getStartDate());
        assertEquals(LocalDateTime.of(2025, 4, 17, 19, 0, 0), maintenance.getEndDate());
        assertEquals("John Doe", maintenance.getNameTechnician());
        assertEquals("Rusted part", maintenance.getReason());
        assertEquals("Report", maintenance.getMaintenanceReport());
        assertEquals(null, maintenance.getRemarks());
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"John Doe", "John", "Doe"})
//    public void testStringSettersValid(String name) {
//        maintenance.setNameTechnician(name);
//        assertEquals(name, maintenance.getNameTechnician());
//    }

//    @ParameterizedTest
//    @NullAndEmptySource
//    @ValueSource(strings = {"", " ", "  "})
//    public void testStringSettersInvalid(String name) {
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> maintenance.setNameTechnician(name));
//        assertEquals(String.format("%s has to be filled in", name), exception.getMessage());
//    }

//    @ParameterizedTest
//    @MethodSource("generatorValidDates")
//    public void testDateSettersValid(LocalDateTime localDateTime) {
//        maintenance.setStartDate(localDateTime);
//        assertEquals(localDateTime, maintenance.getStartDate());
//    }

    private static Stream<LocalDateTime> generatorValidDates() {
        return Stream.of(
            LocalDateTime.of(2025, 4, 3, 8, 0),
            LocalDateTime.of(2025, 5, 2, 17, 0),
            LocalDateTime.of(2025, 6, 6, 7, 0)
        );
    }

//    @ParameterizedTest
//    @MethodSource("generatorInvalidDates")
//    public void testDateSettersInvalid(LocalDateTime localDateTime) {
//        Exception exception = assertThrows(DateTimeException.class, () -> maintenance.setStartDate(localDateTime));
//        assertEquals("The datetime is from the past", exception.getMessage());
//    }

    private static Stream<LocalDateTime> generatorInvalidDates() {
        return Stream.of(
            LocalDateTime.of(2024, 4, 3, 14, 0, 0),
            LocalDateTime.of(2023, 7, 7, 7, 0, 0),
            LocalDateTime.of(2025, 3, 12, 8, 0, 0)
        );
    }
}
