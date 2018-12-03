package org.tch.fc;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import org.tch.fc.model.Evaluation;
import org.tch.fc.model.EvaluationActual;
import org.tch.fc.model.Event;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.Service;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.SoftwareResultStatus;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;
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

    MDSConnector(Software software, List<VaccineGroup> forecastItemList) {
        this.software = software;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private MDSConnector() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult) throws Exception {
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
            softwareResult.setLogText(writer.toString());
            readEvalution(careplan, eventsMap);
            List<ForecastActual> forecast = readForecast(careplan);
            return forecast;
            
        } catch(Exception exp) {
        	exp.printStackTrace();
        	this.setLogText(true);
        	softwareResult.setSoftwareResultStatus(SoftwareResultStatus.PROBLEM);
            softwareResult.setLogText(writer.toString());
            return null;
        }
    }

    private List<ForecastActual> readForecast(Careplan careplan) {
        List<ForecastActual> actual = new ArrayList<ForecastActual>();

        for (Antigen antigen : careplan.getAntigen()) {
            for (Series series : antigen.getSeries()) {
                ForecastActual f = new ForecastActual();
                f.setDoseNumber(series.getDosenum().toString());
                f.setDueDate(xmlToDate(series.getRecommendeddate()));
                f.setOverdueDate(xmlToDate(series.getPastduedate()));
                f.setValidDate(xmlToDate(series.getMindate()));
                f.setVaccineCvx(findCvx(antigen.getName(), series.getAllowableCvx()));
                f.setFinishedDate(xmlToDate(series.getLatedate()));
                actual.add(f);
            }
        }

        return actual;
    }

    public static void main(String[] args) throws Exception {
        
        MDSConnector connector = new MDSConnector();
        TestCase tc = new TestCase();
        tc.setPatientSex("M");
        tc.setPatientDob(new Date());
        tc.setEvalDate(new Date());

        TestEvent te1 = new TestEvent();
        te1.setEvent(Event.getEvent(107));
        te1.setEventDate(sdf.parse("03/29/2018"));
        
        TestEvent te2 = new TestEvent();
        te2.setEvent(Event.getEvent(107));
        te2.setEventDate(sdf.parse("04/24/2018"));

        tc.setTestEventList(Arrays.asList(te1, te2));

        SoftwareResult sftr = new SoftwareResult();
        Software sft = new Software();
        sft.setService(Service.MDS);
        sft.setServiceUrl("http://testws.swpartners.com/mdsservice/mds");
        sftr.setSoftware(sft);

        List<ForecastActual> fa = connector.queryForForecast(tc, sftr);
//        System.out.println(fa);
//        System.out.println(sftr.getLogText());
//        ObjectMapper mapper = new ObjectMapper();
//        System.out.println(mapper.writeValueAsString(fa));
////        System.out.println(mapper.writeValueAsString(tc.getTestEventList()));

    }

    private List<String> allowable(String allowable) {
        return Arrays.asList(allowable.split(","));
    }

    private void readEvalution(Careplan careplan, Map<Integer, Map<Long, TestEvent>> eventsMap) {
        for (Antigen antigen : careplan.getAntigen()) {
            for (Series series : antigen.getSeries()) {
                for (SeriesDose dose : series.getSeriesDose()) {       	
                    if (eventsMap.containsKey(Integer.parseInt(dose.getCvx())) && eventsMap.get(Integer.parseInt(dose.getCvx())).containsKey(xmlToDate(dose.getAdmindate()).getTime())) {
                        TestEvent event = eventsMap.get(Integer.parseInt(dose.getCvx())).get(xmlToDate(dose.getAdmindate()).getTime());
                        EvaluationActual evaluationActual = new EvaluationActual();
	                    evaluationActual.setTestEvent(event);

                        Evaluation ev = Evaluation.getEvaluationByLabel(dose.getStatus());
                        if (ev == null && "Invalid".equalsIgnoreCase(dose.getStatus())) {
                            evaluationActual.setEvaluation(Evaluation.NOT_VALID);
                        } else {
                            evaluationActual.setEvaluation(ev);
                        }

                        if (event.getEvaluationActualList() == null) {
                            event.setEvaluationActualList(new ArrayList<EvaluationActual>());
                        }

                        evaluationActual.setVaccineCvx(findCvx(antigen.getName(), series.getAllowableCvx()));
                        event.getEvaluationActualList().add(evaluationActual);
                    }
                }
            }
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
