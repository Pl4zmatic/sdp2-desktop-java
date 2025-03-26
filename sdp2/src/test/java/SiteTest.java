import domein.site.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SiteTest {

    private Site site;

    @BeforeEach
    public void setUp() {
        site = new Site("Main Site", "123 Street", "John Doe");
    }

    @Test
    void testConstructor() {
        assertNotNull(site);
        assertEquals("Main Site", site.getName());
        assertEquals("123 Street", site.getAddress());
        assertEquals("John Doe", site.getVerantwoordelijke());
        assertNotNull(site.getMachines());
    }

    @Test
    void testSetDeleted() {
        site.setDeleted(true);
        assertTrue(site.getDeleted());
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        Site siteClone = (Site) site.clone();
        assertNotSame(site, siteClone);
        assertEquals(site.getName(), siteClone.getName());
        assertEquals(site.getAddress(), siteClone.getAddress());
        assertEquals(site.getVerantwoordelijke(), siteClone.getVerantwoordelijke());
    }
}
