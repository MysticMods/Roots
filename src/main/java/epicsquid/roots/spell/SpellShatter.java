package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.AABBUtil;
import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.mysticallib.util.RayCastUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageRunicShearsAOEFX;
import epicsquid.roots.network.fx.MessageShatterBurstFX;
import epicsquid.roots.properties.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SpellShatter extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(0);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("stalicripe", 0.250));
  public static Property<Float> PROP_DISTANCE = new Property<>("distance", 8f).setDescription("the maximum range of the beam");
  public static Property<Integer> PROP_DEFAULT_WIDTH = new Property<>("default_width", 0).setDescription("the default width (when not amplified; 0 = 1)");
  public static Property<Integer> PROP_DEFAULT_HEIGHT = new Property<>("default_height", 0).setDescription("the default height (when not amplified: this is 2 blocks tall, with the block below the block targeted also being broken; if this value is greater than 0, than the height will be 1 block above and 1 block below, relatively, th block hit)");
  public static Property<Integer> PROP_DEFAULT_DEPTH = new Property<>("default_depth", 0).setDescription("the default depth (when not amplified; 0 = just one block broken)");
  public static Property<Integer> PROP_WIDTH = new Property<>("width", 1).setDescription("the width when the width modifier is applied; this is appiled to the left and right of the targeted block, meaning a width of 1 is a total width of 3 blocks");
  public static Property<Integer> PROP_HEIGHT = new Property<>("height", 1).setDescription("the height when the height modifier is applied; as per width");
  public static Property<Integer> PROP_DEPTH = new Property<>("depth", 2).setDescription("this value is applied by offsetting the position relative to the angle the beam struck it; if struck from above, it digs down, etc. By default, with a value single 2, this should result in 3 blocks being broken.");

  public static Modifier WIDER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "wider"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.45)));
  public static Modifier TALLER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "taller"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.45)));
  public static Modifier MAGNETISM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_ray"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.125)));
  public static Modifier DEEPER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "deeper"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.45)));
  public static Modifier SINGLE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "singularity"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.125)));
  public static Modifier KNIFE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "moss_harvest"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.275)));
  public static Modifier VOID = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "shatter_void"), ModifierCores.BAFFLE_CAP, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.125)));
  public static Modifier FORTUNE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "shatter_fortune"), ModifierCores.CLOUD_BERRY, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 0.375)));
  public static Modifier SMELTING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "smelting_ray"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.25)));
  public static Modifier SILK_TOUCH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "shatter_silk_touch"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.65)));

  static {
    SILK_TOUCH.addConflict(FORTUNE);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_shatter");
  public static SpellShatter instance = new SpellShatter(spellName);

  public float distance;
  private int default_depth, added_depth, default_height, added_height, default_width, added_width, fortune;

  public SpellShatter(ResourceLocation name) {
    super(name, TextFormatting.GRAY, 96f / 255f, 96f / 255f, 96f / 255f, 192f / 255f, 192f / 255f, 192f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DISTANCE, PROP_DEPTH, PROP_WIDTH, PROP_HEIGHT, PROP_DEFAULT_DEPTH, PROP_DEFAULT_HEIGHT, PROP_DEFAULT_WIDTH);
    acceptModifiers(WIDER, TALLER, MAGNETISM, DEEPER, SINGLE, KNIFE, VOID, FORTUNE, SMELTING, SILK_TOUCH);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Items.FLINT),
        new ItemStack(Items.STONE_PICKAXE),
        new ItemStack(ModItems.stalicripe),
        new ItemStack(Item.getItemFromBlock(Blocks.TNT)),
        new OreIngredient("cobblestone")
    );
    setCastSound(ModSounds.Spells.SHATTER);
  }

  @Nullable
  public static AxisAlignedBB getBox(EntityPlayer player, StaffModifierInstanceList info, RayTraceResult result) {
    if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
      BlockPos pos = result.getBlockPos();
      EnumFacing side = result.sideHit;
      EnumFacing playerFacing = player.getHorizontalFacing();
      EnumFacing width = EnumFacing.fromAngle(playerFacing.getHorizontalAngle() + 90);
      EnumFacing height = side.getAxis() != EnumFacing.Axis.Y ? EnumFacing.DOWN : playerFacing.getOpposite();
      EnumFacing depth = side.getOpposite();

      BlockPos start = pos;
      BlockPos stop = pos;

      if (!info.has(SINGLE)) {
        int h = info.has(TALLER) ? SpellShatter.instance.added_height : SpellShatter.instance.default_height;
        if (info.has(TALLER)) {
          stop = stop.offset(height, h);
          start = start.offset(height.getOpposite(), h);
        } else if (!info.has(WIDER)) {
          stop = stop.offset(height);
        }

        int w = info.has(WIDER) ? SpellShatter.instance.added_width : SpellShatter.instance.default_width;
        if (w != 0) {
          stop = stop.offset(width, w);
          start = start.offset(width.getOpposite(), w);
        }
      }

      int d = info.has(DEEPER) ? SpellShatter.instance.added_depth : SpellShatter.instance.default_depth;
      if (d != 0) {
        stop = stop.offset(depth, d);
      }

      return new AxisAlignedBB(start, stop);
    }
    return null;
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    Vec3d eyes = new Vec3d(0, (double) player.getEyeHeight(), 0);
    RayTraceResult result = RayCastUtil.rayTraceBlocks(player.world, player.getPositionVector().add(eyes), player.getLookVec().scale(distance).add(player.getPositionVector().add(eyes)), false, false, false, false);

    List<Function<ItemStack, ItemStack>> converters = new ArrayList<>();
    if (info.has(SMELTING)) {
      converters.add(SpellHarvest.instance::tryCook);
    }
    AxisAlignedBB box = getBox(player, info, result);
    if (box == null) {
      return false;
    }
    boolean broke = false;
    List<BlockPos> mossPositions = new ArrayList<>();
    boolean noMoss = MossConfig.getBlacklistDimensions().contains(player.world.provider.getDimension());
    for (BlockPos p : AABBUtil.unique(box)) {
      IBlockState state = player.world.getBlockState(p);
      // TODO: Update this as per silk touch
      boolean didMoss = false;
      if (info.has(KNIFE) && !noMoss) {
        IBlockState mossState = MossConfig.scrapeResult(state);
        if (mossState != null) {
          broke = true;
          didMoss = true;
          if (!player.world.isRemote) {
            player.world.setBlockState(p, mossState);
            player.world.scheduleBlockUpdate(p, mossState.getBlock(), 1, mossState.getBlock().tickRate(player.world));
            mossPositions.add(p);
            if (info.has(MAGNETISM)) {
              ItemUtil.spawnItem(player.world, player.getPosition(), new ItemStack(ModItems.terra_moss));
            } else {
              ItemUtil.spawnItem(player.world, p, new ItemStack(ModItems.terra_moss));
            }
          }
        }
      }
      if (!didMoss && ((info.has(SILK_TOUCH) && SpellNaturesScythe.instance.canSilkTouch(player.world, p, state, player) || state.getBlockHardness(player.world, p) > 0))) {
        if (!player.world.isRemote) {
          if (info.has(VOID)) {
            player.world.destroyBlock(p, false);
          } else {
            SpellNaturesScythe.instance.breakBlock(player.world, p, info, player, converters);
          }
        }
        broke = true;
      }
    }

    if (broke) {
      if (!player.world.isRemote) {
        double yaw = Math.toRadians(-90.0 - player.rotationYaw);
        double offX = 0.5 * Math.sin(yaw);
        double offZ = 0.5 * Math.cos(yaw);
        PacketHandler.sendToAllTracking(new MessageShatterBurstFX(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, result.hitVec.x, result.hitVec.y, result.hitVec.z), player);
        if (!mossPositions.isEmpty()) {
          MessageRunicShearsAOEFX message = new MessageRunicShearsAOEFX(mossPositions);
          PacketHandler.sendToAllTracking(message, player.world.provider.getDimension(), mossPositions.get(0));
        }
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.distance = properties.get(PROP_DISTANCE);
    this.default_depth = properties.get(PROP_DEFAULT_DEPTH);
    this.added_depth = properties.get(PROP_DEPTH);
    this.default_height = properties.get(PROP_DEFAULT_HEIGHT);
    this.added_height = properties.get(PROP_HEIGHT);
    this.default_width = properties.get(PROP_DEFAULT_WIDTH);
    this.added_width = properties.get(PROP_WIDTH);
  }
}
