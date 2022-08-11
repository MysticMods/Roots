package epicsquid.roots.util;

public class ColorLight {
	public final int light1;
	public final int light2;
	public final int alpha;
	public final int red;
	public final int green;
	public final int blue;
	
	public ColorLight(int colour, int light) {
		light1 = light >> 0x10 & 0xFFFF;
		light2 = light & 0xFFFF;
		alpha = colour >> 24 & 0xFF;
		red = colour >> 16 & 0xFF;
		green = colour >> 8 & 0xFF;
		blue = colour & 0xFF;
	}
}
