package de.pure.diva.arf.examples.walkthroughs.dt;

import de.pure.diva.arf.examples.walkthroughs.Walkthrough;

public class WP2WalkthroughRunner {

  /**
   * Main method for the WP2 walkthrough.
   * 
   * @param args
   *          The arguments, which are ignored.
   */
  public static void main(String[] args) {
    // create walkthrough for WP2
    Walkthrough wt = new WP2Walkthrough();
    // run walkthrough
    wt.run();
  }

}
