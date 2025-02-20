package enstabretagne.applications.capricorn.missile;

import org.apache.commons.geometry.euclidean.twod.Vector2D;
import enstabretagne.applications.capricorn.expertise.Location;
import enstabretagne.engine.InitData;
import enstabretagne.base.time.LogicalDuration;

public class MissileInit extends InitData {
    public final Location position;
    public final Vector2D velocity;
    public final LogicalDuration updateInterval;

    public MissileInit(String name, Location position, Vector2D velocity, LogicalDuration updateInterval) {
        super(name);
        this.position = position;
        this.velocity = velocity;
        this.updateInterval = updateInterval;
    }
}

