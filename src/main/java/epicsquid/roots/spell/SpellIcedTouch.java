package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModPotions;
import epicsquid.roots.network.fx.MessageIcedTouchFX;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SpellIcedTouch extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(100);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("dewgonia", 0.015));
  public static Property<Integer> PROP_TOUCH_DURATION = new Property<>("touch_duration", 600).setDescription("the duration of the spell effect on the player");

  public static String spellName = "spell_iced_touch";
  public static SpellIcedTouch instance = new SpellIcedTouch(spellName);

  private int touchDuration;

  public SpellIcedTouch(String name) {
    super(name, TextFormatting.DARK_AQUA, 22f / 255f, 142f / 255f, 255f / 255f, 255f / 255f, 255f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_TOUCH_DURATION);
  }

  @Override
  public void init () {
    addIngredients(
        new ItemStack(ModItems.dewgonia),
        new ItemStack(Item.getItemFromBlock(Blocks.SNOW)),
        new ItemStack(Item.getItemFromBlock(Blocks.SNOW)),
        new ItemStack(Items.SNOWBALL),
        new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER), 1, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta())
    );

    acceptModules(ModuleRegistry.module_touch);
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules, int ticks) {
    World world = player.world;
    if (modules.contains(ModuleRegistry.module_touch)) {
      if (!world.isRemote) {
        player.addPotionEffect(new PotionEffect(ModPotions.freeze, touchDuration, 0, false, false));
        world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.3f, 2f);
      }
      return true;
    } else {
      RayTraceResult result = this.rayTrace(player, player.isSneaking() ? 1 : 10);
      if (result != null && (!player.isSneaking() && result.typeOfHit == RayTraceResult.Type.BLOCK)) {
        BlockPos pos = result.getBlockPos().offset(result.sideHit);
        IBlockState state = world.getBlockState(pos);
        boolean didSpell = false;
        BlockPos posNoOffset = result.getBlockPos();
        IBlockState stateNoOffset = world.getBlockState(posNoOffset);

        if (!world.isRemote) {
          if (state.getBlock() == Blocks.FIRE) {
            didSpell = true;
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.3f, 1);
          } else if (state.getBlock() == Blocks.LAVA) {
            if (state.getValue(BlockLiquid.LEVEL) == 0) {
              didSpell = true;
              world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
              world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.3f, 1);
            } else {
              didSpell = true;
              world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
              world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 0.3f, 1);
            }
          } else if (state.getBlock() == Blocks.WATER) {
            if (state.getValue(BlockLiquid.LEVEL) == 0) {
              didSpell = true;
              world.setBlockState(pos, Blocks.ICE.getDefaultState());
              world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.3f, 1);
            }
          }else if (stateNoOffset.getBlock() == Blocks.SNOW_LAYER) {
            didSpell = true;
            world.setBlockState(posNoOffset, Blocks.SNOW.getDefaultState());
            world.playSound(null, posNoOffset, SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.PLAYERS, 0.5F, 1);
          }
          else if (stateNoOffset.getBlock() == Blocks.SNOW) {
            didSpell = true;
            world.setBlockState(posNoOffset, Blocks.ICE.getDefaultState());
            world.playSound(null, posNoOffset, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.PLAYERS, 0.5F, 1);
          }
          else if (stateNoOffset.getBlock() == Blocks.ICE) {
            didSpell = true;
            world.setBlockState(posNoOffset, Blocks.PACKED_ICE.getDefaultState());
            world.playSound(null, posNoOffset, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.PLAYERS, 0.5F, 1);
          }
          else if (world.isAirBlock(pos)) {
            IBlockState down = world.getBlockState(pos.down());
            if (down.getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID) {
              didSpell = true;
              world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
              world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.PLAYERS, 0.3f, 1);
            }
          }
          if (didSpell) {
            PacketHandler.sendToAllTracking(new MessageIcedTouchFX(pos.getX(), pos.getY(), pos.getZ()), player);
          }
        }
        return didSpell;
      }
    }
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.touchDuration = properties.get(PROP_TOUCH_DURATION);
  }

  @Nullable
  public RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance) {
    Vec3d vec3d = player.getPositionEyes(1.0F);
    Vec3d vec3d1 = player.getLook(1.0F);
    Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
    return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
  }
}

