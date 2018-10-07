/**
 * 
 * 
 * @author Daniel Iosifidis
 * 
 * 
 * This file will make a simple world for unsw graph
 */


package filemaker;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WorldMaker {

	private static int width = 80;
	private static int depth = 80;
	
	private static float tree_density = 1/25f;
	private static int n_trees = (int) ((float)width*(float)depth*tree_density);
	private static int n_bumps = (int) ((float)width*(float)depth*tree_density);

	private static float bump_width = 6;
	private static float bump_height = 3.5f;
	
	private static String runloc = System.getProperty("user.dir") + "/res/worlds/";
	private static String filename = "dantest1.json";
	
	private static List<String> lines = new ArrayList<String>();
	
	private static float[][] altitudes = new float[width][depth];
	private static float[][] trees = new float[n_trees][2];
	
	public static void main(String[] args){
		initAltitudes();
		initTreePositions();
		bumpAltitudes();
		writeFileArray();
		//printFileArray();
		writeToFile();
		System.out.println("written");
	}
	
	
	private static void initAltitudes(){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < depth; j++){
				altitudes[i][j] = 0;
			}
		}	
	}

	private static void initTreePositions(){
		for(int i = 0; i < n_trees; i++){
			trees[i] = generatePoint();
		}	
	}
	
	private static void bumpAltitudes(){
		for(int bump_num = 0; bump_num < n_bumps; bump_num++){
			float[] pt = generatePoint();
			generateBump(pt[0], pt[1]);
		}
	}
	
	
	private static float[] generatePoint(){
		float x = (float) Math.random()*(width-1);
		float z = (float) Math.random()*(depth-1);
		float[] point = {x, z};
		return point;
	}
	
	private static float gaussian(float x, float z){
		float g = (float) Math.exp(-1/((x*x + z*z))*bump_width*bump_width);
		return g;
	}
	
	private static void generateBump(float x, float z){
		float p = (Math.random() > 0.5) ? 1 : -1;
		float s = bump_height;
		for(int i = 0; i < width; i++){
			for(int j = 0; j < depth; j++){
				altitudes[i][j] += p*s*gaussian(x-i, z-j);
			}
		}	
		
	}
	
	private static void writeFileArray(){
		lines.add("{");
		lines.add("    \"width\" : " + width + ",");
		lines.add("    \"depth\" : " + depth + ",");
		
		lines.add("");
		lines.add("    \"sunlight\" : [ -1, 1, 0 ],");
		lines.add("");
		
		
		//altitude
		lines.add("    \"altitude\" : [");

		//first depth-1 lines of alt data
		for(int j = 0; j < depth-1; j++){
			String s = "   	   "; 
			for(int i = 0; i < width; i++){
				s += " " + altitudes[i][j] + ", ";
			}
			lines.add(s);
		}
		
		//final depth line (to avoid extra comma at the end)
		String s = "   	   "; 
		for(int i = 0; i < width-1; i++){
			s += " " + altitudes[i][depth-1] + ", ";
		}
		s += " " + altitudes[width-1][depth-1];
		lines.add(s);
		
		lines.add("    ],");
		lines.add("");
		
		//trees
		lines.add("    \"trees\" : [");
		for(int i = 0; i< n_trees; i++){
			String t = "   	    {";
			t += " \"x\" : ";
			t += trees[i][0];
			t += ", \"z\" : ";
			t += trees[i][1];
			t += " }";
			if(i < n_trees-1) t+=",";
			lines.add(t);
		}
		lines.add("    ],");
		lines.add("}");
	}
	
	private static void printFileArray(){
		int size = lines.size();
		for(int i = 0; i<size; i++){
			System.out.println(lines.get(i));
		}
	}
	
	
	private static void writeToFile(){
		String path = runloc + filename;
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			int size = lines.size();
			for(int i = 0; i<size; i++){
				bw.write(lines.get(i));
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}
	
}
