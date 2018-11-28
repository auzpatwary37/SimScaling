package ScenarioGeneration;

import java.util.HashSet;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.counts.Counts;
import org.matsim.counts.CountsWriter;

public class NetworkGenerator {
	public static void main(String[] args) {
		Config config= ConfigUtils.createConfig();
		Scenario scenario=ScenarioUtils.createScenario(config);
		Network network=scenario.getNetwork();
		NetworkFactory netFac=network.getFactory();
		Node node1=netFac.createNode(Id.createNodeId("1"), new Coord(0,1000));
		Node node2=netFac.createNode(Id.createNodeId("2"), new Coord(1000,1000));
		Node node3=netFac.createNode(Id.createNodeId("3"), new Coord(1500,2000));
		Node node4=netFac.createNode(Id.createNodeId("4"), new Coord(1500,0));
		Node node5=netFac.createNode(Id.createNodeId("5"), new Coord(3000,1000));
		Node nodeO=netFac.createNode(Id.createNodeId("O"), new Coord(-10,1000));
		Node nodeD=netFac.createNode(Id.createNodeId("D"), new Coord(3010,1000));
		network.addNode(node1);
		network.addNode(node2);
		network.addNode(node3);
		network.addNode(node4);
		network.addNode(node5);
		network.addNode(nodeO);
		network.addNode(nodeD);
		network.addLink(netFac.createLink(Id.createLinkId("1_2"), node1, node2));
		network.addLink(netFac.createLink(Id.createLinkId("2_3"), node2, node3));
		network.addLink(netFac.createLink(Id.createLinkId("2_4"), node2, node4));
		network.addLink(netFac.createLink(Id.createLinkId("3_5"), node3, node5));
		network.addLink(netFac.createLink(Id.createLinkId("4_5"), node4, node5));
		network.addLink(netFac.createLink(Id.createLinkId("O_1"), nodeO, node1));
		network.addLink(netFac.createLink(Id.createLinkId("5_D"), node5, nodeD));
		Counts<Link> counts=new Counts<Link>();
		
		for(Link l: network.getLinks().values()) {
			HashSet<String> modes=new HashSet<String>();
			modes.add("car");
			l.setAllowedModes(modes);
			if(l.getId().toString().equals("1_2")) {
				l.setCapacity(1800);
			}else {
				l.setCapacity(500);
			}
			l.setFreespeed(15*1000/3600);
			l.setNumberOfLanes(1);
			counts.createAndAddCount(l.getId(), l.getId().toString());
			for(int i=1;i<=24;i++) {
				counts.getCount(l.getId()).createVolume(i, 0);
			}
		}
		new NetworkWriter(network).write("data/FiveLinkNetwork.xml");
		new CountsWriter(counts).write("data/emptyCounts.xml");
		System.out.println("Finished");
		
	}
}
