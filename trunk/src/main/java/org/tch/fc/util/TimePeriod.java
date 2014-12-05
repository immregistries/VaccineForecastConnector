package org.tch.fc.util;

import java.util.Calendar;
import java.util.Date;

public class TimePeriod
{
  private TimePeriod nextTimePeriod = null;
  private int value = 0;
  private boolean isMonth = false;

  private static final String PLUS_AND = "and";
  private static final String PLUS_PLUS = "plus";
  private static final String PLUS_SIGN = "+";

  private static final String MINUS_AND = "less";
  private static final String MINUS_MINUS = "minus";
  private static final String MINUS_SIGN = "-";

  private static final String DAYS = "days";
  private static final String DAY = "day";
  private static final String D = "d";

  private static final String WEEKS = "weeks";
  private static final String WEEK = "week";
  private static final String W = "w";

  private static final String MONTHS = "months";
  private static final String MONTH = "month";
  private static final String M = "m";

  private static final String YEAR = "year";
  private static final String YEARS = "years";
  private static final String Y = "y";

  public TimePeriod getNextTimePeriod() {
    return nextTimePeriod;
  }
  
  public boolean isNegative()
  {
    return value < 0;
  }
  
  public boolean isZero()
  {
    return value == 0;
  }

  public void negate() {
    value = -value;
    if (nextTimePeriod != null) {
      nextTimePeriod.negate();
    }
  }

  public Date getDateFrom(Date startingDate) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startingDate);
    if (isMonth) {
      calendar.add(Calendar.MONTH, value);
    } else {
      calendar.add(Calendar.DAY_OF_MONTH, value);
    }
    if (nextTimePeriod != null) {
      return nextTimePeriod.getDateFrom(calendar.getTime());
    }
    return calendar.getTime();
  }

  public Date getDateBefore(Date startingDate) {
    if (nextTimePeriod != null) {
      startingDate = nextTimePeriod.getDateBefore(startingDate);
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startingDate);
    if (isMonth) {
      calendar.add(Calendar.MONTH, -value);
    } else {
      calendar.add(Calendar.DAY_OF_MONTH, -value);
    }
    return calendar.getTime();
  }

  public TimePeriod(String timePeriodString) {
    this(timePeriodString.trim().toLowerCase().split("\\s+"), 0, true);
  }

  private TimePeriod(String[] parts, int i, boolean positive) {
    int multiple = 1;
    boolean doneValue = false;
    boolean doneType = false;
    while (i < parts.length) {
      String part = parts[i];
      if (part.length() > 0) {
        if (part.equals(PLUS_AND) || part.equals(PLUS_PLUS) || part.equals(PLUS_SIGN)) {
          nextTimePeriod = new TimePeriod(parts, i + 1, true);
          i = parts.length;
        } else if (part.equals(MINUS_AND) || part.equals(MINUS_MINUS) || part.equals(MINUS_SIGN)) {
          nextTimePeriod = new TimePeriod(parts, i + 1, false);
          i = parts.length;
        } else if (doneType && doneValue) {
          nextTimePeriod = new TimePeriod(parts, i, true);
          i = parts.length;
        } else if (part.equals(DAYS) || part.equals(DAY) || part.equals(D)) {
          doneType = true;
          isMonth = false;
          multiple = 1;
          value = multiple * value;
        } else if (part.equals(WEEKS) || part.equals(WEEK) || part.equals(W)) {
          doneType = true;
          isMonth = false;
          multiple = 7;
          value = multiple * value;
        } else if (part.equals(MONTHS) || part.equals(MONTH) || part.equals(M)) {
          doneType = true;
          isMonth = true;
          multiple = 1;
          value = multiple * value;
        } else if (part.equals(YEARS) || part.equals(YEAR) || part.equals(Y)) {
          doneType = true;
          isMonth = true;
          multiple = 12;
          value = multiple * value;
        } else {
          char firstChar = part.charAt(0);
          if (firstChar == '-' || firstChar == '+' || (firstChar >= '0' && firstChar <= '9')) {
            try {
              if (positive) {
                value = Integer.parseInt(part) * multiple;
              } else {
                value = Integer.parseInt(part) * -multiple;
              }
              doneValue = true;
            } catch (NumberFormatException nfe) {
              value = 0;
            }
          }
        }
      }
      i++;
    }
  }

  @Override
  public String toString() {
    return printString(true);
  }
  
  public String toStringNoSign()
  {
    if (isNegative())
    {
      return printString(false) + " before";
    }
    else if (isZero())
    {
      return "on";
    }
    return printString(false) + " after";
  }

  private String printString(boolean showSign) {
    String s;
    String sign = "";
    int v = value;
    if (v < 0) {
      v = -value;
      if (showSign) {
        sign = "-";
      }
    }
    if (isMonth) {
      int monthsLeftOver = v % 12;
      if (v > 0 && monthsLeftOver == 0) {
        int years = v / 12;
        s = sign + years + (years == 1 ? " year" : " years");
      } else {
        s = sign + v + (v == 1 ? " month" : " months");
      }
    } else {
      int daysLeftOver = v % 7;
      if (v > 0 && daysLeftOver == 0) {
        int weeks = v / 7;
        s = sign + weeks + (weeks == 1 ? " week" : " weeks");
      } else {
        s = sign + v + (v == 1 ? " day" : " days");
      }
    }
    if (nextTimePeriod != null) {
      if (nextTimePeriod.value > 0) {
        s = s + " plus " + nextTimePeriod.printString(false);
      } else {
        s = s + " minus " + nextTimePeriod.printString(false);
      }
    }
    return s;
  }
}
