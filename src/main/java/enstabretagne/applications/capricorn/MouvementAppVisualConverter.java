package enstabretagne.applications.capricorn;

import enstabretagne.applications.capricorn.environnement.Environement;
import enstabretagne.applications.capricorn.mobile.Mobile;
import enstabretagne.applications.capricorn.radar.Radar;
import enstabretagne.applications.capricorn.missile.Missile;
import enstabretagne.engine.SimuEngine;
import enstabretagne.moniteur2D.VisualConverter;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class MouvementAppVisualConverter extends VisualConverter {

	@Override
	public void init(SimuEngine engine, Canvas background, Canvas objects, double maxX, double maxY) {
		super.init(engine, background, objects, maxX, maxY);

		addVisualMapper(Environement.class, this::convEnv);
		addVisualMapper(Radar.class, this::convRadar);
		addVisualMapper(Mobile.class, this::convMobile);
		addVisualMapper(Missile.class, this::convMissile);
	}

	public void convMobile(Mobile m) {
		drawCircle(true, VisualConverter.Layers.Objects, m.position().position().getX(), m.position().position().getY(), 5, Color.BLUE, m.ini.name);
	}

	public void convRadar(Radar r) {
		// ✅ The radar center turns red ONLY if an entity is inside range
		Color radarCenterColor = r.isEntityDetected() ? Color.RED : Color.AQUA;

		// ✅ The detection range circle ALWAYS remains AQUA
		drawCircle(false, VisualConverter.Layers.Objects,
				r.position().position().getX(),
				r.position().position().getY(),
				r.rIni.portee,
				Color.AQUA,
				"");

		// ✅ The radar center turns RED only if an entity is inside
		drawCircle(true, VisualConverter.Layers.Objects,
				r.position().position().getX(),
				r.position().position().getY(),
				5,
				radarCenterColor,
				r.rIni.name);
	}

	public void convMissile(Missile m) {
		drawCircle(true, VisualConverter.Layers.Objects, m.position().position().getX(), m.position().position().getY(), 3, Color.RED, "Missile");
	}

	public void convEnv(Environement env) {
		for (var pos : env.ini.positions())
			drawCircle(true, VisualConverter.Layers.BackGround, pos.position().getX(), pos.position().getY(), 5, Color.BLACK, pos.nom());
	}
}
