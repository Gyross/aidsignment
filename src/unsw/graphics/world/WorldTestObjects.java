package unsw.graphics.world;

import java.awt.Color;

import com.jogamp.opengl.GL3;

import unsw.graphics.CoordFrame3D;
import unsw.graphics.Shader;
import unsw.graphics.geometry.TriangleFan3D;

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
	
}
