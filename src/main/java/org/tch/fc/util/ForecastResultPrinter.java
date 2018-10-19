package org.tch.fc.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.tch.fc.model.EvaluationActual;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;

public class ForecastResultPrinter
{
  public static void printOutResultInFixedWidth(List<ForecastActual> forecastActualList, TestCase testCase, PrintStream out) {
    PrintWriter printWriter = new PrintWriter(out);
    printOutResultInFixedWidth(forecastActualList, testCase, printWriter);
    printWriter.close();
  }
  public static void printOutResultInFixedWidth(List<ForecastActual> forecastActualList, TestCase testCase, PrintWriter out) {
    out.println();
    out.println("VACCINE EVALUATION");
    out.print("| " + pad("CVX", 6));
    out.print("| " + pad("DATE", 12));
    out.print("| " + pad("#", 3));
    out.print("| " + pad("CVX E", 6));
    out.print("| " + pad("VALID", 6));
    out.print("| " + pad("REASON", 30));
    out.println("|");
    out.print("|" + pad("--------------------------------------", 7));
    out.print("|" + pad("--------------------------------------", 13));
    out.print("|" + pad("--------------------------------------", 4));
    out.print("|" + pad("--------------------------------------", 7));
    out.print("|" + pad("--------------------------------------", 7));
    out.print("|" + pad("--------------------------------------", 31));
    out.println("|");
    for (TestEvent testEvent : testCase.getTestEventList()) {
      if (testEvent.getEvaluationActualList() != null && testEvent.getEvaluationActualList().size() > 0) {
        for (EvaluationActual evaluationActual : testEvent.getEvaluationActualList()) {
          out.print("| " + pad(testEvent.getEvent().getVaccineCvx(), 6));
          out.print("| " + pad(testEvent.getEventDate(), 12));
          out.print("| " + pad(evaluationActual.getDoseNumber(), 3));
          out.print("| " + pad(evaluationActual.getVaccineCvx(), 6));
          out.print("| " + pad(evaluationActual.getDoseValid(), 6));
          out.print("| " + pad(evaluationActual.getReasonText(), 30));
          out.println("|");
        }
      } else {
        out.print("| " + pad(testEvent.getEvent().getVaccineCvx(), 6));
        out.print("| " + pad(testEvent.getEventDate(), 12));
        out.print("| " + pad("", 3));
        out.print("| " + pad("", 6));
        out.print("| " + pad("", 6));
        out.print("| " + pad("", 30));
        out.println("|");
      }
    }

    out.println();
    out.println("VACCINE FORECAST");
    out.print("| " + pad("VACCINE", 21));
    out.print("| " + pad("#", 3));
    out.print("| " + pad("STATUS", 15));
    out.print("| " + pad("EARLIEST", 11));
    out.print("| " + pad("RECOMMEND", 11));
    out.print("| " + pad("PAST DUE", 11));
    out.print("| " + pad("LATEST", 11));
    out.println("|");
    out.print("|" + pad("--------------------------", 22));
    out.print("|" + pad("--------------------------", 4));
    out.print("|" + pad("--------------------------", 16));
    out.print("|" + pad("--------------------------", 12));
    out.print("|" + pad("--------------------------", 12));
    out.print("|" + pad("--------------------------", 12));
    out.print("|" + pad("--------------------------", 12));
    out.println("|");

    for (ForecastActual forecastActual : forecastActualList) {
      out.print("| " + pad(forecastActual.getVaccineGroup(), 21));
      out.print("| " + pad(forecastActual.getDoseNumber(), 3));
      out.print("| " + pad(forecastActual.getAdmin(), 15));
      out.print("| " + pad(forecastActual.getValidDate(), 11));
      out.print("| " + pad(forecastActual.getDueDate(), 11));
      out.print("| " + pad(forecastActual.getOverdueDate(), 11));
      out.print("| " + pad(forecastActual.getFinishedDate(), 11));
      out.println("|");
    }
  }
  
  private static String pad(Object o, int len) {
    if (o == null) {
      return pad("", len);
    }
    return pad(o.toString(), len);
  }

  private static String pad(Date date, int len) {
    if (date == null) {
      return pad("", len);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    return pad(sdf.format(date), len);
  }

  private static String pad(String s, int len) {
    if (s == null) {
      s = "";
    }
    for (int i = s.length(); i < len; i++) {
      s += " ";
    }
    if (s.length() > len) {
      s = s.substring(0, len);
    }
    return s;
  }


}
