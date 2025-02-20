package enstabretagne.applications.capricorn.radar;

import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.applications.capricorn.missile.Missile;
import enstabretagne.applications.capricorn.missile.MissileInit;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimuEngine;
import enstabretagne.base.logger.Logger;
import enstabretagne.base.time.LogicalDuration;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import enstabretagne.engine.SimEvent;
import java.util.List;

public class Radar extends EntiteSimulee implements ILocatable {

	public final RadarInit rIni;
	private boolean entityDetected = false;  // ✅ Track detection status
	private boolean missileLaunched = false; // ✅ Prevent multiple launches

	public Radar(SimuEngine engine, InitData ini) {
		super(engine, ini);
		rIni = (RadarInit) ini;
	}

	@Override
	public void Init() {
		super.Init();
		scheduleDetection();
	}

	@Override
	public Location position() {
		return rIni.position;
	}

	private void scheduleDetection() {
		SimEvent detectionEvent = new SimEvent(Now().add(rIni.scanInterval)) {
			@Override
			public void process() {
				detectEntities();
				rescheduleAt(Now().add(rIni.scanInterval));
				Post(this);
			}
		};
		Post(detectionEvent);
	}

	private void detectEntities() {
		List<EntiteSimulee> entities = engine.getAllEntities();
		entityDetected = false; // Reset detection status

		for (EntiteSimulee entity : entities) {
			if (entity instanceof Mobile) { // ✅ Detect Cesna (Plane)
				Mobile cesna = (Mobile) entity;
				double distance = computeDistance(position(), cesna.position());

				if (distance < rIni.portee) { // ✅ Inside radar range
					entityDetected = true;

					// ✅ Launch missile only once
					if (!missileLaunched) {
						launchMissile(cesna);
						missileLaunched = true; // Prevent multiple launches
					}
					break; // Stop checking after detection
				}
			}
		}
	}

	private double computeDistance(Location loc1, Location loc2) {
		double dx = loc1.position().getX() - loc2.position().getX();
		double dy = loc1.position().getY() - loc2.position().getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	private void launchMissile(Mobile target) {
		Logger.Information(this, "launchMissile", "Missile Launched at Cesna!");

		Vector2D initialVelocity = Vector2D.of(0, 0); // Start with no movement
		MissileInit missileInit = new MissileInit(
				"Missile1",
				position(),  // ✅ Start at radar's position
				initialVelocity,
				LogicalDuration.ofSeconds(1)
		);

		new Missile(engine, missileInit); // ✅ Create & launch the missile
	}

	public boolean isEntityDetected() {
		return entityDetected;
	}
}
