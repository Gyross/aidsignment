package unsw.graphics.world;

import java.awt.Color;
import java.util.ArrayList;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.Vector3;
import unsw.graphics.geometry.Point3D;
import unsw.graphics.geometry.TriangleFan3D;
import unsw.graphics.geometry.TriangleMesh;

public class WorldTestObjects {

	public static void drawCubes(GL3 gl, int n, float s){
		//draw some cubes in an n*n*n grid to test the display
        CoordFrame3D frame = CoordFrame3D.identity();
        
        for(int i = 0; i < n*n*n; i++){
        	if (i == (n+1)/2) continue;
        	int a = (i%n)-(n-1)/2;
        	int b = (i/n)%n-(n-1)/2;
        	int c = (i/(n*n))%n-(n-1)/2;
        	frame = CoordFrame3D.identity() 
        		.translate(3*a, 3*b, 3*c)
                .scale(s, s, s);
        	drawCube(gl, frame);
        }
	}
	
	public static void drawCube(GL3 gl, CoordFrame3D frame) {
        TriangleFan3D face = new TriangleFan3D(-1,-1,1, 1,-1,1, 1,1,1, -1,1,1);
        
        // Front
        Shader.setPenColor(gl, Color.RED);
        face.draw(gl, frame);
        
        // Left
        Shader.setPenColor(gl, Color.BLUE);
        face.draw(gl, frame.rotateY(-90));
        
        // Right
        Shader.setPenColor(gl, Color.GREEN);
        face.draw(gl, frame.rotateY(90));
        
        // Back
        Shader.setPenColor(gl, Color.CYAN);
        face.draw(gl, frame.rotateY(180));
        
        // Bottom
        Shader.setPenColor(gl, Color.YELLOW);
        face.draw(gl, frame.rotateX(-90));
        
        // Top
        Shader.setPenColor(gl, Color.MAGENTA);
        face.draw(gl, frame.rotateX(90));
    }
	
	
	public static TriangleMesh genSkyBox(GL3 gl, int width, int height, int depth){		
		int w = width;
		int h = height;
		int d = depth;
        Point3D p0 = new Point3D(-w, -h, -d);
        Point3D p1 = new Point3D(-w, -h,  d);
        Point3D p2 = new Point3D( w, -h, -d);
        Point3D p3 = new Point3D( w, -h,  d);
        
        Point3D p4 = new Point3D(-w,  h, -d);
        Point3D p5 = new Point3D(-w,  h,  d);
        Point3D p6 = new Point3D( w,  h, -d);
        Point3D p7 = new Point3D( w,  h,  d);
        
        
        ArrayList<Point3D> vertices = new ArrayList<Point3D>();
        vertices.add(p0);
        vertices.add(p1);
        vertices.add(p2);
        vertices.add(p3);
        vertices.add(p4);
        vertices.add(p5);
        vertices.add(p6);
        vertices.add(p7);
        
        ArrayList<Integer> ints = new ArrayList<Integer>();
        //top
        
        ints.add(0);
        ints.add(1);
        ints.add(3);
      
        ints.add(0);
        ints.add(3);
        ints.add(2);
        
        //bottom
          
         
        ints.add(5);
        ints.add(4);
        ints.add(7);
        
        ints.add(6);
        ints.add(7);
        ints.add(4);
        
        //a
        ints.add(5);
        ints.add(1);
        ints.add(0);
        
        ints.add(4);
        ints.add(5);
        ints.add(0);
        
        //b
        ints.add(2);
        ints.add(3);
        ints.add(7);
        
        ints.add(6);
        ints.add(2);
        ints.add(7);
        
        //c
        ints.add(0);
        ints.add(2);
        ints.add(6);
        
        ints.add(0);
        ints.add(6);
        ints.add(4);
        
        //d
        ints.add(3);
        ints.add(1);
        ints.add(7);
        
        ints.add(5);
        ints.add(7);
        ints.add(1);
        
        ArrayList<Vector3> normals = new ArrayList<Vector3>();
        
		
        for(int i = 0; i<vertices.size(); i++){
        	normals.add(new Vector3(0,1,0));
        }
		
		
		TriangleMesh t = new TriangleMesh(vertices, normals, ints);
		
		t.init(gl);
		return t;
	}
	
}
