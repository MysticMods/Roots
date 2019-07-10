package epicsquid.roots.spell;

import java.util.List;

import javax.annotation.Nullable;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.RegistryManager;
import epicsquid.roots.effect.EffectManager;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageAcidCloudFX;
import epicsquid.roots.network.fx.MessageFrostTouchFX;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class SpellIcedTouch extends SpellBase {
  public static String spellName = "spell_iced_touch";
  public static SpellIcedTouch instance = new SpellIcedTouch(spellName);

  public SpellIcedTouch(String name) {
    super(name, TextFormatting.DARK_AQUA, 22f / 255f, 142f / 255f, 255f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    this.castType = EnumCastType.INSTANTANEOUS;
    this.cooldown = 100;
    addCost(HerbRegistry.getHerbByName("dewgonia"), 0.015f);
    addIngredients(new ItemStack(ModItems.dewgonia), new ItemStack(ModItems.bark_birch), new ItemStack(Items.SNOWBALL), new ItemStack(ModItems.bark_birch),
        new ItemStack(Items.SNOWBALL));

    acceptModules(ModuleRegistry.module_touch);
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    World world = player.world;
    if (modules.contains(ModuleRegistry.module_touch)) {
        player.addPotionEffect(new PotionEffect(RegistryManager.freeze, 600));
//      EffectManager.assignEffect(player, EffectManager.effect_freeze.getName(), 600, new NBTTagCompound());
    } else {
      RayTraceResult result = this.rayTrace(player, player.isSneaking() ? 1 : 10);
      if (result != null && (!player.isSneaking() && result.typeOfHit == RayTraceResult.Type.BLOCK)) {
        BlockPos pos = result.getBlockPos().offset(result.sideHit);
        IBlockState state = world.getBlockState(pos);
        if (!world.isRemote) {
          if (state.getBlock() == Blocks.FIRE) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
          } else if (state.getBlock() == Blocks.LAVA) {
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
          } else if (state.getBlock() == Blocks.WATER) {
            world.setBlockState(pos, Blocks.ICE.getDefaultState());
          } else if (world.isAirBlock(pos)) {
            world.setBlockState(result.getBlockPos().offset(result.sideHit), Blocks.SNOW_LAYER.getDefaultState());
          }
        } else {
          if (state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.LAVA) {
            player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5f, 1);
          }
        }
        return true;
      }
    }
    return false;
  }

  @Nullable
  public RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance) {
    Vec3d vec3d = player.getPositionEyes(1.0F);
    Vec3d vec3d1 = player.getLook(1.0F);
    Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
    return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
  }
}

