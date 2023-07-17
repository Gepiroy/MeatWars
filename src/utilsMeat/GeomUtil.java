package utilsMeat;

import org.bukkit.util.Vector;

public class GeomUtil {
	public static float getLookAtYaw(Vector motion) {
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0;
        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        return (float) (-yaw * 180 / Math.PI);
    }
	public static float getLookAtPitch(Vector motion) {
        double dx = motion.getX();
        double dz = motion.getZ();
        double dxz=(Math.sqrt(dx*dx+dz*dz));
        double pitch = 0;
        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
            	pitch = Math.cos(motion.getY()/dxz);
            } else {
            	pitch = Math.sin(motion.getY()/dxz);
            }
            pitch -= Math.atan(dz / dx);
        } else if (dz < 0) {
        	pitch = Math.PI;
        }
        return (float) (-pitch * 180 / Math.PI - 90);
    }
}
