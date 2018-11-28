package ScenarioGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.utils.collections.Tuple;

import ust.hk.praisehk.metamodelcalibration.measurements.Measurement;
import ust.hk.praisehk.metamodelcalibration.measurements.Measurements;
import ust.hk.praisehk.metamodelcalibration.measurements.MeasurementsWriter;

public class MeasuremetnsCreator {
	public static void main(String[] args) {
		Map<String,Tuple<Double,Double>>timeBean=new HashMap<>();
		for(int i=1;i<=24;i++) {
			timeBean.put(Integer.toString(i),new Tuple<Double,Double>((i-1)*3600.,i*3600.));
		}
		Measurements m=Measurements.createMeasurements(timeBean);
		Id<Measurement> MeasurementId=Id.create("1_2",Measurement.class);
		m.createAnadAddMeasurement("1_2");
		Measurement mm=m.getMeasurements().get(MeasurementId);
		ArrayList<Id<Link>>linkList=new ArrayList<>();
		linkList.add(Id.createLinkId(MeasurementId.toString()));
		mm.setAttribute(mm.linkListAttributeName,linkList);
		for(String s:timeBean.keySet()) {
			mm.addVolume(s, 0);
		}
		
		MeasurementId=Id.create("2_3",Measurement.class);
		m.createAnadAddMeasurement("2_3");
		mm=m.getMeasurements().get(MeasurementId);
		linkList=new ArrayList<>();
		linkList.add(Id.createLinkId(MeasurementId.toString()));
		mm.setAttribute(mm.linkListAttributeName,linkList);
		for(String s:timeBean.keySet()) {
			mm.addVolume(s, 0);
		}
		
		MeasurementId=Id.create("2_4",Measurement.class);
		m.createAnadAddMeasurement("2_4");
		mm=m.getMeasurements().get(MeasurementId);
		linkList=new ArrayList<>();
		linkList.add(Id.createLinkId(MeasurementId.toString()));
		mm.setAttribute(mm.linkListAttributeName,linkList);
		for(String s:timeBean.keySet()) {
			mm.addVolume(s, 0);
		}
		
		MeasurementId=Id.create("3_5",Measurement.class);
		m.createAnadAddMeasurement("3_5");
		mm=m.getMeasurements().get(MeasurementId);
		linkList=new ArrayList<>();
		linkList.add(Id.createLinkId(MeasurementId.toString()));
		mm.setAttribute(mm.linkListAttributeName,linkList);
		for(String s:timeBean.keySet()) {
			mm.addVolume(s, 0);
		}
		
		MeasurementId=Id.create("4_5",Measurement.class);
		m.createAnadAddMeasurement("4_5");
		mm=m.getMeasurements().get(MeasurementId);
		linkList=new ArrayList<>();
		linkList.add(Id.createLinkId(MeasurementId.toString()));
		mm.setAttribute(mm.linkListAttributeName,linkList);
		for(String s:timeBean.keySet()) {
			mm.addVolume(s, 0);
		}
		
		new MeasurementsWriter(m).write("data/emptyMeasurements.xml");
	}
}
