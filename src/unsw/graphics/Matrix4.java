/**
 * 
 */
package unsw.graphics;

import java.util.Arrays;

import unsw.graphics.geometry.Point3D;

/**
 * A 4x4 matrix.
 * 
 * This class is immutable.
 * 
 * @author Robert Clifton-Everest
 *
 */
public class Matrix4 {
    // Matrix is stored in column-major order to match OpenGL
    private float[] values;
    
    /**
     * Construct a matrix from the given flat array of values.
     * @param values The values for the matrix in column-major order.
     */
    public Matrix4(float[] values) {
        if (values.length != 16)
            throw new IllegalArgumentException("Matrix4 constructor passed an array of length " + values.length);
        this.values = Arrays.copyOf(values, 16);
    }
    
    /**
     * Create an identity matrix.
     */
    public static Matrix4 identity() {
        float[] values = new float[] {
            1, 0, 0, 0, // i
            0, 1, 0, 0, // j
            0, 0, 1, 0, // k
            0, 0, 0, 1  // phi
        };
        return new Matrix4(values);
    }
    
    /**
     * Create a translation matrix to the given point.
     * @param x The x coordinate of the new origin
     * @param y The y coordinate of the new origin
     * @return
     */
    public static Matrix4 translation(float x, float y, float z) {
        float[] values = new float[] {
            1, 0, 0, 0, // i
            0, 1, 0, 0, // j
            0, 0, 1, 0, // k
            x, y, z, 1  // phi
        };
        return new Matrix4(values);
    }
    
    /**
     * Create a translation matrix to the given point.
     * @param x The x coordinate of the new origin
     * @param y The y coordinate of the new origin
     * @return
     */
    public static Matrix4 translation(Point3D point) {
        return translation(point.getX(), point.getY(), point.getZ());
    }
    
    /**
     * Create a rotation matrix for rotation around the x-axis.
     * @param degrees Rotate around the axis this many degrees
     * @return
     */
    public static Matrix4 rotationX(float degrees) {
        double radians = Math.toRadians(degrees);
        float[] values = new float[] {
            1, 0, 0, 0,                                                  // i
            0, (float) Math.cos(radians), (float) Math.sin(radians), 0,  // j
            0, (float) -Math.sin(radians), (float) Math.cos(radians), 0, // k
            0, 0, 0, 1                                                   // phi
        };
        return new Matrix4(values);
    }
    
    
    /**
     * Create a rotation matrix for rotation around the y-axis.
     * @param degrees Rotate around the axis this many degrees
     * @return
     */
    public static Matrix4 rotationY(float degrees) {
        double radians = Math.toRadians(degrees);
        float[] values = new float[] {
            (float) Math.cos(radians), 0, (float) -Math.sin(radians), 0, // i
            0, 1, 0, 0,                                                  // j
            (float) Math.sin(radians), 0, (float) Math.cos(radians), 0,  // k
            0, 0, 0, 1                                                   // phi
        };
        return new Matrix4(values);
    }
    
    
    /**
     * Create a rotation matrix for rotation around the x-axis.
     * @param degrees Rotate around the axis this many degrees
     * @return
     */
    public static Matrix4 rotationZ(float degrees) {
        double radians = Math.toRadians(degrees);
        float[] values = new float[] {
            (float) Math.cos(radians), (float) Math.sin(radians), 0, 0,  // i
            (float) -Math.sin(radians), (float) Math.cos(radians), 0, 0, // j
            0, 0, 1, 0,                                                  // k
            0, 0, 0, 1                                                   // phi
        };
        return new Matrix4(values);
    }
    
    /**
     * Create a scale matrix.
     * @param x Scale the i-axis by this much
     * @param y Scale the j-axis by this much
     * @return
     */
    public static Matrix4 scale(float x, float y, float z) {
        float[] values = new float[] {
            x, 0, 0, 0, // i
            0, y, 0, 0, // j
            0, 0, z, 0, // k
            0, 0, 0, 1  // phi
        };
        return new Matrix4(values);
    }
    
    /**
     * Create an orthographic projection matrix.
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     * @return
     */
    public static Matrix4 orthographic(float left, float right, float bottom, float top, float near,
            float far) {
        float[] values = new float[] {
            2/(right-left), 0, 0, 0,                                                          // i
            0, 2/(top-bottom), 0, 0,                                                          // j
            0, 0, -2/(far-near), 0,                                                           // k
            -(right+left)/(right-left), -(top+bottom)/(top-bottom), -(far+near)/(far-near), 1 // phi
        };
        return new Matrix4(values);
    }
    
    /**
     * Create a perspective projection matrix defined by a frustum.
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     * @return
     */
    public static Matrix4 frustum(float left, float right, float bottom, float top, float near, 
            float far) {
        float[] values = new float[] {
            2*near/(right-left), 0, 0, 0,                                                     // i
            0, 2*near/(top-bottom), 0, 0,                                                     // j
            (right+left)/(right-left), (top+bottom)/(top-bottom), -(far+near)/(far-near), -1, // k
            0, 0, -2*far*near/(far-near), 0                                                   // phi
        };
        return new Matrix4(values);
    }
    
    /**
     * Create a perspective projection matrix defined by a the vetical field of view, the aspect
     * ratio and near and far clipping planes.
     * @param fovy The field of view in degrees
     * @param aspectRatio
     * @param near
     * @param far
     * @return
     */
    public static Matrix4 perspective(float fovy, float aspectRatio, float near, float far) {
        float halfHeight = (float) (near*Math.tan(Math.toRadians(fovy)/2));
        return Matrix4.frustum(-aspectRatio*halfHeight, aspectRatio*halfHeight, 
                -halfHeight, halfHeight, near, far);
    }
    
    @Override
    public String toString() {
        String str = "";
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                str += values[x*4 + y] + " ";
            }
            str += "\n";
        }
        return str;
    }
    
    /**
     * Multiply this matrix by the given matrix.
     * @param mat
     * @return
     */
    public Matrix4 multiply(Matrix4 mat) {
        float[] r = new float[16];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                r[x*4 + y] = getRow(y).dotp(mat.getColumn(x));
            }
        }
        return new Matrix4(r);
    }
    
    /**
     * Multiply this matrix by the given (column) vector.
     * @param mat
     * @return
     */
    public Vector4 multiply(Vector4 v) {
        float[] r = new float[4];
        for (int y = 0; y < 4; y++) {
            r[y] = getRow(y).dotp(v);
        }
        return new Vector4(r);
    }
    
    private Vector4 getColumn(int x) {
        float[] vec = new float[4];
        for (int y = 0; y < 4; y++) {
            vec[y] = values[x*4 + y];
        }
        return new Vector4(vec);
    }
    
    private Vector4 getRow(int y) {
        float[] vec = new float[4];
        for (int x = 0; x < 4; x++) {
            vec[x] = values[x*4 + y];
        }
        return new Vector4(vec);
    }

    /**
     * Get the values stored in this matrix in column-major order.
     * @return
     */
    public float[] getValues() {
        return Arrays.copyOf(values, 16);
    }
    
    /**
     * calculation for the inverse rotation matrix of this rotation matrix
     * Checked By Daniel
     * Source: http://semath.info/src/inverse-cofactor-ex4.html
     * @return
     */
    public Matrix4 getInverseRot(){
    	float a11 = A(2,2)*A(3,3) - A(2,3)*A(3,2);
    	float a12 =	A(1,3)*A(3,2) - A(1,2)*A(3,3);
    	float a13 = A(1,2)*A(2,3) - A(1,3)*A(2,2);
    	float a14 = 0;
    	
    	float a21 = A(2,3)*A(3,1) - A(2,1)*A(3,3);
    	float a22 = A(1,1)*A(3,3) - A(1,3)*A(3,1);
    	float a23 = A(1,3)*A(2,1) - A(1,1)*A(2,3);
    	float a24 = 0;
    	
    	float a31 = A(2,1)*A(3,2) - A(2,2)*A(3,1);
    	float a32 = A(1,2)*A(3,1) - A(1,1)*A(3,2);
    	float a33 = A(1,1)*A(2,2) - A(2,1)*A(1,2);
    	float a34 = 0;
    	
    	float a41 = 0;
    	float a42 = 0;
    	float a43 = 0;
    	float a44 = 1;
    	
    	float[] vals = {a11, a21, a31, a41, a12, a22, a32, a42, a13, a23, a33, a43, a14, a24, a34, a44};
    	
    	return new Matrix4(vals);
    }
    
    //assume direction is unitary
    public Matrix4 rotateDirection(Vector4 direction){
    	Point3D point = direction.asPoint3D();
    	float x0 = point.getX();
    	float y0 = point.getY();
    	float z0 = point.getZ();
    	
    	//cross product of 0,0,1 with x,y,z
    	float r = (x0 == 0 && y0 == 0) ? 1 : (float) Math.sqrt(x0*x0 + y0*y0);
    	float x = y0/r;
    	float y = -x0/r;
    	float z = 0;
    	
    	float cos = z0;
    	float sin = (float) r;
    	if(x*y > 0) sin = -sin;
    	
    	float[] p1 = new float[] {
    			0, z, -y, 0,
    			-z, 0, x, 0,
    			y, -x, 0, 0,
    			0, 0, 0, 0
    			};
    	   	
    	float[] p2 = new float[] {
    			x*x, x*y, x*z, 0,
    			x*y, y*y, y*z, 0,
    			x*z, z*y, z*z, 0,
    			0, 0, 0, 0
    			};
    	
    	float[] id = new float[] {
    			1, 0, 0, 0,
    			0, 1, 0, 0,
    			0, 0, 1, 0,
    			0, 0, 0, 0
    			};
    	
    	float[] p4 = new float[16];
 
    	for(int i = 0; i<16; i++)
    		p4[i] = p1[i]*sin + (id[i] - p2[i])*cos + p2[i];
    	p4[15] = 1;
    	Matrix4 m = new Matrix4(p4);
    	
    	return m;
    }
    
    private float A(int row, int col){
    	return values[(col-1)*4 + (row-1)];
    }

}
