package repository;

import domein.logging.LogEntry;
import domein.user.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface LogDao extends GenericDao<LogEntry> {
    List<LogEntry> getLogsByAction(String action) throws EntityNotFoundException;
    List<LogEntry> getRecentLogs(int limit) throws EntityNotFoundException;
}