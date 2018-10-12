package unsw.graphics.world.helper;

import java.awt.Color;
import java.util.ArrayList;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.Vector4;
import unsw.graphics.geometry.Point2D;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleMesh;
import unsw.graphics.world.Terrain;

public class RoadCreationHelper {

	
	public static Point2D transformPoint(Point2D p, CoordFrame3D c){
		Point3D p3D = new Point3D(p.getX(), 0, p.getY());
		Vector4 v = p3D.asHomogenous();
		v = c.getMatrix().multiply(v);
		p3D = v.asPoint3D();	
		return new Point2D(p3D.getX(), p3D.getZ());
	}
	

}
