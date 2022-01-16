package Rest.Services;

public interface ISubject {
    void subscribe(IObserver sub);

    void unsubscribe(IObserver sub);

    void notifySubs();
}
