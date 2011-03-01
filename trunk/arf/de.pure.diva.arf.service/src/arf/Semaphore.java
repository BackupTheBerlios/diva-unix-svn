package arf;

/**
 * Implementation of classical Dykstra semaphores.
 */
public class Semaphore {
  /**
   * The counter value for this counting semaphore. The value is initialized by
   * the constructor and modified by acquire and release.
   */
  private int m_Counter;

  /**
   * Constructor to create a semaphore object. The initial counter for the
   * semaphore is zero.
   */
  public Semaphore() {
    m_Counter = 0;
  }

  /**
   * Constructor to create a semaphore object.
   * 
   * @param counter
   *          int The initial value for the semaphore counter.
   */
  public Semaphore(int counter) {
    m_Counter = counter;
  }

  /**
   * The acquire method decrements the semaphore counter. If the counter value
   * is zero before being decremented, the thread will be suspended until
   * another thread increments the counter with the release method.
   */
  public void acquire() {
    if (decCounter() < 0) {
      dowait();
    }
  }

  /**
   * The release method increments the semaphore counter. If other threads are
   * suspended on this semaphore, one of them will be activated.
   */
  public void release() {
    if (incCounter() >= 0) {
      donotify();
    }
  }

  protected synchronized int getCounter() {
    return m_Counter;
  }

  protected synchronized int incCounter() {
    return ++m_Counter;
  }

  protected synchronized int decCounter() {
    return --m_Counter;
  }

  protected synchronized void donotify() {
    notify();
  }

  /**
   * We wait for the notification.
   */
  protected synchronized void dowait() {
    try {
      wait();
    }
    catch (InterruptedException e) {/* ignored */}
  }
}
