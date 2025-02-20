package enstabretagne.applications.capricorn.missile;
import java.util.List;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import enstabretagne.applications.capricorn.expertise.ILocatable;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.base.logger.Logger;
import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.InitData;
import enstabretagne.engine.SimEvent;
import enstabretagne.engine.SimuEngine;
import javafx.scene.paint.Color;
import enstabretagne.moniteur2D.VisualConverter;
import enstabretagne.applications.capricorn.mobile.Mobile;

public class Missile extends EntiteSimulee implements ILocatable {

    public final MissileInit ini;
    private Location p;
    private Vector2D v;
    private SimEvent updateEvent;

    public Missile(SimuEngine engine, InitData ini) {
        super(engine, ini);
        this.ini = (MissileInit) ini;
        this.p = this.ini.position;  // ✅ Fix: Use ini.position
        this.v = this.ini.velocity;  // ✅ Fix: Use ini.velocity
    }

    @Override
    public void Init() {
        super.Init();
        scheduleUpdate();
    }

    @Override
    public Location position() {
        return p;
    }

    private void scheduleUpdate() {
        updateEvent = new SimEvent(Now().add(ini.updateInterval)) {
            @Override
            public void process() {
                moveMissile();
                rescheduleAt(Now().add(ini.updateInterval));
                Post(updateEvent);
            }
        };
        Post(updateEvent);
    }

    private void moveMissile() {
        p = p.add(v);
        Logger.Information(this, "moveMissile", "Missile position: " + p);
    }

    public static void conMissile(Missile m, VisualConverter vc) {
        vc.drawCircle(true, VisualConverter.Layers.Objects, m.position().position().getX(), m.position().position().getY(), 3, Color.RED, "Missile");
    }

    private double computeDistance(Location loc1, Location loc2) {
        double dx = loc1.position().getX() - loc2.position().getX();
        double dy = loc1.position().getY() - loc2.position().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }


    private void updateMissileTarget() {
        List<EntiteSimulee> entities = engine.getAllEntities();
        List<Mobile> planes = entities.stream()
                .filter(e -> e instanceof Mobile)
                .map(e -> (Mobile) e)
                .toList();

        if (!planes.isEmpty()) {
            // ✅ Find the closest Cesna
            Mobile target = planes.get(0);
            double minDistance = computeDistance(p, target.position());

            for (Mobile plane : planes) {
                double distance = computeDistance(p, plane.position());
                if (distance < minDistance) {
                    target = plane;
                    minDistance = distance;
                }
            }

            // ✅ Update velocity to head towards Cesna
            Vector2D direction = target.position().position().subtract(p.position()).normalize();
            this.v = direction.multiply(20); // Adjust speed dynamically
        }
    }
}

