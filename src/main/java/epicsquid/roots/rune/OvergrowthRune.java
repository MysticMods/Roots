package epicsquid.roots.rune;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.tileentity.TileEntityWildrootRune;
import epicsquid.roots.util.RgbColorUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OvergrowthRune extends RuneBase {

    private List<BlockPos> cobbleBlocks = new ArrayList<>();
    private int checkTime = 0;

    public OvergrowthRune(){
        setIncense(ModItems.terra_moss);
        setColor(RgbColorUtil.OVERGROWTH);
        setRuneName("overgrowth_rune");
    }

    @Override
    public void update(World world, BlockPos pos) {
        if(world.isRemote){
           return;
        }
        checkTime--;
        if(checkTime <= 0){
            checkTime = new Random().nextInt(50) + 100;
            if(cobbleBlocks.isEmpty()){
                this.cobbleBlocks = Util.getBlocksWithinRadius(world, pos, 6, 6, 6, Blocks.COBBLESTONE);
            }

            if(!cobbleBlocks.isEmpty()){
                int getNumb = 0;
                if(cobbleBlocks.size() > 1){
                    getNumb = new Random().nextInt(cobbleBlocks.size() - 1);
                }


                BlockPos getPos = this.cobbleBlocks.get(getNumb);
                this.cobbleBlocks.remove(getNumb);
                world.setBlockState(getPos, Blocks.MOSSY_COBBLESTONE.getDefaultState());

                Random random = new Random();
                for (int i = 0; i < 50; i++) {
                    ClientProxy.particleRenderer.spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), getPos.getX() + 0.5, getPos.getY() + 0.5, getPos.getZ() + 0.5, random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
                            random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.855 + random.nextDouble() * 0.05, 0.710, 0.943 - random.nextDouble() * 0.05, 1, random.nextDouble() + 0.5, random.nextDouble() * 2);
                }

            }

        }


    }

    @Override
    public void saveToEntity(NBTTagCompound tag) {
        super.saveToEntity(tag);
    }

    @Override
    public void readFromEntity(NBTTagCompound tag) {

    }

    @Override
    public void activate(TileEntityWildrootRune entity, EntityPlayer player) {

    }
}
