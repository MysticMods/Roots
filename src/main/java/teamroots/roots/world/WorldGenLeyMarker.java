package teamroots.roots.world;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.fml.common.IWorldGenerator;
import teamroots.roots.RegistryManager;
import teamroots.roots.entity.EntityAuspiciousPoint;
import teamroots.roots.ley.LeyLineNoiseGen;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageTEUpdate;
import teamroots.roots.util.NoiseGenUtil;

public class WorldGenLeyMarker extends StructureBase implements IWorldGenerator {
	public WorldGenLeyMarker(){
		super(3,3);
		addBlockMapping("M",Blocks.MOSSY_COBBLESTONE.getDefaultState());
		addBlockMapping("C",Blocks.COBBLESTONE.getDefaultState());
		addBlockMapping("W",Blocks.COBBLESTONE_WALL.getDefaultState());
		addBlockMapping(" ",Blocks.AIR.getDefaultState());
		addBlockMapping("L",Blocks.LAPIS_BLOCK.getDefaultState());
		addLayer(new String[]{
				"MMM",
				"MMM",
				"MMM"
		});
		addLayer(new String[]{
				"CMM",
				"CCM",
				"MMC"
		});
		addLayer(new String[]{
				" M ",
				"MMC",
				" M "
		});
		addLayer(new String[]{
				" M ",
				"CCM",
				" M "
		});
		addLayer(new String[]{
				" M ",
				"CCC",
				" C "
		});
		addLayer(new String[]{
				" M ",
				"CCM",
				" M "
		});
		addLayer(new String[]{
				" C ",
				"MCC",
				" C "
		});
		addLayer(new String[]{
				" C ",
				"CCC",
				" C "
		});
		addLayer(new String[]{
				"CC ",
				"CCC",
				" CC"
		});
		addLayer(new String[]{
				"C C",
				" L ",
				"C C"
		});
		addLayer(new String[]{
				" CC",
				"C C",
				"CC "
		});
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (world.provider.getDimension() == DimensionType.OVERWORLD.getId()){
			BlockPos leyPos = LeyLineNoiseGen.getNearestLeyLine(world, new BlockPos(chunkX*16,0,chunkZ*16));
			if (leyPos.getY() > 0 && leyPos.getX() >= chunkX*16 && leyPos.getZ() >= chunkZ*16 && leyPos.getX() < chunkX*16+16 && leyPos.getZ() < chunkZ*16+16){
				boolean generated = false;
				for (int i = 0; i < 8 && !generated; i ++){
					double ang = random.nextDouble()*360.0;
					int offX = (int)(8.0*Math.sin(Math.toRadians(ang)));
					int offZ = (int)(8.0*Math.cos(Math.toRadians(ang)));
					BlockPos placePos = new BlockPos(leyPos.getX()+offX,leyPos.getY()-1,leyPos.getZ()+offZ);
					if (world.getBlockState(placePos).getBlock() instanceof BlockGrass || world.getBlockState(placePos).getBlock() instanceof BlockSand){
						this.generateIn(world, placePos.getX(), placePos.getY()-1, placePos.getZ());
						generated = true;
					}
				}
			}
		}
	}
	
}
