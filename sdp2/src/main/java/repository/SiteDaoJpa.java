package repository;

import domein.machine.Machine;
import domein.site.Site;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public class SiteDaoJpa extends GenericDaoJpa<Site> implements SiteDao {

    public SiteDaoJpa() {
        super(Site.class);
    }


}
