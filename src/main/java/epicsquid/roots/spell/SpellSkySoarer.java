package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityBoost;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import epicsquid.roots.recipe.ingredient.ArrowBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

public class SpellSkySoarer extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(39);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("cloud_berry", 0.15));
  public static Property<Integer> PROP_SLOW_FALL_DURATION = new Property<>("slow_fall_duration", 20 * 4).setDescription("the duration of the slow fall effect that should be applied after the boost effect ends");
  public static Property<Integer> PROP_JAUNT_DISTANCE = new Property<>("jaunt_distance", 5).setDescription("the number of blocks forward to jaunt");

  public static Modifier SLOW_FALL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slow_fall"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier NO_COLLIDE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "arboreal_bypass"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier FASTER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "momentum"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier VERTICAL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "verticality"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier KNOCKBACK = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "knockback"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier REGENERATION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "regenerative_sky"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier CHEM_TRAILS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "chem_trails"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier JAUNT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "jaunt"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier NO_FALL_DAMAGE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "friendly_earth"), ModifierCores.STALICRIPE, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 1)));
  public static Modifier UNDERWATER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "surface_seeker"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  static {
    JAUNT.addConflicts(NO_FALL_DAMAGE, CHEM_TRAILS, KNOCKBACK, VERTICAL, FASTER, NO_COLLIDE, SLOW_FALL);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_sky_soarer");
  public static SpellSkySoarer instance = new SpellSkySoarer(spellName);

  public int slow_duration, jaunt_distance;

  public SpellSkySoarer(ResourceLocation name) {
    super(name, TextFormatting.BLUE, 32f / 255f, 200f / 255f, 255f / 255f, 32f / 255f, 64f / 255f, 255f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_SLOW_FALL_DURATION, PROP_JAUNT_DISTANCE);
    acceptsModifiers(SLOW_FALL, NO_COLLIDE, FASTER, VERTICAL, KNOCKBACK, REGENERATION, CHEM_TRAILS, JAUNT, NO_FALL_DAMAGE, UNDERWATER);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(Blocks.LADDER)),
        new ItemStack(ModItems.petals),
        ArrowBuilder.get(),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),
        new ItemStack(ModItems.cloud_berry)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    if (!player.world.isRemote) {
      if (info.has(JAUNT)) {
        Vec3d realPos = new Vec3d(player.posX, player.posY, player.posZ).add(Vec3d.fromPitchYaw(0, player.rotationYaw).scale(jaunt_distance));
        BlockPos pos = player.world.getHeight(new BlockPos(realPos.x, realPos.y, realPos.z));
        IBlockState state = player.world.getBlockState(pos);
        if (state.getBlock().isPassable(player.world, pos)) {
          //acted = true;
          if (!player.world.isRemote) {
            player.setPositionAndUpdate(realPos.x, pos.getY() + 0.01, realPos.z);
            player.fallDistance = 0f;
          }
        }
      } else {
        EntityBoost boost = new EntityBoost(player.world);
        boost.setPlayer(player.getUniqueID());
        boost.setPosition(player.posX, player.posY, player.posZ);
        player.world.spawnEntity(boost);
        player.getEntityData().setIntArray(getCachedName(), info.snapshot());
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    this.slow_duration = properties.get(PROP_SLOW_FALL_DURATION);
    this.jaunt_distance = properties.get(PROP_JAUNT_DISTANCE);
  }
}
