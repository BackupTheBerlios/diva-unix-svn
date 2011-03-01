package de.pure.diva.arf.pm.reasoner.util;

import java.util.HashSet;
import java.util.Set;

public class ARFConstraintAnalyser {

  public Set<String> getItems(String constraint) {
    Set<String> items = new HashSet<String>();
    StringBuffer buffer = new StringBuffer();
    boolean item = false;
    for (int i = 0; i < constraint.length(); ++i) {
      char c = constraint.charAt(i);
      if (c == '(') {
        item = false;
        if (buffer.length() != 0) {
          String s = buffer.toString();
          items.add(s);
          buffer = new StringBuffer();
        }
      }
      else if (c == ')') {
        item = false;
        if (buffer.length() != 0) {
          String s = buffer.toString();
          items.add(s);
          buffer = new StringBuffer();
        }
      }
      else if (c == ' ') {
        item = false;
        if (buffer.length() != 0) {
          String s = buffer.toString();
          items.add(s);
          buffer = new StringBuffer();
        }
      }
      else if (c == 'n') {
        if (i + 3 < constraint.length() && item == false) {
          if (constraint.charAt(i + 1) == 'o' && constraint.charAt(i + 2) == 't' && (constraint.charAt(i + 3) == '(' || constraint.charAt(i + 3) == ' ')) {
            item = false;
            if (buffer.length() != 0) {
              String s = buffer.toString();
              items.add(s);
              buffer = new StringBuffer();
            }
            i = i + 3;
          }
          else {
            buffer.append(c);
            item = true;
          }
        }
        else {
          buffer.append(c);
          item = true;
        }
      }
      else if (c == 'i') {
        if (i + 7 < constraint.length() && item == false) {
          if (constraint.charAt(i + 1) == 'm' && constraint.charAt(i + 2) == 'p' && constraint.charAt(i + 3) == 'l' && constraint.charAt(i + 4) == 'i' && constraint
              .charAt(i + 5) == 'e' && constraint.charAt(i + 6) == 's' && (constraint.charAt(i + 7) == '(' || constraint.charAt(i + 7) == ' ')) {
            item = false;
            if (buffer.length() != 0) {
              String s = buffer.toString();
              items.add(s);
              buffer = new StringBuffer();
            }
            i = i + 7;
          }
          else {
            buffer.append(c);
            item = true;
          }
        }
        else {
          buffer.append(c);
          item = true;
        }
      }
      else if (c == 'a') {
        if (i + 3 < constraint.length() && item == false) {
          if (constraint.charAt(i + 1) == 'n' && constraint.charAt(i + 2) == 'd' && (constraint.charAt(i + 3) == '(' || constraint.charAt(i + 3) == ' ')) {
            item = false;
            if (buffer.length() != 0) {
              String s = buffer.toString();
              items.add(s);
              buffer = new StringBuffer();
            }
            i = i + 3;
          }
          else {
            buffer.append(c);
            item = true;
          }
        }
        else {
          buffer.append(c);
          item = true;
        }
      }
      else if (c == 'o') {
        if (i + 2 < constraint.length() && item == false) {
          if (constraint.charAt(i + 1) == 'r' && (constraint.charAt(i + 2) == '(' || constraint.charAt(i + 2) == ' ')) {
            item = false;
            if (buffer.length() != 0) {
              String s = buffer.toString();
              items.add(s);
              buffer = new StringBuffer();
            }
            i = i + 2;
          }
          else {
            buffer.append(c);
            item = true;
          }
        }
        else {
          buffer.append(c);
          item = true;
        }
      }
      else {
        buffer.append(c);
        item = true;
      }

    }
    item = false;
    if (buffer.length() != 0) {
      String s = buffer.toString();
      items.add(s);
      buffer = new StringBuffer();
    }
    return items;
  }

}
