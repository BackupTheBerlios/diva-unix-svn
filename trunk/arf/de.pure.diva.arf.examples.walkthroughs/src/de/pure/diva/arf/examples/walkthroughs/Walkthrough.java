package de.pure.diva.arf.examples.walkthroughs;

import service.arf.Protocol;
import service.arf.Statistics;

/**
 * Base class for all work package-dependent walkthroughs.
 */
public abstract class Walkthrough {

  /**
   * Runs the walkthrough.
   */
  public abstract void run();

  /**
   * Prints the result of walkthrough
   * 
   * @param answer
   *          The answer of the question.
   * @param protocol
   *          The protocol of reasoning.
   * @param statistics
   *          The statistics of reasoning.
   */
  protected <Answer> void print(Answer answer, Protocol protocol, Statistics statistics) {
    System.out.println("Answer: " + answer);
    if (protocol != null) {
      System.out.println("Used reasoner: " + protocol.getUsedReasoner());
    }
    if (statistics != null) {
      System.out.println("Infos: " + statistics.getInfos());
      System.out.println("Warnings: " + statistics.getWarnings());
      System.out.println("Errors: " + statistics.getErrors());
    }
  }
}
