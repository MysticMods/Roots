package epicsquid.roots.integration.chisel;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.Arrays;

public class RootsChisel {
  public static void init() {
    for (Block block : ModBlocks.runestoneBlocks) {
      CompoundNBT tag = new CompoundNBT();
      tag.setString("group", "rootsRunestone");
      tag.setString("block", block.getRegistryName().toString());
      FMLInterModComms.sendMessage("chisel", "add_variation", tag);
    }

    for (Block block : ModBlocks.runedObsidianBlocks) {
      CompoundNBT tag = new CompoundNBT();
      tag.setString("group", "rootsRunedObsidian");
      tag.setString("block", block.getRegistryName().toString());
      FMLInterModComms.sendMessage("chisel", "add_variation", tag);
    }

    for (Block block : Arrays.asList(ModBlocks.elemental_soil_air, ModBlocks.elemental_soil_earth, ModBlocks.elemental_soil_fire, ModBlocks.elemental_soil_water)) {
      CompoundNBT tag = new CompoundNBT();
      tag.setString("group", "rootsRunicSoilTypes");
      tag.setString("block", block.getRegistryName().toString());
      FMLInterModComms.sendMessage("chisel", "add_variation", tag);
    }
  }
}
