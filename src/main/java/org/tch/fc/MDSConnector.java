package org.tch.fc;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.tch.fc.model.Evaluation;
import org.tch.fc.model.EvaluationActual;
import org.tch.fc.model.Event;
import org.tch.fc.model.ForecastActual;
import org.tch.fc.model.Service;
import org.tch.fc.model.Software;
import org.tch.fc.model.SoftwareResult;
import org.tch.fc.model.TestCase;
import org.tch.fc.model.TestEvent;
import org.tch.fc.model.VaccineGroup;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.healthcare.iz.cds.swp.mds.client.domain.Dose;
import gov.nist.healthcare.iz.cds.swp.mds.client.domain.Patient;
import gov.nist.healthcare.iz.cds.swp.mds.client.service.impl.MDSClientImpl;
import swp.mds.wsdl.domain.Antigen;
import swp.mds.wsdl.domain.Careplan;
import swp.mds.wsdl.domain.Series;
import swp.mds.wsdl.domain.SeriesDose;

public class MDSConnector implements ConnectorInterface {


	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	public List<ForecastActual> queryForForecast(TestCase testCase, SoftwareResult softwareResult) throws Exception {
		String gender = testCase.getPatientSex();
		Date dob = testCase.getPatientDob();
		Date evalDate = testCase.getEvalDate();
		Map<Integer, Map<Long, TestEvent>> eventsMap = new HashMap<Integer, Map<Long, TestEvent>>();
		Patient patient = new Patient(gender, dob);
		List<Dose> doses = new ArrayList<Dose>();
		for(TestEvent event : testCase.getTestEventList()){
			String cvx = event.getEvent().getVaccineCvx();
			Date date = event.getEventDate();
			doses.add(new Dose(cvx, date));
			
			if(eventsMap.containsKey(cvx)){
				eventsMap.get(cvx).put(date.getTime(), event);
			}
			else {
				Map<Long, TestEvent> map = new HashMap<Long, TestEvent>();
				map.put(date.getTime(), event);
				eventsMap.put(Integer.parseInt(cvx), map);
			}
		}
		
		MDSClientImpl client = new MDSClientImpl();
		Careplan careplan = client.getForecast(softwareResult.getSoftware().getServiceUrl(), patient, evalDate, doses);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(eventsMap));
		System.out.println(mapper.writeValueAsString(careplan));
		readEvalution(careplan, eventsMap);
		List<ForecastActual> forecast = readForecast(careplan);
		return forecast;
	}
	
	private List<ForecastActual> readForecast(Careplan careplan){
		List<ForecastActual> actual = new ArrayList<ForecastActual>();
		
		for(Antigen antigen : careplan.getAntigen()){
			for(Series series : antigen.getSeries()){
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
		
		TestEvent te = new TestEvent();
		te.setEvent(Event.getEvent(110));
//		te.setTestCase(tc);
		Date d = sdf.parse("09/24/2018");

		te.setEventDate(d);
		
		tc.setTestEventList(Arrays.asList(te));
		
		SoftwareResult sftr = new SoftwareResult();
		Software sft = new Software();
		sft.setService(Service.MDS);
		
		sftr.setSoftware(sft);
		
		List<ForecastActual> fa = connector.queryForForecast(tc, sftr);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(fa));
		System.out.println(mapper.writeValueAsString(tc.getTestEventList()));

	}
	
	private List<String> allowable(String allowable){
		return Arrays.asList(allowable.split(","));
	}
	
	private void readEvalution(Careplan careplan, Map<Integer, Map<Long, TestEvent>> eventsMap){
		for(Antigen antigen : careplan.getAntigen()){
			for(Series series : antigen.getSeries()){
				for(SeriesDose dose: series.getSeriesDose()){
					if(eventsMap.containsKey(Integer.parseInt(dose.getCvx())) && eventsMap.get(Integer.parseInt(dose.getCvx())).containsKey(xmlToDate(dose.getAdmindate()).getTime())) {

						TestEvent event = eventsMap.get(Integer.parseInt(dose.getCvx())).get(xmlToDate(dose.getAdmindate()).getTime());
						EvaluationActual evaluationActual = new EvaluationActual();
//	                    evaluationActual.setTestEvent(event);
						
						Evaluation ev = Evaluation.getEvaluationByLabel(dose.getStatus());
						if(ev == null && "Invalid".equalsIgnoreCase(dose.getStatus())){
							evaluationActual.setEvaluation(Evaluation.NOT_VALID);
						}
						else {
							evaluationActual.setEvaluation(ev);
						}
						
	                    if(event.getEvaluationActualList() == null){
	                    	event.setEvaluationActualList(new ArrayList<EvaluationActual>());
	                    }
	                    
	                    evaluationActual.setVaccineCvx(findCvx(antigen.getName(), series.getAllowableCvx()));
	                    event.getEvaluationActualList().add(evaluationActual);
					}
				}
			}
		}
	}
	
	private String findCvx(String antigen, String allowableStr){
		List<String> allowable = allowable(allowableStr);
		List<VaccineGroup> groups = VaccineGroup.getForecastItemList();
		List<VaccineGroup> candidates = new ArrayList<VaccineGroup>();
		for(VaccineGroup group : groups){
			if(group.getLabel().equalsIgnoreCase(antigen) && allowable.contains(group.getVaccineCvx())){
				return group.getVaccineCvx();
			}
			if(allowable.contains(group.getVaccineCvx())){
				candidates.add(group);
			}
		}
		if(candidates.size() > 0){
			return candidates.get(0).getVaccineCvx();
		}
		return allowable.get(0);
	}
	

	private Date xmlToDate(XMLGregorianCalendar c) {
		if(c == null) return null;
		return c.toGregorianCalendar().getTime();
	}
	
	public void setLogText(boolean logText) {
		// TODO Auto-generated method stub
		
	}

	public boolean isLogText() {
		// TODO Auto-generated method stub
		return false;
	}

}
