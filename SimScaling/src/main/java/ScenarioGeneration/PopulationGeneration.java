package ScenarioGeneration;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.ConfigWriter;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ActivityParams;
import org.matsim.core.config.groups.QSimConfigGroup.LinkDynamics;
import org.matsim.core.config.groups.StrategyConfigGroup.StrategySettings;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleWriterV1;
import org.matsim.vehicles.Vehicles;
import org.matsim.vehicles.VehiclesFactory;

import ust.hk.praisehk.metamodelcalibration.matsimIntegration.LinkPCUCountModule;

public class PopulationGeneration {
public static void main(String[] args) {
	Config config= ConfigUtils.createConfig();
	Scenario scenario=ScenarioUtils.createScenario(config);
	Population population=scenario.getPopulation();
	PopulationFactory popFac=population.getFactory();
	Vehicles vehicles=scenario.getVehicles();
	VehiclesFactory vfac=vehicles.getFactory();
	VehicleType vType=vfac.createVehicleType(Id.create("car", VehicleType.class));
	vType.setPcuEquivalents(1);
	vehicles.addVehicleType(vType);
	for(int i=0;i<5000;i++) {
		Person person=popFac.createPerson(Id.createPersonId(i));
		Activity act1=popFac.createActivityFromCoord("Home1",new Coord(-5,1000));
		act1.setMaximumDuration(8*3600);
		act1.setEndTime(8*3600);
		Activity act2;
		Leg leg;
		leg=popFac.createLeg("car");
		
		
		if(i%2==0) {
			act2=popFac.createActivityFromCoord("Home2",new Coord(3005,1000));
			
		}else {
			act2=popFac.createActivityFromCoord("Home2",new Coord(3005,1000));
			
		}
		act2.setMaximumDuration(8*3600);
		act2.setStartTime(10*3600);
		
		
		Plan plan=popFac.createPlan();
		plan.addActivity(act1);
		plan.addLeg(leg);
		plan.addActivity(act2);
		
		person.addPlan(plan);
		population.addPerson(person);
		vehicles.addVehicle(vfac.createVehicle(Id.createVehicleId(i), vType));
		
	}
	new PopulationWriter(population).write("data/populationHome1Home2.xml");
	new VehicleWriterV1(vehicles).writeFile("data/vehicles.xml");
	ActivityParams params=new ActivityParams();
	params.setActivityType("Home1");
	//params.setMinimalDuration(7*3600);
	params.setTypicalDuration(8*3600);
	config.planCalcScore().addActivityParams(params);
	params=new ActivityParams();
	params.setActivityType("Home2");
	//params.setMinimalDuration(7*3600);
	params.setTypicalDuration(8*3600);
	config.planCalcScore().addActivityParams(params);
	StrategySettings st=new StrategySettings();
	config.qsim().setUsePersonIdForMissingVehicleId(true);
	config.global().setCoordinateSystem("arbitrary");
	config.parallelEventHandling().setNumberOfThreads(5);
	config.controler().setWritePlansInterval(50);
	config.global().setNumberOfThreads(4);
	config.strategy().setFractionOfIterationsToDisableInnovation(0.8);
	config.controler().setWriteEventsInterval(10);
	config.strategy().addParam("ModuleProbability_1", "0.8");
	config.strategy().addParam("Module_1", "ChangeExpBeta");
	config.strategy().addParam("ModuleProbability_2", "0.1");
	config.strategy().addParam("Module_2", "ReRoute");
	config.network().setInputFile("data/FiveLinkNetwork.xml");
	config.plans().setInputFile("data/populationHome1Home2.xml");
	config.vehicles().setVehiclesFile("data/vehicles.xml");
	config.controler().setLastIteration(20);
//	config.strategy().addParam("ModuleProbability_3", "0.1");
//	config.strategy().addParam("Module_3", "TimeAllocationMutator");
	config.controler().setLastIteration(20);
	config.qsim().setLinkDynamics(LinkDynamics.PassingQ);
	config.qsim().setUsingThreadpool(false);
	config.strategy().setFractionOfIterationsToDisableInnovation(.8);
	config.counts().setAverageCountsOverIterations(1);
	config.counts().setInputFile("data/emptyCounts.xml");
	config.qsim().setStartTime(6*3600);
	config.qsim().setEndTime(12*3600);
//	config.strategy().addParam("ModuleProbability_4", "0.05");
//	config.strategy().addParam("Module_4", "ChangeTripMode");
	new ConfigWriter(config).write("data/config.xml");
	Scenario scenario1=ScenarioUtils.loadScenario(config);
	Controler controler=new Controler(scenario1);
	
	controler.getConfig().controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);
	config.linkStats().setAverageLinkStatsOverIterations(1);
	config.linkStats().setWriteLinkStatsInterval(1);
	controler.run();
}
}
