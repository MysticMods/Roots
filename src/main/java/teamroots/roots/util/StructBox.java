package teamroots.roots.util;

public class StructBox {
	public double x1, y1, z1, x2, y2, z2;
	public boolean[] inversions = new boolean[6];
	public StructUV[] textures = new StructUV[6];
	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param textures
	 * 
	 * Order the textures and inversions like so: up (pos Y), down (neg Y), north (neg Z), south (pos Z), west (neg X), east (pos X)
	 */
	public StructBox(double x1, double y1, double z1, double x2, double y2, double z2, StructUV[] textures){
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.textures = textures;
	}
}
