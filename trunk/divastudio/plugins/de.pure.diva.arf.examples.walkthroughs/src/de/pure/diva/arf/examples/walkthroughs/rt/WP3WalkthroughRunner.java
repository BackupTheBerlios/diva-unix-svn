package de.pure.diva.arf.examples.walkthroughs.rt;

import de.pure.diva.arf.examples.walkthroughs.Walkthrough;

public class WP3WalkthroughRunner {

  /**
   * Main method for the WP3 walkthrough.
   * 
   * @param args
   *          The arguments, which are ignored.
   */
  public static void main(String[] args) {
    // create walkthrough for WP3
    Walkthrough wt = new WP3Walkthrough();
    // run walkthrough
    wt.run();
  }

}
