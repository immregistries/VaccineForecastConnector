package org.tch.fc.model;

import java.io.Serializable;

public enum Admin implements Serializable {
  DUE("D", "Due"), OVERDUE("O", "Overdue"), DUE_LATER("L", "Due Later"), CONTRAINDICATED("X", "Contraindicated"), COMPLETE(
      "C", "Complete"), NOT_COMPLETE("F", "Not Complete"), FINISHED("N", "Finished"), IMMUNE("I", "Immune");

  private Admin(String adminStatus, String label) {
    this.adminStatus = adminStatus;
    this.label = label;
  }

  private String adminStatus = "";
  private String label = "";

  public String getAdminStatus() {
    return adminStatus;
  }

  public void setAdminStatus(String adminStatus) {
    this.adminStatus = adminStatus;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public static Admin getAdmin(String adminStatus) {
    for (Admin admin : Admin.values()) {
      if (admin.getAdminStatus().equalsIgnoreCase(adminStatus)) {
        return admin;
      }
    }
    return null;
  }
}
