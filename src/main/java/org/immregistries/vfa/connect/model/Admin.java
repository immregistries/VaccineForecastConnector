package org.immregistries.vfa.connect.model;

import java.io.Serializable;

public enum Admin implements Serializable {
  DUE("D", "Due"), OVERDUE("O", "Overdue"), DUE_LATER("L", "Due Later"), CONTRAINDICATED("X", "Contraindicated"), COMPLETE(
      "C", "Complete"), COMPLETE_FOR_SEASON("S", "Complete For Season"), NOT_COMPLETE("N", "Not Complete"), FINISHED(
      "F", "Finished"), IMMUNE("I", "Immune"), UNKNOWN("U", "Unknown"), ERROR("E", "Error"), NO_RESULTS("R",
      "No Results"), ASSUMED_COMPLETE_OR_IMMUNE("A", "Assumed Complete or Immune"), AGED_OUT("G", "Aged Out"), NOT_RECOMMENDED("Y", "Not Recommended");

  private Admin(String adminStatus, String label) {
    this.adminStatus = adminStatus;
    this.label = label;
  }

  private String adminStatus = "";
  private String label = "";

  public String getAdminStatus() {
    return adminStatus;
  }

  public String getLabel() {
    return label;
  }

  public static Admin getAdmin(String adminStatus) {
    for (Admin admin : Admin.values()) {
      if (admin.getAdminStatus().equalsIgnoreCase(adminStatus)) {
        return admin;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return label;
  }
}
