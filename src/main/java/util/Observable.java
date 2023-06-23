package util;

import distributed.messages.Message;
import java.util.ArrayList;
import java.util.function.Consumer;

/**Class that represents the Observable*/
public class Observable{
    private boolean changed = false;
    private ArrayList<Observer> observers;

    /**Constructor of the Class. <br>
     * This creates an <code>ArrayList</code> of observers.*/
    public Observable() {
        this.observers = new ArrayList<>();
    }

    /**
     * <p>
     *     Method that given an <code>Observer</code> as a parameter, adds it to the list of observers.
     * </p>
     * @param o an <code>Observer</code>
     * @throws NullPointerException if the given parameter <code>o==null</code>*/
    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!this.observers.contains(o)) {
            this.observers.add(o);
            System.out.println("aggiungo observer " + this.observers.get(0) + " a " + this);
        }
    }


    public ArrayList<Observer> getObservers () { return this.observers; }

    /**
     * <p>
     *     Method that given an <code>Observer</code> removes it from the list of observers.
     * </p>
     * @param o an <code>Observer</code>
     * @throws NullPointerException if the given parameter <code>o==null</code>*/
    public synchronized void deleteObserver(Observer o) {
        if(o==null) throw new NullPointerException();
        observers.remove(o);
    }

    /**
     * <p>
     *     Method that, given a parameter accepts its input.
     * </p>
     * @param lambda a {@link Consumer} with <code>Observer</code> as parameter*/
    public void notifyObservers(Consumer<Observer> lambda){
        for(Observer observer: this.observers){
            lambda.accept(observer);
        }
    }

    /**
     * <p>
     *     Method that given a {@link Message} as a parameter calls the update.
     * </p>
     * @param arg a <code>Message</code> created by the Class that calls this method*/
    public void notifyObservers(Message arg) {
        System.out.println("notify observer " + this + " e size " + this.observers.size());
        for (int i = 0; i<this.observers.size(); i++) {
            System.out.println("notifyObserver!" + i);
            this.observers.get(i).update(this, arg);
        }
    }

    /**
     * Method that clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {this.observers.clear();}

    /**
     * <p>
     *     Method that marks this {@code Observable} object as having been changed; the
     *     {@code hasChanged} method will now return {@code true}.
     * </p>
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
