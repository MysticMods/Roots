package epicsquid.roots.spell;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SpellFeyLight extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("cloud_berry", 0.125));

  public static String spellName = "spell_fey_light";
  public static SpellFeyLight instance = new SpellFeyLight(spellName);

  public SpellFeyLight(String name) {
    super(name, TextFormatting.LIGHT_PURPLE, 247f / 255f, 246 / 255f, 210f / 255f, 227f / 255f, 81f / 255f, 244f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(Blocks.LIT_PUMPKIN)),
        new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, DoublePlantBlock.EnumPlantType.SUNFLOWER.getMeta()),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.bark_acacia),
        new ItemStack(ModItems.cloud_berry)
    );
  }

  @Override
  public boolean cast(PlayerEntity player, List<SpellModule> modules) {
    World world = player.world;
    RayTraceResult result = this.rayTrace(player, player.isSneaking() ? 1 : 10);
    if (result != null && (!player.isSneaking() && result.typeOfHit == RayTraceResult.Type.BLOCK)) {
      BlockPos pos = result.getBlockPos().offset(result.sideHit);
      if (world.isAirBlock(pos)) {
        if (!world.isRemote) {
          world.setBlockState(pos, ModBlocks.fey_light.getDefaultState());
          world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.PLAYERS, 0.25f, 1);
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.getProperty(PROP_CAST_TYPE);
    this.cooldown = properties.getProperty(PROP_COOLDOWN);
  }

  @Nullable
  public RayTraceResult rayTrace(PlayerEntity player, double blockReachDistance) {
    Vec3d vec3d = player.getPositionEyes(1.0F);
    Vec3d vec3d1 = player.getLook(1.0F);
    Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
    return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
  }
}

