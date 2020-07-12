package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import vazkii.botania.common.item.equipment.bauble.CloudPendantShim;

import javax.annotation.Nullable;

public class SpellFeyLight extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("cloud_berry", 0.125));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_fey_light");
  public static SpellFeyLight instance = new SpellFeyLight(spellName);

  public static Modifier PERESKIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "pink_light"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier WILDEWHEET = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "yellow_light"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier WILDROOT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "fixed_light"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier MOONGLOW_LEAF = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "purple_light"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SPIRIT_HERB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "deluminator"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier TERRA_MOSS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "green_light"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier BAFFLE_CAP = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "decaying_light"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier INFERNAL_BULB = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "red_light"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier STALICRIPE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "brown_light"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier DEWGONIA = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "blue_light"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    // Conflicts
    // Deluminator <-> Everything
    SPIRIT_HERB.addConflicts(PERESKIA, WILDEWHEET, WILDROOT, MOONGLOW_LEAF, TERRA_MOSS, BAFFLE_CAP, INFERNAL_BULB, STALICRIPE, DEWGONIA);
  }

  public SpellFeyLight(ResourceLocation name) {
    super(name, TextFormatting.LIGHT_PURPLE, 247f / 255f, 246 / 255f, 210f / 255f, 227f / 255f, 81f / 255f, 244f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1);
    acceptsModifiers(PERESKIA, WILDEWHEET, WILDROOT, MOONGLOW_LEAF, SPIRIT_HERB, TERRA_MOSS, BAFFLE_CAP, INFERNAL_BULB, STALICRIPE, DEWGONIA);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(Blocks.LIT_PUMPKIN)),
        new ItemStack(Item.getItemFromBlock(Blocks.DOUBLE_PLANT), 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta()),
        new ItemStack(ModItems.cloud_berry),
        new ItemStack(ModItems.bark_acacia),
        new ItemStack(ModItems.cloud_berry)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList modifiers, int ticks, double amplifier, double speedy) {
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
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
  }

  @Nullable
  public RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance) {
    Vec3d vec3d = player.getPositionEyes(1.0F);
    Vec3d vec3d1 = player.getLook(1.0F);
    Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
    return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
  }
}

