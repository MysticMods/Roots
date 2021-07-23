package epicsquid.roots.spell;

import com.google.common.collect.Sets;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockColoredFeyLight;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class SpellFeyLight extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("cloud_berry", 0.125));
  public static Property<Integer> PROP_RADIUS_X = new Property<>("radius_x", 15).setDescription("radius on the X axis within which lights are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Y = new Property<>("radius_y", 10).setDescription("radius on the Y axis within which lights are affected by the spell");
  public static Property<Integer> PROP_RADIUS_Z = new Property<>("radius_z", 15).setDescription("radius on the Z axis within which lights are affected by the spell");

  public static Modifier PINK = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "pink_light"), ModifierCores.PERESKIA, Cost.of(Cost.cost(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.125), Cost.cost(CostType.ALL_COST_MULTIPLIER, -0.15))));
  public static Modifier YELLOW = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "yellow_light"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier FIXED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fixed_light"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.125)));
  public static Modifier PURPLE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "purple_light"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.125)));
  public static Modifier CONSUME = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "deluminator"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.475)));
  public static Modifier GREEN = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "green_light"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.125)));
  public static Modifier DECAY = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "decaying_light"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.275)));
  public static Modifier RED = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "red_light"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.125)));
  public static Modifier BROWN = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "brown_light"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.125)));
  public static Modifier BLUE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "blue_light"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.125)));

  static {
    // Conflicts
    // Deluminator <-> Everything
    CONSUME.addConflicts(DECAY, PINK, YELLOW, FIXED, PURPLE, GREEN, RED, BROWN, BLUE);
    DECAY.addConflicts(PINK, YELLOW, PURPLE, GREEN, RED, BROWN, BLUE);
    PINK.addConflicts(YELLOW, PURPLE, GREEN, RED, BROWN, BLUE);
    YELLOW.addConflicts(PURPLE, GREEN, RED, BROWN, BLUE);
    PURPLE.addConflicts(GREEN, RED, BROWN, BLUE);
    GREEN.addConflicts(RED, BROWN, BLUE);
    RED.addConflicts(BROWN, BLUE);
    BROWN.addConflicts(BLUE);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_fey_light");
  public static SpellFeyLight instance = new SpellFeyLight(spellName);

  private int radius_x, radius_y, radius_z;
  private AxisAlignedBB box;

  public SpellFeyLight(ResourceLocation name) {
    super(name, TextFormatting.LIGHT_PURPLE, 247f / 255f, 246 / 255f, 210f / 255f, 227f / 255f, 81f / 255f, 244f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1);
    acceptsModifiers(PINK, YELLOW, FIXED, PURPLE, CONSUME, GREEN, DECAY, RED, BROWN, BLUE);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(Blocks.LIT_PUMPKIN)),
        new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta()),
        new ItemStack(ModItems.cloud_berry),
        new OreIngredient("rootsBark"),
        new ItemStack(ModItems.cloud_berry)
    );
    setCastSound(ModSounds.Spells.FEY_LIGHT);
  }

  public int nextTick() {
    return 75 + Util.rand.nextInt(75);
  }

  private static Set<Block> lightSet = null;

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    World world = player.world;
    if (info.has(CONSUME)) {
      if (lightSet == null) {
        lightSet = Sets.newHashSet(ModBlocks.fey_colored_light, ModBlocks.fey_decaying_light, ModBlocks.fey_light);
      }
      List<BlockPos> positions = Util.getBlocksWithinRadius(world, player.getPosition(), radius_x, radius_y, radius_z, lightSet);
      if (positions.isEmpty()) {
        return false;
      }

      if (!world.isRemote) {
        for (BlockPos pos : positions) {
          world.setBlockToAir(pos);
        }
      }
      return true;

    } else {
      BlockPos pos = BlockPos.ORIGIN;
      if (info.has(FIXED)) {
        Vec3d lookVec = player.getLookVec().scale(1.5);
        pos = new BlockPos(player.posX + lookVec.x, player.posY + 1, player.posZ + lookVec.z);
      } else {
        RayTraceResult result = this.rayTrace(player, 10);
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
          pos = result.getBlockPos().offset(result.sideHit);
          if (!world.isAirBlock(pos)) {
            result = this.rayTrace(player, 10, false);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
              pos = result.getBlockPos().offset(result.sideHit);
            }
          }
        }
      }
      if (world.isAirBlock(pos)) {
        if (!world.isRemote) {
          IBlockState state = ModBlocks.fey_light.getDefaultState();
          if (info.has(PINK)) {
            state = ModBlocks.fey_colored_light.getDefaultState().withProperty(BlockColoredFeyLight.COLOR, 0);
          }
          if (info.has(YELLOW)) {
            state = ModBlocks.fey_colored_light.getDefaultState().withProperty(BlockColoredFeyLight.COLOR, 1);
          }
          if (info.has(PURPLE)) {
            state = ModBlocks.fey_colored_light.getDefaultState().withProperty(BlockColoredFeyLight.COLOR, 2);
          }
          if (info.has(GREEN)) {
            state = ModBlocks.fey_colored_light.getDefaultState().withProperty(BlockColoredFeyLight.COLOR, 3);
          }
          if (info.has(RED)) {
            state = ModBlocks.fey_colored_light.getDefaultState().withProperty(BlockColoredFeyLight.COLOR, 4);
          }
          if (info.has(BROWN)) {
            state = ModBlocks.fey_colored_light.getDefaultState().withProperty(BlockColoredFeyLight.COLOR, 5);
          }
          if (info.has(BLUE)) {
            state = ModBlocks.fey_colored_light.getDefaultState().withProperty(BlockColoredFeyLight.COLOR, 6);
          }
          if (info.has(DECAY)) {
            state = ModBlocks.fey_decaying_light.getDefaultState();
          }
          world.setBlockState(pos, state);
          if (info.has(DECAY)) {
            world.scheduleUpdate(pos, state.getBlock(), nextTick());
          }
          // TODO: FIX SOUND EFFECTS
          world.playSound(null, pos, SoundEvents.BLOCK_CLOTH_PLACE, SoundCategory.PLAYERS, 0.25f, 1);
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.radius_x = properties.get(PROP_RADIUS_X);
    this.radius_y = properties.get(PROP_RADIUS_Y);
    this.radius_z = properties.get(PROP_RADIUS_Z);
    this.box = new AxisAlignedBB(-radius_x, -radius_y, -radius_z, (radius_x + 1), (radius_y + 1), (radius_z + 1));
  }

  @Nullable
  public RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance) {
    return rayTrace(player, blockReachDistance, true);
  }

  @Nullable
  public RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance, boolean ignore) {
    Vec3d vec3d = player.getPositionEyes(1.0F);
    Vec3d vec3d1 = player.getLook(1.0F);
    Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
    return player.world.rayTraceBlocks(vec3d, vec3d2, false, ignore, true);
  }
}

