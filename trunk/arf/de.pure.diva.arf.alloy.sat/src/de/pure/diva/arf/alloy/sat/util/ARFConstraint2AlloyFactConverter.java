package de.pure.diva.arf.alloy.sat.util;

public class ARFConstraint2AlloyFactConverter {

  public String convert(String constraint) {
    return getFact(constraint);
  }

  private String getFact(String constraint) {
    StringBuffer buffer = new StringBuffer();
    boolean item = false;
    for (int i = 0; i < constraint.length(); ++i) {
      char c = constraint.charAt(i);
      if (c == '(') {
        item = false;
        buffer.append(c);
      }
      else if (c == ')') {
        item = false;
        buffer.append(c);
      }
      else if (c == ' ') {
        item = false;
        buffer.append(c);
      }
      else if (c == 'n') {
        if (i + 3 < constraint.length() && item == false) {
          if (constraint.charAt(i + 1) == 'o' && constraint.charAt(i + 2) == 't' && constraint.charAt(i + 3) == '(') {
            buffer.append("not(");
            item = false;
            i = i + 3;
          }
          else {
            if (item == false) {
              buffer.append("one ");
              buffer.append(c);
              item = true;
            }
            else if (item == true) {
              buffer.append(c);
            }
          }
        }
        else {
          if (item == false) {
            buffer.append("one ");
            buffer.append(c);
            item = true;
          }
          else if (item == true) {
            buffer.append(c);
          }
        }
      }
      else if (c == 'i' || c == 'I') {
        if (i + 7 < constraint.length() && item == false) {
          if ((constraint.charAt(i + 1) == 'm' || constraint.charAt(i + 1) == 'M') && (constraint.charAt(i + 2) == 'p' || constraint.charAt(i + 2) == 'P') && (constraint
              .charAt(i + 3) == 'l' || constraint.charAt(i + 3) == 'L') && (constraint.charAt(i + 4) == 'i' || constraint.charAt(i + 4) == 'I') && (constraint
              .charAt(i + 5) == 'e' || constraint.charAt(i + 5) == 'E') && (constraint.charAt(i + 6) == 's' || constraint.charAt(i + 6) == 'S') && constraint
              .charAt(i + 7) == ' ') {
            buffer.append("implies ");
            item = false;
            i = i + 7;
          }
          else {
            if (item == false) {
              buffer.append("one ");
              buffer.append(c);
              item = true;
            }
            else if (item == true) {
              buffer.append(c);
            }
          }
        }
        else {
          if (item == false) {
            buffer.append("one ");
            buffer.append(c);
            item = true;
          }
          else if (item == true) {
            buffer.append(c);
          }
        }
      }
      else if (c == 'a') {
        if (i + 3 < constraint.length() && item == false) {
          if (constraint.charAt(i + 1) == 'n' && constraint.charAt(i + 2) == 'd' && constraint.charAt(i + 3) == ' ') {
            buffer.append("and ");
            item = false;
            i = i + 3;
          }
          else {
            if (item == false) {
              buffer.append("one ");
              buffer.append(c);
              item = true;
            }
            else if (item == true) {
              buffer.append(c);
            }
          }
        }
        else {
          if (item == false) {
            buffer.append("one ");
            buffer.append(c);
            item = true;
          }
          else if (item == true) {
            buffer.append(c);
          }
        }
      }
      else if (c == 'o') {
        if (i + 2 < constraint.length() && item == false) {
          if (constraint.charAt(i + 1) == 'r' && constraint.charAt(i + 2) == ' ') {
            buffer.append("or ");
            item = false;
            i = i + 2;
          }
          else {
            if (item == false) {
              buffer.append("one ");
              buffer.append(c);
              item = true;
            }
            else if (item == true) {
              buffer.append(c);
            }
          }
        }
        else {
          if (item == false) {
            buffer.append("one ");
            buffer.append(c);
            item = true;
          }
          else if (item == true) {
            buffer.append(c);
          }
        }
      }
      else if (item == false) {
        buffer.append("one ");
        buffer.append(c);
        item = true;
      }
      else if (item == true) {
        buffer.append(c);
      }

    }
    return buffer.toString();
  }

}
