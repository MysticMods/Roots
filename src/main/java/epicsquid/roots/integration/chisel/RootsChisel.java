package epicsquid.roots.integration.chisel;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.Arrays;

public class RootsChisel {
  public static void integrate () {
    for (Block block : Arrays.asList(ModBlocks.runestone, ModBlocks.runestone_brick, ModBlocks.runestone_brick_alt, ModBlocks.chiseled_runestone)) {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setString("group", "rootsRunestone");
      tag.setString("block", block.getRegistryName().toString());
      FMLInterModComms.sendMessage("chisel", "add_variation", tag);
    }

    for (Block block : Arrays.asList(ModBlocks.runic_soil_air, ModBlocks.runic_soil_earth, ModBlocks.runic_soil_fire, ModBlocks.runic_soil_water)) {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setString("group", "rootsRunicSoilTypes");
      tag.setString("block", block.getRegistryName().toString());
      FMLInterModComms.sendMessage("chisel", "add_variation", tag);
    }
  }
}
