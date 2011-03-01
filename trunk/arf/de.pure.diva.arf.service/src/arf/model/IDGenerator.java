package arf.model;

import java.security.SecureRandom;

public class IDGenerator {

  private static final SecureRandom m_Rand  = new SecureRandom();
  private static final String       IDCHARS = "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "-_";

  /**
   * Generate a new identifier.
   */
  public static String generate() {
    byte[] rand = new byte[16];
    m_Rand.nextBytes(rand);
    StringBuffer strbuf = new StringBuffer("ID");
    for (int idx = 0; idx < rand.length; ++idx) {
      int charOff = rand[idx] & 0x3f;
      strbuf.append(IDCHARS.charAt(charOff));
    }
    return strbuf.toString();
  }
}
