package org.immregistries.vfa.connect;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import org.immregistries.vfa.connect.model.Evaluation;
import org.immregistries.vfa.connect.model.EvaluationActual;
import org.immregistries.vfa.connect.model.ForecastActual;
import org.immregistries.vfa.connect.model.Software;
import org.immregistries.vfa.connect.model.SoftwareResult;
import org.immregistries.vfa.connect.model.SoftwareResultStatus;
import org.immregistries.vfa.connect.model.TestCase;
import org.immregistries.vfa.connect.model.TestEvent;
import org.immregistries.vfa.connect.model.VaccineGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.healthcare.iz.cds.swp.mds.client.domain.Dose;
import gov.nist.healthcare.iz.cds.swp.mds.client.domain.Patient;
import gov.nist.healthcare.iz.cds.swp.mds.client.service.impl.MDSClientImpl;
import swp.mds.wsdl.domain.Antigen;
import swp.mds.wsdl.domain.Careplan;
import swp.mds.wsdl.domain.Series;
import swp.mds.wsdl.domain.SeriesDose;

public class MDSConnector implements ConnectorInterface {

    private Software software = null;
    static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    boolean logText = false;
    private ObjectMapper mapper = new ObjectMapper();

    public MDSConnector(Software software, List<VaccineGroup> forecastItemList) {
        this.software = software;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private MDSConnector() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult) throws Exception {
    	softwareResult.setSoftware(this.software);
        String gender = testCase.getPatientSex();
        Date dob = testCase.getPatientDob();
        Date evalDate = testCase.getEvalDate();
        Map<Integer, Map<Long, TestEvent>> eventsMap = new HashMap<Integer, Map<Long, TestEvent>>();
        Patient patient = new Patient(gender, dob);
        List<Dose> doses = new ArrayList<Dose>();
        for (TestEvent event : testCase.getTestEventList()) {
            String cvx = event.getEvent().getVaccineCvx();
            Date date = event.getEventDate();
            doses.add(new Dose(cvx, date));
            if (eventsMap.containsKey(Integer.parseInt(cvx))) {
                eventsMap.get(Integer.parseInt(cvx)).put(date.getTime(), event);
            } else {
                Map<Long, TestEvent> map = new HashMap<Long, TestEvent>();
                map.put(date.getTime(), event);
                eventsMap.put(Integer.parseInt(cvx), map);
            }
        }
        
        MDSClientImpl client = new MDSClientImpl();
        StringWriter writer = new StringWriter();
        
        try {
        	
            Careplan careplan = client.getForecast(this.software.getServiceUrl(), patient, evalDate, doses, writer);
            this.setLogText(true);
            writer.write("\n[Reading Evaluations]\n");
            readEvalution(careplan, eventsMap, softwareResult);
            writer.write("\n[Reading Forecasts]\n");
            List<ForecastActual> forecast = readForecast(careplan, writer);
            
            softwareResult.setLogText(writer.toString());
            return forecast;
            
        } catch(Exception exp) {
        	writer.write("\nProblem Encountred [StackTrace] : \n");
        	writer.write(exp.getMessage());
        	exp.printStackTrace();
        	this.setLogText(true);
        	softwareResult.setSoftwareResultStatus(SoftwareResultStatus.PROBLEM);
            softwareResult.setLogText(writer.toString());
            return null;
        }
    }

    private List<ForecastActual> readForecast(Careplan careplan, StringWriter log) throws JsonProcessingException {
        List<ForecastActual> actual = new ArrayList<ForecastActual>();

        for (Antigen antigen : careplan.getAntigen()) {
            for (Series series : antigen.getSeries()) {
                ForecastActual f = new ForecastActual();
                f.setDoseNumber(series.getDosenum().toString());
                f.setDueDate(xmlToDate(series.getRecommendeddate()));
                f.setOverdueDate(xmlToDate(series.getPastduedate()));
                f.setValidDate(xmlToDate(series.getMindate()));
                if(series.getRecommendedCvx() != null || series.getAllowableCvx() != null) {
                	f.setVaccineCvx(findCvx(antigen.getName(), series.getRecommendedCvx() != null ? series.getRecommendedCvx() : series.getAllowableCvx()));
                } else {
                    log.write("\nFound Forecast for - antigen : "+antigen.getName()+" - series : "+series.getName()+" without CVX");
                	continue;
                }
                f.setFinishedDate(xmlToDate(series.getLatedate()));
                log.write("\nFound Forecast for - antigen : "+antigen.getName()+" - series : "+series.getName()+" - CVX : "+f.getVaccineCvx());
                actual.add(f);
            }
        }

        return actual;
    }

    private List<String> allowable(String allowable) {
        return Arrays.asList(allowable.split(","));
    }

    private void readEvalution(Careplan careplan, Map<Integer, Map<Long, TestEvent>> eventsMap, SoftwareResult softwareResult) throws JsonProcessingException {
        for (Antigen antigen : careplan.getAntigen()) {
            for (Series series : antigen.getSeries()) {
                for (SeriesDose dose : series.getSeriesDose()) {   
                	
                    if (dose.getCvx() != null && !dose.getStatus().equals("Skipped") && eventsMap.containsKey(Integer.parseInt(dose.getCvx())) && eventsMap.get(Integer.parseInt(dose.getCvx())).containsKey(xmlToDate(dose.getAdmindate()).getTime())) {
                        TestEvent event = eventsMap.get(Integer.parseInt(dose.getCvx())).get(xmlToDate(dose.getAdmindate()).getTime());
                        EvaluationActual evaluationActual = new EvaluationActual();
                        evaluationActual.setSoftwareResult(softwareResult);
                        evaluationActual.setTestEvent(event);
                        evaluationActual.setDoseNumber(dose.getDosenum() != null ? dose.getDosenum()+"" : null);
                        evaluationActual.setEvaluation(evaluationFromString(dose.getStatus()));
                        evaluationActual.setDoseValid(evaluationActual.getEvaluation() == Evaluation.VALID ? "Y" : "N");
                        evaluationActual.setVaccineCvx(findCvx(antigen.getName(), dose.getCvx()));
                        if(series.getRecommendedCvx() != null || series.getAllowableCvx() != null) {
                        	evaluationActual.setVaccineCvx(findCvx(antigen.getName(), series.getRecommendedCvx() != null ? series.getRecommendedCvx() : series.getAllowableCvx()));
                        } else {
                        	evaluationActual.setVaccineCvx(findCvx(antigen.getName(), dose.getCvx()));
                        }
                        evaluationActual.setSeriesUsedCode(evaluationActual.getVaccineCvx());

                        if(event.getEvaluationActualList() == null) {
                        	event.setEvaluationActualList(new ArrayList<EvaluationActual>());
                        }
                        event.getEvaluationActualList().add(evaluationActual);
                    }
                }
            }
        }
    }
    
    private Evaluation evaluationFromString(String evaluation) {
    	Evaluation ev = Evaluation.getEvaluationByLabel(evaluation);
    	if(ev == null || "Invalid".equalsIgnoreCase(evaluation)) {
    		return Evaluation.NOT_VALID;
    	} else {
    		return ev;
    	}
    }

    private String findCvx(String antigen, String allowableStr) {
        List<String> allowable = allowable(allowableStr);
        List<VaccineGroup> groups = VaccineGroup.getForecastItemList();
        List<VaccineGroup> candidates = new ArrayList<VaccineGroup>(); 
        
        for (VaccineGroup group : groups) {
        	
        	boolean isAllowable = this.groupIsAllowable(allowable, group);
        	
            if (group.getLabel().equalsIgnoreCase(antigen) && isAllowable) {
                return group.getVaccineCvx();
            }
            if (isAllowable) {
                candidates.add(group);
            }
            
        }
        if (candidates.size() > 0) {
            return candidates.get(0).getVaccineCvx();
        }
        return allowable.get(0);
    }
    
    private boolean groupIsAllowable(List<String> allowable,  VaccineGroup group) {
    	for(String cvx: allowable) {
    		if(Integer.parseInt(cvx) == Integer.parseInt(group.getVaccineCvx())) {
    			return true;
    		}
    	}
    	return false;
    }

    private Date xmlToDate(XMLGregorianCalendar c) {
        if (c == null) {
            return null;
        }
        return c.toGregorianCalendar().getTime();
    }

    public void setLogText(boolean logText) {
    	this.logText = logText;
    }

    public boolean isLogText() {
        return this.logText;
    }
}
