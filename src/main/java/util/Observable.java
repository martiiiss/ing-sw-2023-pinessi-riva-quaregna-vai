package util;

import distributed.messages.Message;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Observable{
    private boolean changed = false;
    private ArrayList<Observer> observers;

    public Observable() {
        observers = new ArrayList<>();
    }


    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    public synchronized void deleteObserver(Observer o) {
        if(o==null) throw new NullPointerException();
        observers.remove(o);
    }

    public void notifyObservers(Consumer<Observer> lambda){
        for(Observer observer: observers){
            lambda.accept(observer);
        }
    }
    public void notifyObservers(Message arg) {
        for (int i = observers.size()-1; i>=0; i--)
            observers.get(i).update(this, arg);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        observers.clear();

    }

    /**
     * Marks this {@code Observable} object as having been changed; the
     * {@code hasChanged} method will now return {@code true}.
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the {@code hasChanged} method will now return {@code false}.
     * This method is called automatically by the
     * {@code notifyObservers} methods.
     *
    
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return  {@code true} if and only if the {@code setChanged}
     *          method has been called more recently than the
     *          {@code clearChanged} method on this object;
     *          {@code false} otherwise.
     * @see     #clearChanged()
     * @see     #setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers of this {@code Observable} object.
     *
     * @return  the number of observers of this object.
     */
    public synchronized int countObservers() {
        return observers.size();
    }
}
