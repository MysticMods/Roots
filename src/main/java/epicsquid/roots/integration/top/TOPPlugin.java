package epicsquid.roots.integration.top;

import epicsquid.roots.block.BlockBonfire;
import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.tileentity.TileEntityBonfire;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.function.Function;

@SuppressWarnings("unused")
public class TOPPlugin implements Function<ITheOneProbe, Void>, IProbeInfoProvider {
  @Override
  public String getID() {
    return "roots:top_integration";
  }

  @Override
  public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
    Block block = blockState.getBlock();
    if (block == ModBlocks.grove_stone) {
      if (blockState.getValue(BlockGroveStone.VALID)) {
        probeInfo.text(TextFormatting.GREEN + "" + TextFormatting.BOLD + "{*roots.hud.grove_stone.valid*}");
      }
    } else if (block instanceof BlockBonfire) {
      TileEntityBonfire te = (TileEntityBonfire) world.getTileEntity(data.getPos());
      if (te != null && te.getBurnTime() > 0) {
        int duration = 0;
        if (te.getLastRecipeUsed() != null) {
          ItemStack result = te.getLastRecipeUsed().getResult();
          duration = te.getLastRecipeUsed().getBurnTime();
          probeInfo.text(TextFormatting.GOLD + "{*roots.hud.top.pyre.recipe*} {*" + result.getTranslationKey() + ".name*}");
        } else if (te.getLastRitualUsed() != null) {
          RitualBase ritual = te.getLastRitualUsed();
          duration = ritual.getDuration();
          probeInfo.text(ritual.getFormat() + "{*roots.hud.top.pyre.ritual*} {*" + "roots.ritual." + ritual.getName() + ".name*}");
        } else {
          return;
        }
        int remaining = duration - (duration - te.getBurnTime());
        probeInfo.text("{*roots.hud.pyre.progress*}" + String.format("%.2f", remaining / 20.0 / 60.0) + " {*roots.hud.pyre.progress_info*}");
      }
    }
  }

  @Override
  public Void apply(ITheOneProbe iTheOneProbe) {
    init(iTheOneProbe);
    return null;
  }

  public void init(ITheOneProbe top) {
    top.registerProvider(this);
  }
}
