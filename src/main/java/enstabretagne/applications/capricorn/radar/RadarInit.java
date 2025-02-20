package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.engine.InitData;
import enstabretagne.base.time.LogicalDuration;

public class RadarInit extends InitData {
	public final Location position;
	public final double portee;
	public final LogicalDuration scanInterval; // ✅ Fix: Added scanInterval

	public RadarInit(String name, Location position, double portee, LogicalDuration scanInterval) {
		super(name);
		this.position = position;
		this.portee = portee;
		this.scanInterval = scanInterval;
	}
}
