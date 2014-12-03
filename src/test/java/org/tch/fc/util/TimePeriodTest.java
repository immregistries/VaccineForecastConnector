package org.tch.fc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

public class TimePeriodTest extends TestCase
{
  public void testTimePeriod() throws ParseException
  {
    assertEquals("4 months", new TimePeriod("4 months").toString());
    assertEquals("4 months", new TimePeriod("months 4").toString());
    assertEquals("4 months", new TimePeriod("4 month").toString());
    assertEquals("4 months", new TimePeriod("4 m").toString());
    assertEquals("1 year", new TimePeriod("1 year").toString());
    assertEquals("1 year", new TimePeriod("12 m").toString());
    assertEquals("1 day", new TimePeriod("1 d").toString());
    assertEquals("4 months plus 5 days", new TimePeriod("4 months plus 5 days").toString());
    assertEquals("4 months plus 5 days", new TimePeriod("4 months + 5 days").toString());
    assertEquals("4 months plus 5 days", new TimePeriod("4 months 5 days").toString());
    assertEquals("4 months plus 5 days", new TimePeriod("4 months and 5 days").toString());
    assertEquals("3 years plus 2 months", new TimePeriod("3 years 2 months").toString());
    assertEquals("4 months minus 5 days", new TimePeriod("4 months minus 5 days").toString());
    assertEquals("4 months minus 5 days", new TimePeriod("4 months - 5 days").toString());
    assertEquals("4 months minus 5 days", new TimePeriod("4 months add -5 days").toString());
    assertEquals("4 months minus 5 days", new TimePeriod("4 months -5 days").toString());
    assertEquals("4 months minus 5 days", new TimePeriod("4 months less 5 days").toString());
    assertEquals("4 months minus 5 days plus 10 days", new TimePeriod("4 months less 5 days plus 10 days").toString());
    assertEquals("4 months minus 5 days plus 10 days", new TimePeriod("4 months less 5 days 10 days").toString());
    assertEquals("22 days", new TimePeriod("22").toString());
    assertEquals("0 days", new TimePeriod("hello").toString());
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    Date evalDate = sdf.parse("12/01/2014");
    assertEquals("09/01/2014", sdf.format(new TimePeriod("3 months").getDateBefore(evalDate)));
    assertEquals("08/29/2014", sdf.format(new TimePeriod("3 months 2 days").getDateBefore(evalDate)));
    assertEquals("09/03/2014", sdf.format(new TimePeriod("3 months -2 days").getDateBefore(evalDate)));
    assertEquals("03/01/2015", sdf.format(new TimePeriod("3 months").getDateFrom(evalDate)));
    assertEquals("03/03/2015", sdf.format(new TimePeriod("3 months 2 days").getDateFrom(evalDate)));
    assertEquals("02/27/2015", sdf.format(new TimePeriod("3 months -2 days").getDateFrom(evalDate)));
  }
}
