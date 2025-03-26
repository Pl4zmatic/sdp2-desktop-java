package service;

import domein.machine.Machine;
import domein.site.Site;
import jakarta.persistence.EntityNotFoundException;
import repository.MachineDaoJpa;
import repository.SiteDaoJpa;
import repository.UserDaoJpa;

import java.util.List;

public class SiteService {

    private final SiteDaoJpa siteDao;
    private static SiteService instance;

    public SiteService() {
        this.siteDao = new SiteDaoJpa();
    }

    public static SiteService getInstance() {
        if (instance == null) {
            instance = new SiteService();
        }
        return instance;
    }

    public List<Site> getAllSites() {
        return siteDao.findAll();
    }

    public boolean addSite(Site s) {
        try {
            SiteDaoJpa.startTransaction();
            siteDao.insert(s);
            SiteDaoJpa.commitTransaction();

            return true;
        } catch (Exception e) {

            SiteDaoJpa.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }

    public boolean update(Site s){
        try {
            Site existingSite = siteDao.get(s.getId());
            Site clone = (Site) existingSite.clone();
            System.out.println(clone);
            if (existingSite != null) {

                SiteDaoJpa.startTransaction();
                siteDao.update(s);
                SiteDaoJpa.commitTransaction();

                return true;
            } else {
                throw new EntityNotFoundException("Machine with code " + s.getId() + " not found");
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSite(int code){
        Site existingSite = siteDao.get(code);

        if (existingSite != null) {
            MachineDaoJpa.startTransaction();
            siteDao.softDelete(existingSite);
            MachineDaoJpa.commitTransaction();

            return true;
        } else {
            throw new EntityNotFoundException("Site with code " + code + " not found");
        }
    }

    public Site getSiteByVerantwoordelijke(String naam) {
        try {
            SiteDaoJpa.startTransaction();
            List<Site> allSites = siteDao.findAll();
            SiteDaoJpa.commitTransaction();

            for (Site site : allSites) {
                if (site.getVerantwoordelijke() != null &&
                        site.getVerantwoordelijke().equals(naam)) {
                    return site;
                }
            }
            return null;
        } catch (Exception e) {
            SiteDaoJpa.rollbackTransaction();
            throw new RuntimeException("Error finding site for verantwoordelijke: " + naam, e);
        }
    }
}
