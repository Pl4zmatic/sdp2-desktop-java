package repository;

import domein.logging.LogEntry;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class LogDaoJpa extends GenericDaoJpa<LogEntry> implements LogDao {
    public LogDaoJpa() {
        super(LogEntry.class);
    }

    @Override
    public List<LogEntry> getLogsByAction(String action) {
        try {
            return  em.createQuery("SELECT l FROM LogEntry l WHERE l.action = :action ORDER BY l.timestamp DESC", LogEntry.class)
                    .setParameter("action", action)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<LogEntry> getRecentLogs(int limit) {
        try {
            return  em.createQuery("SELECT l FROM LogEntry l ORDER BY l.timestamp DESC", LogEntry.class)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
