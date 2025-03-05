package domein.machine;

public interface Subject {

	public void addObserver(Observer observer);
	public void removeObserver(Observer observer);
}
