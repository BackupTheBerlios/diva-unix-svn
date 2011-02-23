package de.pure.diva.arf.examples.walkthroughs.re;

import de.pure.diva.arf.examples.walkthroughs.Walkthrough;

public class WP1WalkthroughRunner {

  /**
   * Main method for the WP1 walkthrough.
   * 
   * @param args
   *          The arguments, which are ignored.
   */
  public static void main(String[] args) throws Exception {
    // create walkthrough for WP1
    Walkthrough wt = new WP1Walkthrough();
    // run walkthrough
    wt.run();
  }

}
