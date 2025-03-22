package domein.polling;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

import domein.Session;
import domein.notification.Notification;
import service.ServiceController;
import utils.Observer;
import utils.Subject;

public class NotificationPoller implements Subject {


    private static NotificationPoller instance;
    private static final long POLL_INTERVAL = 5000; // Poll every 5 seconds (adjust as needed) 5sec = 5000
    private static Timer timer;
    private final ServiceController sc;

    private List<Observer> observers;

    // Constructor to initialize the NotificationService
    public NotificationPoller() {
        this.sc = ServiceController.getInstance();
        this.observers = new ArrayList<>();
    }

    public static NotificationPoller getInstance(){
        if(instance == null){
            instance = new NotificationPoller();
        }
        return instance;
    }

    // Start the polling process
    public void startPolling() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                notifyObserver();
            }
        }, 0, POLL_INTERVAL);
    }

    // Fetch new notifications for a specific user
    private void notifyObserver(){
        if(sc.hasNotificationsByUser(Session.getCurrentUser())){
            observers.forEach(o -> o.update(true));
            System.out.println(true);
            return;
        }
        observers.forEach(o -> o.update(false));
        System.out.println(false);
    }

    // Stop the polling process
    public void stopPolling() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void addObserver(Observer e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer e) {
        observers.remove(e);
    }
}
