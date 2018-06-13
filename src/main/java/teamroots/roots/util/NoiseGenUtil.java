package teamroots.roots.util;

import java.util.Random;

public class NoiseGenUtil {
	static Random random = new Random();
	public static float getNoise(long seed, int x, int y){
		random.setSeed(simple_hash(new int[]{(int)seed,(int)(seed << 32),(int)Math.signum(y)*512+512,(int)Math.signum(x)*512+512,x,y},5));
		return random.nextFloat();
	}
	
	public static long getSeed(int seed, int x, int y){
		return simple_hash(new int[]{seed,(int)Math.signum(y)*512+512,(int)Math.signum(x)*512+512,x,y},5);
	}
	
	public static Random getRandom(int... args){
		return new Random((long)simple_hash(args,args.length));
	}
	
	public static float get2DNoise(long seed, int x, int z){
		return (float)Math.pow((
				80.0f*NoiseGenUtil.getOctave(seed, x, z, 112)
				+20.0f*NoiseGenUtil.getOctave(seed, x, z, 68)
				+6.0f*NoiseGenUtil.getOctave(seed, x, z, 34)
				+4.0f*NoiseGenUtil.getOctave(seed, x, z, 21)
				+2.0f*NoiseGenUtil.getOctave(seed, x, z, 11)
				+1.0f*NoiseGenUtil.getOctave(seed, x, z, 4)
				)/93.0f,1.6f);
	}
	
	public static int simple_hash(int[] is, int count){
		int i;
		int hash = 80238287;

		for (i = 0; i < count; i++){
			hash = (hash << 4) ^ (hash >> 28) ^ (is[i] * 5449 % 130651);
		}

		return hash % 75327403;
	}
	
	public static float fastSin(float x){
	    if (x < -3.14159265){
	        x += 6.28318531;
	    }
	    else {
	        if (x >  3.14159265){
	            x -= 6.28318531;
	        }
	    }

	    if (x < 0){
	        return (float) (1.27323954 * x + .405284735 * x * x);
	    }
	    else {
	        return (float) (1.27323954 * x - 0.405284735 * x * x);
	    }
	}
	
	public static float fastCos(float x){
	    if (x < -3.14159265){
	        x += 6.28318531;
	    }
	    else {
	        if (x >  3.14159265){
	            x -= 6.28318531;
	        }
	    }
	    x += 1.57079632;
	    if (x >  3.14159265){
	        x -= 6.28318531;
	    }

	    if (x < 0){
	        return (float) (1.27323954 * x + 0.405284735 * x * x);
	    }
	    else {
	        return (float) (1.27323954 * x - 0.405284735 * x * x);
	    }
	}

	public static float interpolate(float s, float e, float t){
	    float t2 = (1.0f-fastCos(t*3.14159265358979323f))/2.0f;
	    return(s*(1.0f-t2)+(e)*t2);
	}
	
	public static float bilinear(float ul, float ur, float dr, float dl, float t1, float t2){
		return interpolate(interpolate(ul,ur,t1),interpolate(dl,dr,t1),t2);
	}
	
	public static float getOctave(long seed, int x, int y, int dimen){
		return bilinear(getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen,(int)Math.floor((float)y/(float)dimen)*dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen+dimen,(int)Math.floor((float)y/(float)dimen)*dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen+dimen,(int)Math.floor((float)y/(float)dimen)*dimen+dimen)
				,getNoise(seed, (int)Math.floor((float)x/(float)dimen)*dimen,(int)Math.floor((float)y/(float)dimen)*dimen+dimen),
				Math.abs(((float)(((x)-Math.floor(((float)x/(float)dimen))*dimen)))/((float)dimen)),
				Math.abs(((float)(((y)-Math.floor(((float)y/(float)dimen))*dimen)))/((float)dimen)));
	}
}