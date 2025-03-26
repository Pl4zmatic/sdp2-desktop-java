import domein.machine.Machine;
import domein.site.Site;
import domein.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Rollen;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MachineTest {

    private Machine machine;
    private Site site;
    private User technieker;

    @BeforeEach
    public void setUp() {
        site = new Site("TestSite", "TestAddress", "TestVerantwoordelijke");
        technieker = new User("John", "Doe", LocalDate.of(1990, 5, 15), "john.doe@example.com", "password", "TestStreet", "987654321", Rollen.TECHNIEKER);
        machine = new Machine(site, "M001", "TestLocatie", "ProductInfo", "Operational", technieker);
    }


    @Test
    public void testConstructor() {
        assertNotNull(machine);
        assertEquals("M001", machine.getCode());
        assertEquals("TestLocatie", machine.getLocatie());
        assertEquals("ProductInfo", machine.getProductInfo());
        assertEquals("Operational", machine.getProductieStatus());
        assertEquals(0, machine.getUptime());
        assertEquals(site.getName(), machine.getSiteNaam());
        assertEquals("John Doe", machine.getTechnieker().getFullName());
    }

    @Test
    public void testStartMachine() {
        machine.startMachine();
        assertEquals("running", machine.getCurrentState());
    }

    @Test
    public void testStopMachine() {
        machine.stopMachine();
        assertEquals("stopped", machine.getCurrentState());
    }

    @Test
    public void testUpdateUptime() {
        machine.setStartDate(LocalDateTime.now().minusHours(5));
        machine.updateUptime();
        assertEquals(5, machine.getUptime());
    }

    @Test
    public void testGetFullMachineDetails() {
        machine.setStartDate(LocalDateTime.now().minusHours(10));
        String machineDetails = machine.toString();
        assertTrue(machineDetails.contains("M001"));
        assertTrue(machineDetails.contains("TestSite"));
        assertTrue(machineDetails.contains("TestLocatie"));
        assertTrue(machineDetails.contains("Operational"));
        assertTrue(machineDetails.contains("10"));
    }

    @Test
    public void testPostLoad() {
        machine.setCurrentStateString("running");
        machine.postLoad();
        assertEquals("running", machine.getCurrentState());
        assertTrue(machine.getUptime() >= 0);
    }
}
