import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domein.machine.Machine;
import domein.machine.Maintenance;
import domein.machine.report.Report;
import domein.machine.stateMachines.maintenance.PlannedState;
import domein.user.User;

import java.time.LocalDate;
import java.time.DateTimeException;

class MaintenanceTest {

    private Machine mockMachine; 
    private Report mockReport;   
    private Maintenance maintenance;

    @BeforeEach
    void setUp() {
        mockMachine = new Machine(); 
        mockReport = new Report();  
        maintenance = new Maintenance(mockMachine, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5),
                "Regular Maintenance", mockReport, "Remarks");
    }

    @Test
    void testMaintenanceConstructor() {
        assertNotNull(maintenance);
        assertEquals(mockMachine, maintenance.getMachine());
        assertEquals("Regular Maintenance", maintenance.getReason());
        assertEquals(mockReport, maintenance.getMaintenanceReport());
        assertEquals("Remarks", maintenance.getRemarks());
    }

    @Test
    void testSetStartDate_validDate() {
        LocalDate newStartDate = LocalDate.now().plusDays(2);
        maintenance.setStartDate(newStartDate);
        assertEquals(newStartDate, maintenance.getStartDate());
    }

    @Test
    void testSetStartDate_invalidDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        DateTimeException thrown = assertThrows(DateTimeException.class, () -> maintenance.setStartDate(pastDate));
        assertEquals("The datetime is from the past", thrown.getMessage());
    }

    @Test
    void testSetEndDate_validDate() {
        LocalDate newEndDate = LocalDate.now().plusDays(7);
        maintenance.setEndDate(newEndDate);
        assertEquals(newEndDate, maintenance.getEndDate());
    }

    @Test
    void testSetEndDate_invalidDateBeforeStartDate() {
        LocalDate invalidEndDate = LocalDate.now().plusDays(1);
        maintenance.setStartDate(LocalDate.now().plusDays(2));
        DateTimeException thrown = assertThrows(DateTimeException.class, () -> maintenance.setEndDate(invalidEndDate));
        assertEquals("Het opgegeven eindtijdstip ligt voor de startdatum", thrown.getMessage());
    }

    @Test
    void testSetReason_validReason() {
        String validReason = "New Maintenance";
        maintenance.setReason(validReason);
        assertEquals(validReason, maintenance.getReason());
    }

    @Test
    void testSetReason_invalidReason_empty() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> maintenance.setReason(""));
        assertEquals(" has to be filled in", thrown.getMessage());
    }

    @Test
    void testSetReason_invalidReason_null() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> maintenance.setReason(null));
        assertEquals("null has to be filled in", thrown.getMessage());
    }

    @Test
    void testSetCurrentState() {
        PlannedState newState = new PlannedState(maintenance);
        maintenance.setCurrentState(newState);
        assertEquals(newState.toString(), maintenance.getCurrentState());
    }

    @Test
    void testMaintenanceToString() {
        String result = maintenance.toString();
        assertTrue(result.contains(String.valueOf(maintenance.getMaintenanceId())));
        assertTrue(result.contains(maintenance.getCurrentState()));
        assertTrue(result.contains(maintenance.getStartDate().toString()));
    }

    @Test
    void testMaintenanceCopyConstructor() {
        Maintenance copiedMaintenance = new Maintenance(maintenance);
        assertEquals(maintenance.getMachine(), copiedMaintenance.getMachine());
        assertEquals(maintenance.getStartDate(), copiedMaintenance.getStartDate());
        assertEquals(maintenance.getEndDate(), copiedMaintenance.getEndDate());
    }
}
