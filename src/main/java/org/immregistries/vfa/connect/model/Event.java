package org.immregistries.vfa.connect.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Event implements Serializable
{

  // All event ids from 0 to 99,000 are reserved for CVX codes
  public static final int EVENT_ID_RANGE_CVX = 0;
  // All event ids from 100,000 to 199,999 are reserved for MIIS specified ids
  public static final int EVENT_ID_RANGE_1_MIIS = 100000;

  public static final int EVENT_ID_RANGE_2_RESERVED = 200000;

  public static final Event BIRTH_EVENT = new Event();
  public static final Event EVALUATION_EVENT = new Event();

  static {
    BIRTH_EVENT.setEventType(EventType.BIRTH);
    BIRTH_EVENT.setLabel("Patient born");
    EVALUATION_EVENT.setEventType(EventType.EVALUATION);
    EVALUATION_EVENT.setLabel("Evaluation should be performed");
  }

  public Event() {
    // default;
  }

  private Event(int eventId, String label, EventType eventType, String vaccineCvx, String vaccineMvx) {
    this.eventId = eventId;
    this.label = label;
    this.eventType = eventType;
    this.vaccineCvx = vaccineCvx;
    this.vaccineMvx = vaccineMvx;
  }

  private static final long serialVersionUID = 1L;

  private int eventId = 0;
  private String label = "";
  private EventType eventType = null;
  private String vaccineCvx = "";
  private String vaccineMvx = "";
  private String tradeLabel = "";

  public String getTradeLabel() {
    return tradeLabel;
  }

  public void setTradeLabel(String tradeLabel) {
    this.tradeLabel = tradeLabel;
  }

  public int getEventId() {
    return eventId;
  }

  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  public String getLabel() {
    return label;
  }

  public String getLabelScreen() {
    if (getEventType() == EventType.BIRTH) {
      return "Birth";
    } else if (getEventType() == EventType.EVALUATION) {
      return "Evaluation";
    } else if (getEventType() == EventType.VACCINATION) {
      return getLabel();
    } else if (getEventType() == EventType.ACIP_DEFINED_CONDITION) {
      return "ACIP-Defined Condition '" + getLabel();
    } else if (getEventType() == EventType.CONDITION_IMPLICATION) {
      return "Condition Implication '" + getLabel();
    } else {
      return getEventType().getLabel() + " '" + getLabel() + "'";
    }
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public EventType getEventType() {
    return eventType;
  }

  public void setEventType(EventType eventType) {
    this.eventType = eventType;
  }

  public void setEventTypeCode(String eventTypeCode) {
    this.eventType = EventType.getEventType(eventTypeCode);
  }

  public String getEventTypeCode() {
    return eventType == null ? null : eventType.getEventTypeCode();
  }

  public String getVaccineCvx() {
    return vaccineCvx;
  }

  public void setVaccineCvx(String vaccineCvx) {
    this.vaccineCvx = vaccineCvx;
  }

  public String getVaccineMvx() {
    return vaccineMvx;
  }

  public void setVaccineMvx(String vaccineMvx) {
    this.vaccineMvx = vaccineMvx;
  }

  @Override
  public String toString() {
    return (eventType == null ? "" : eventType.getLabel()) + ": " + label;
  }

  private static List<Event> eventList = null;
  private static Map<Integer, Event> eventMap = null;

  public static List<Event> getEventList() {
    initEventListAndMap();
    return eventList;
  }

  public static Event getEvent(int eventId) {
    initEventListAndMap();
    return eventMap.get(eventId);
  }

  public static void initEventListAndMap() {
    if (eventList == null) {
      eventList = new ArrayList<Event>();
      eventList.add(new Event(1, "DTP", EventType.VACCINATION, "01", ""));
      eventList.add(new Event(2, "OPV", EventType.VACCINATION, "02", ""));
      eventList.add(new Event(3, "MMR", EventType.VACCINATION, "03", ""));
      eventList.add(new Event(4, "M/R", EventType.VACCINATION, "04", ""));
      eventList.add(new Event(5, "measles", EventType.VACCINATION, "05", ""));
      eventList.add(new Event(6, "rubella", EventType.VACCINATION, "06", ""));
      eventList.add(new Event(7, "mumps", EventType.VACCINATION, "07", ""));
      eventList.add(new Event(8, "Hep B, adolescent or pediatric", EventType.VACCINATION, "08", ""));
      eventList.add(new Event(9, "Td (adult), adsorbed", EventType.VACCINATION, "09", ""));
      eventList.add(new Event(10, "IPV", EventType.VACCINATION, "10", ""));
      eventList.add(new Event(11, "pertussis", EventType.VACCINATION, "11", ""));
      eventList.add(new Event(12, "diphtheria antitoxin", EventType.VACCINATION, "12", ""));
      eventList.add(new Event(13, "TIG", EventType.VACCINATION, "13", ""));
      eventList.add(new Event(14, "IG, unspecified formulation", EventType.VACCINATION, "14", ""));
      eventList
          .add(new Event(15, "influenza, split (incl. purified surface antigen)", EventType.VACCINATION, "15", ""));
      eventList.add(new Event(16, "influenza, whole", EventType.VACCINATION, "16", ""));
      eventList.add(new Event(17, "Hib, unspecified formulation", EventType.VACCINATION, "17", ""));
      eventList.add(new Event(18, "rabies, intramuscular injection", EventType.VACCINATION, "18", ""));
      eventList.add(new Event(19, "BCG", EventType.VACCINATION, "19", ""));
      eventList.add(new Event(20, "DTaP", EventType.VACCINATION, "20", ""));
      eventList.add(new Event(21, "varicella", EventType.VACCINATION, "21", ""));
      eventList.add(new Event(22, "DTP-Hib", EventType.VACCINATION, "22", ""));
      eventList.add(new Event(23, "plague", EventType.VACCINATION, "23", ""));
      eventList.add(new Event(24, "anthrax", EventType.VACCINATION, "24", ""));
      eventList.add(new Event(25, "typhoid, oral", EventType.VACCINATION, "25", ""));
      eventList.add(new Event(26, "cholera", EventType.VACCINATION, "26", ""));
      eventList.add(new Event(27, "botulinum antitoxin", EventType.VACCINATION, "27", ""));
      eventList.add(new Event(28, "DT (pediatric)", EventType.VACCINATION, "28", ""));
      eventList.add(new Event(29, "CMVIG", EventType.VACCINATION, "29", ""));
      eventList.add(new Event(30, "HBIG", EventType.VACCINATION, "30", ""));
      eventList.add(new Event(31, "Hep A, pediatric, unspecified formulation", EventType.VACCINATION, "31", ""));
      eventList.add(new Event(32, "meningococcal MPSV4", EventType.VACCINATION, "32", ""));
      eventList.add(new Event(33, "pneumococcal polysaccharide PPV23", EventType.VACCINATION, "33", ""));
      eventList.add(new Event(34, "RIG", EventType.VACCINATION, "34", ""));
      eventList.add(new Event(35, "tetanus toxoid, adsorbed", EventType.VACCINATION, "35", ""));
      eventList.add(new Event(36, "VZIG", EventType.VACCINATION, "36", ""));
      eventList.add(new Event(37, "yellow fever", EventType.VACCINATION, "37", ""));
      eventList.add(new Event(38, "rubella/mumps", EventType.VACCINATION, "38", ""));
      eventList.add(new Event(39, "Japanese encephalitis SC", EventType.VACCINATION, "39", ""));
      eventList.add(new Event(40, "rabies, intradermal injection", EventType.VACCINATION, "40", ""));
      eventList.add(new Event(41, "typhoid, parenteral", EventType.VACCINATION, "41", ""));
      eventList.add(new Event(42, "Hep B, adolescent/high risk infant", EventType.VACCINATION, "42", ""));
      eventList.add(new Event(43, "Hep B, adult", EventType.VACCINATION, "43", ""));
      eventList.add(new Event(44, "Hep B, dialysis", EventType.VACCINATION, "44", ""));
      eventList.add(new Event(45, "Hep B, unspecified formulation", EventType.VACCINATION, "45", ""));
      eventList.add(new Event(46, "Hib (PRP-D)", EventType.VACCINATION, "46", ""));
      eventList.add(new Event(47, "Hib (HbOC)", EventType.VACCINATION, "47", ""));
      eventList.add(new Event(48, "Hib (PRP-T)", EventType.VACCINATION, "48", ""));
      eventList.add(new Event(49, "Hib (PRP-OMP)", EventType.VACCINATION, "49", ""));
      eventList.add(new Event(50, "DTaP-Hib", EventType.VACCINATION, "50", ""));
      eventList.add(new Event(51, "Hib-Hep B", EventType.VACCINATION, "51", ""));
      eventList.add(new Event(52, "Hep A, adult", EventType.VACCINATION, "52", ""));
      eventList.add(new Event(53, "typhoid, parenteral, AKD (U.S. military)", EventType.VACCINATION, "53", ""));
      eventList.add(new Event(54, "adenovirus, type 4", EventType.VACCINATION, "54", ""));
      eventList.add(new Event(55, "adenovirus, type 7", EventType.VACCINATION, "55", ""));
      eventList.add(new Event(56, "dengue fever", EventType.VACCINATION, "56", ""));
      eventList.add(new Event(57, "hantavirus", EventType.VACCINATION, "57", ""));
      eventList.add(new Event(58, "Hep C", EventType.VACCINATION, "58", ""));
      eventList.add(new Event(59, "Hep E", EventType.VACCINATION, "59", ""));
      eventList.add(new Event(60, "herpes simplex 2", EventType.VACCINATION, "60", ""));
      eventList.add(new Event(61, "HIV", EventType.VACCINATION, "61", ""));
      eventList.add(new Event(62, "HPV, quadrivalent", EventType.VACCINATION, "62", ""));
      eventList.add(new Event(63, "Junin virus", EventType.VACCINATION, "63", ""));
      eventList.add(new Event(64, "leishmaniasis", EventType.VACCINATION, "64", ""));
      eventList.add(new Event(65, "leprosy", EventType.VACCINATION, "65", ""));
      eventList.add(new Event(66, "Lyme disease", EventType.VACCINATION, "66", ""));
      eventList.add(new Event(67, "malaria", EventType.VACCINATION, "67", ""));
      eventList.add(new Event(68, "melanoma", EventType.VACCINATION, "68", ""));
      eventList.add(new Event(69, "parainfluenza-3", EventType.VACCINATION, "69", ""));
      eventList.add(new Event(70, "Q fever", EventType.VACCINATION, "70", ""));
      eventList.add(new Event(71, "RSV-IGIV", EventType.VACCINATION, "71", ""));
      eventList.add(new Event(72, "rheumatic fever", EventType.VACCINATION, "72", ""));
      eventList.add(new Event(73, "Rift Valley fever", EventType.VACCINATION, "73", ""));
      eventList.add(new Event(74, "rotavirus, tetravalent", EventType.VACCINATION, "74", ""));
      eventList.add(new Event(75, "vaccinia (smallpox)", EventType.VACCINATION, "75", ""));
      eventList.add(new Event(76, "Staphylococcus bacterio lysate", EventType.VACCINATION, "76", ""));
      eventList.add(new Event(77, "tick-borne encephalitis", EventType.VACCINATION, "77", ""));
      eventList.add(new Event(78, "tularemia vaccine", EventType.VACCINATION, "78", ""));
      eventList.add(new Event(79, "vaccinia immune globulin", EventType.VACCINATION, "79", ""));
      eventList.add(new Event(80, "VEE, live", EventType.VACCINATION, "80", ""));
      eventList.add(new Event(81, "VEE, inactivated", EventType.VACCINATION, "81", ""));
      eventList.add(new Event(82, "adenovirus, unspecified formulation", EventType.VACCINATION, "82", ""));
      eventList.add(new Event(83, "Hep A, ped/adol, 2 dose", EventType.VACCINATION, "83", ""));
      eventList.add(new Event(84, "Hep A, ped/adol, 3 dose", EventType.VACCINATION, "84", ""));
      eventList.add(new Event(85, "Hep A, unspecified formulation", EventType.VACCINATION, "85", ""));
      eventList.add(new Event(86, "IG", EventType.VACCINATION, "86", ""));
      eventList.add(new Event(87, "IGIV", EventType.VACCINATION, "87", ""));
      eventList.add(new Event(88, "influenza, unspecified formulation", EventType.VACCINATION, "88", ""));
      eventList.add(new Event(89, "polio, unspecified formulation", EventType.VACCINATION, "89", ""));
      eventList.add(new Event(90, "rabies, unspecified formulation", EventType.VACCINATION, "90", ""));
      eventList.add(new Event(91, "typhoid, unspecified formulation", EventType.VACCINATION, "91", ""));
      eventList.add(new Event(92, "VEE, unspecified formulation", EventType.VACCINATION, "92", ""));
      eventList.add(new Event(93, "RSV-MAb", EventType.VACCINATION, "93", ""));
      eventList.add(new Event(94, "MMRV", EventType.VACCINATION, "94", ""));
      eventList.add(new Event(95, "TST-OT tine test", EventType.VACCINATION, "95", ""));
      eventList.add(new Event(96, "TST-PPD intradermal", EventType.VACCINATION, "96", ""));
      eventList.add(new Event(97, "TST-PPD tine test", EventType.VACCINATION, "97", ""));
      eventList.add(new Event(98, "TST, unspecified formulation", EventType.VACCINATION, "98", ""));
      eventList.add(new Event(99, "RESERVED - do not use", EventType.VACCINATION, "99", ""));
      eventList.add(new Event(100, "pneumococcal conjugate PCV 7", EventType.VACCINATION, "100", ""));
      eventList.add(new Event(101, "typhoid, ViCPs", EventType.VACCINATION, "101", ""));
      eventList.add(new Event(102, "DTP-Hib-Hep B", EventType.VACCINATION, "102", ""));
      eventList.add(new Event(103, "meningococcal C conjugate", EventType.VACCINATION, "103", ""));
      eventList.add(new Event(104, "Hep A-Hep B", EventType.VACCINATION, "104", ""));
      eventList.add(new Event(105, "vaccinia (smallpox) diluted", EventType.VACCINATION, "105", ""));
      eventList.add(new Event(106, "DTaP, 5 pertussis antigens", EventType.VACCINATION, "106", ""));
      eventList.add(new Event(107, "DTaP, unspecified formulation", EventType.VACCINATION, "107", ""));
      eventList.add(new Event(108, "meningococcal, unspecified formulation", EventType.VACCINATION, "108", ""));
      eventList.add(new Event(109, "pneumococcal, unspecified formulation", EventType.VACCINATION, "109", ""));
      eventList.add(new Event(110, "DTaP-Hep B-IPV", EventType.VACCINATION, "110", ""));
      eventList.add(new Event(111, "influenza, live, intranasal", EventType.VACCINATION, "111", ""));
      eventList.add(new Event(112, "tetanus toxoid, unspecified formulation", EventType.VACCINATION, "112", ""));
      eventList.add(new Event(113, "Td (adult) preservative free", EventType.VACCINATION, "113", ""));
      eventList.add(new Event(114, "meningococcal MCV4P", EventType.VACCINATION, "114", ""));
      eventList.add(new Event(115, "Tdap", EventType.VACCINATION, "115", ""));
      eventList.add(new Event(116, "rotavirus, pentavalent", EventType.VACCINATION, "116", ""));
      eventList.add(new Event(117, "VZIG (IND)", EventType.VACCINATION, "117", ""));
      eventList.add(new Event(118, "HPV, bivalent", EventType.VACCINATION, "118", ""));
      eventList.add(new Event(119, "rotavirus, monovalent", EventType.VACCINATION, "119", ""));
      eventList.add(new Event(120, "DTaP-Hib-IPV", EventType.VACCINATION, "120", ""));
      eventList.add(new Event(121, "zoster", EventType.VACCINATION, "121", ""));
      eventList.add(new Event(122, "rotavirus, unspecified formulation", EventType.VACCINATION, "122", ""));
      eventList.add(new Event(123, "influenza, H5N1-1203", EventType.VACCINATION, "123", ""));
      eventList.add(new Event(125, "Novel Influenza-H1N1-09, nasal", EventType.VACCINATION, "125", ""));
      eventList.add(new Event(126, "Novel influenza-H1N1-09, preservative-free", EventType.VACCINATION, "126", ""));
      eventList.add(new Event(127, "Novel influenza-H1N1-09", EventType.VACCINATION, "127", ""));
      eventList.add(new Event(128, "Novel Influenza-H1N1-09, all formulations", EventType.VACCINATION, "128", ""));
      eventList.add(new Event(129, "Japanese Encephalitis, unspecified formulation", EventType.VACCINATION, "129", ""));
      eventList.add(new Event(130, "DTaP-IPV", EventType.VACCINATION, "130", ""));
      eventList.add(new Event(131, "typhus, historical", EventType.VACCINATION, "131", ""));
      eventList.add(new Event(132, "DTaP-IPV-HIB-HEP B, historical", EventType.VACCINATION, "132", ""));
      eventList.add(new Event(133, "Pneumococcal conjugate PCV 13", EventType.VACCINATION, "133", ""));
      eventList.add(new Event(134, "Japanese Encephalitis IM", EventType.VACCINATION, "134", ""));
      eventList.add(new Event(135, "Influenza, high dose seasonal", EventType.VACCINATION, "135", ""));
      eventList.add(new Event(136, "Meningococcal MCV4O", EventType.VACCINATION, "136", ""));
      eventList.add(new Event(137, "HPV, unspecified formulation", EventType.VACCINATION, "137", ""));
      eventList.add(new Event(138, "Td (adult)", EventType.VACCINATION, "138", ""));
      eventList.add(new Event(139, "Td(adult) unspecified formulation", EventType.VACCINATION, "139", ""));
      eventList
          .add(new Event(140, "Influenza, seasonal, injectable, preservative free", EventType.VACCINATION, "140", ""));
      eventList.add(new Event(141, "Influenza, seasonal, injectable", EventType.VACCINATION, "141", ""));
      eventList.add(new Event(142, "tetanus toxoid, not adsorbed", EventType.VACCINATION, "142", ""));
      eventList.add(new Event(143, "Adenovirus types 4 and 7", EventType.VACCINATION, "143", ""));
      eventList
          .add(new Event(144, "influenza, seasonal, intradermal, preservative free", EventType.VACCINATION, "144", ""));
      eventList.add(new Event(145, "RSV-MAb (new)", EventType.VACCINATION, "145", ""));
      eventList.add(new Event(146, "DTaP,IPV,Hib,HepB", EventType.VACCINATION, "146", ""));
      eventList.add(new Event(147, "meningococcal MCV4, unspecified formulation", EventType.VACCINATION, "147", ""));
      eventList.add(new Event(148, "Meningococcal C/Y-HIB PRP", EventType.VACCINATION, "148", ""));
      eventList.add(new Event(149, "influenza, live, intranasal, quadrivalent", EventType.VACCINATION, "149", ""));
      eventList.add(new Event(150, "influenza, injectable, quadrivalent, preservative free", EventType.VACCINATION, "150", ""));
      eventList.add(new Event(151, "influenza nasal, unspecified formulation", EventType.VACCINATION, "151", ""));
      eventList.add(new Event(152, "Pneumococcal Conjugate, unspecified formulation", EventType.VACCINATION, "152", ""));
      eventList.add(new Event(153, "Influenza, injectable, MDCK, preservative free", EventType.VACCINATION, "153", ""));
      eventList.add(new Event(154, "Hep A, IG", EventType.VACCINATION, "154", ""));
      eventList.add(new Event(155, "influenza, recombinant, injectable, preservative free", EventType.VACCINATION, "155", ""));
      eventList.add(new Event(156, "Rho(D)-IG", EventType.VACCINATION, "156", ""));
      eventList.add(new Event(157, "Rho(D) -IG IM", EventType.VACCINATION, "157", ""));
      eventList.add(new Event(158, "influenza, injectable, quadrivalent", EventType.VACCINATION, "158", ""));
      eventList.add(new Event(159, "Rho(D) - Unspecified formulation", EventType.VACCINATION, "159", ""));
      eventList.add(new Event(160, "Influenza A monovalent (H5N1), ADJUVANTED-2013", EventType.VACCINATION, "160", ""));
      eventList.add(new Event(161, "Influenza, injectable,quadrivalent, preservative free, pediatric", EventType.VACCINATION, "161", ""));
      eventList.add(new Event(162, "meningococcal B, recombinant", EventType.VACCINATION, "162", ""));
      eventList.add(new Event(163, "meningococcal B, OMV", EventType.VACCINATION, "163", ""));
      eventList.add(new Event(164, "meningococcal B, unspecified", EventType.VACCINATION, "164", ""));
      eventList.add(new Event(165, "HPV9", EventType.VACCINATION, "165", ""));
      eventList.add(new Event(166, "influenza, intradermal, quadrivalent, preservative free", EventType.VACCINATION, "166", ""));
      eventList.add(new Event(167, "meningococcal, unknown serogroups", EventType.VACCINATION, "167", ""));
      eventList.add(new Event(168, "influenza, trivalent, adjuvanted", EventType.VACCINATION, "168", ""));
      eventList.add(new Event(169, "Hep A, live attenuated", EventType.VACCINATION, "169", ""));
      eventList.add(new Event(170, "DTAP/IPV/HIB - non-US", EventType.VACCINATION, "170", ""));
      eventList.add(new Event(171, "Influenza, injectable, MDCK, preservative free, quadrivalent", EventType.VACCINATION, "171", ""));
      eventList.add(new Event(172, "cholera, WC-rBS", EventType.VACCINATION, "172", ""));
      eventList.add(new Event(173, "cholera, BivWC", EventType.VACCINATION, "173", ""));
      eventList.add(new Event(174, "cholera, live attenuated", EventType.VACCINATION, "174", ""));
      eventList.add(new Event(175, "Rabies - IM Diploid cell culture", EventType.VACCINATION, "175", ""));
      eventList.add(new Event(176, "Rabies - IM fibroblast culture", EventType.VACCINATION, "176", ""));
      eventList.add(new Event(177, "PCV10", EventType.VACCINATION, "177", ""));
      eventList.add(new Event(178, "OPV bivalent", EventType.VACCINATION, "178", ""));
      eventList.add(new Event(179, "OPV ,monovalent, unspecified", EventType.VACCINATION, "179", ""));
      eventList.add(new Event(180, "tetanus immune globulin", EventType.VACCINATION, "180", ""));
      eventList.add(new Event(181, "anthrax immune globulin", EventType.VACCINATION, "181", ""));
      eventList.add(new Event(182, "OPV, Unspecified", EventType.VACCINATION, "182", ""));
      eventList.add(new Event(183, "Yellow fever vaccine - alt", EventType.VACCINATION, "183", ""));
      eventList.add(new Event(184, "Yellow fever, unspecified formulation", EventType.VACCINATION, "184", ""));
      eventList.add(new Event(185, "influenza, recombinant, quadrivalent,injectable, preservative free", EventType.VACCINATION, "185", ""));
      eventList.add(new Event(186, "Influenza, injectable, MDCK, quadrivalent, preservative", EventType.VACCINATION, "186", ""));
      eventList.add(new Event(187, "zoster recombinant", EventType.VACCINATION, "187", ""));
      eventList.add(new Event(188, "zoster, unspecified formulation", EventType.VACCINATION, "188", ""));
      eventList.add(new Event(189, "HepB-CpG", EventType.VACCINATION, "189", ""));
      eventList.add(new Event(190, "Typhoid conjugate vaccine (TCV)", EventType.VACCINATION, "190", ""));
      eventList.add(new Event(191, "meningococcal A polysaccharide (non-US)", EventType.VACCINATION, "191", ""));
      eventList.add(new Event(192, "meningococcal AC polysaccharide (non-US)", EventType.VACCINATION, "192", ""));
      eventList.add(new Event(193, "Hep A-Hep B, pediatric/adolescent", EventType.VACCINATION, "193", ""));
      eventList.add(new Event(194, "Influenza, Southern Hemisphere", EventType.VACCINATION, "194", ""));
      eventList.add(new Event(195, "DT, IPV adsorbed", EventType.VACCINATION, "195", ""));
      eventList.add(new Event(196, "Td, adsorbed, preservative free, adult use, Lf unspecified", EventType.VACCINATION, "196", ""));
      eventList.add(new Event(197, "influenza, high-dose, quadrivalent", EventType.VACCINATION, "197", ""));
      eventList.add(new Event(198, "DTP-hepB-Hib Pentavalent Non-US", EventType.VACCINATION, "198", ""));
      eventList.add(new Event(200, "influenza, Southern Hemisphere, pediatric, preservative free", EventType.VACCINATION, "200", ""));
      eventList.add(new Event(201, "influenza, Southern Hemisphere, preservative free", EventType.VACCINATION, "201", ""));
      eventList.add(new Event(202, "influenza, Southern Hemisphere, quadrivalent, with preservative", EventType.VACCINATION, "202", ""));
      eventList.add(new Event(203, "meningococcal polysaccharide (groups A, C, Y, W-135) TT conjugate", EventType.VACCINATION, "203", ""));
      eventList.add(new Event(205, "Influenza vaccine, quadrivalent, adjuvanted", EventType.VACCINATION, "205", ""));
      eventList.add(new Event(206, "Smallpox monkeypox vaccine (National Stockpile)", EventType.VACCINATION, "206", ""));
      eventList.add(new Event(207, "COVID-19, mRNA, LNP-S, PF, 100 mcg/0.5 mL dose", EventType.VACCINATION, "207", ""));
      eventList.add(new Event(208, "COVID-19, mRNA, LNP-S, PF, 30 mcg/0.3 mL dose", EventType.VACCINATION, "208", ""));
      eventList.add(new Event(213, "SARS-COV-2 (COVID-19) vaccine, UNSPECIFIED", EventType.VACCINATION, "213", ""));
      eventList.add(new Event(801, "AS03 Adjuvant", EventType.VACCINATION, "801", ""));
      eventList.add(new Event(998, "no vaccine administered", EventType.VACCINATION, "998", ""));
      eventList.add(new Event(999, "unknown", EventType.VACCINATION, "999", ""));

      eventMap = new HashMap<Integer, Event>();
      for (Event event : eventList) {
        if (event != null) {
          eventMap.put(event.eventId, event);
        }
      }
    }
  }
}
