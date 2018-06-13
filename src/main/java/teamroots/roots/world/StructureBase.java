package teamroots.roots.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teamroots.roots.util.Misc;

public class StructureBase {
	public ArrayList<String[]> layers = new ArrayList<String[]>();
	public int width = 0, length = 0;
	public Map<String, IBlockState> blocks = new HashMap<String, IBlockState>();
	
	public StructureBase(int w, int h){
		width = w;
		length = h;
	}
	
	public StructureBase addLayer(String[] layer){
		layers.add(layer);
		return this;
	}
	
	public void generateIn(World world, int x, int y, int z){
		for (int i = 0; i < layers.size(); i ++){
			for (int j = 0; j < layers.get(i).length; j ++){
				for (int k = 0; k < layers.get(i)[j].length(); k ++){
					placeBlock(world,new BlockPos(x+j-width/2, y+i,z+k-length/2), blocks.get(layers.get(i)[j].substring(k, k+1)));
				}
			}
		}
	}
	
	public void placeBlock(World world, BlockPos pos, IBlockState state){
		if (state.getBlock() != Blocks.AIR){
			world.setBlockState(pos, state);
		}
	}
	
	public StructureBase addBlockMapping(String name, IBlockState state){
		blocks.put(name, state);
		return this;
	}
}
