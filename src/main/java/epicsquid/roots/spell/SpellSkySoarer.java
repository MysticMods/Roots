package epicsquid.roots.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.entity.spell.EntityBoost;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.init.ModSounds;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.properties.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public class SpellSkySoarer extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(39);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(new SpellCost("cloud_berry", 0.250));
  public static Property<Integer> PROP_SLOW_FALL_DURATION = new Property<>("slow_fall_duration", 20 * 20).setDescription("the duration of the slow fall effect that should be applied after the boost effect ends");
  public static Property<Integer> PROP_JAUNT_DISTANCE = new Property<>("jaunt_distance", 5).setDescription("the number of blocks forward to jaunt");
  public static Property<Integer> PROP_REGEN_DURATION = new Property<>("regen_duration", 20 * 20).setDescription("how long regeneration should be applied for");
  public static Property<Integer> PROP_REGEN_AMPLIFIER = new Property<>("regen_amplifier", 0).setDescription("the amplifier to use for the regeneration effect");
  public static Property<Float> PROP_AMPLIFIER = new Property<>("amplifier", 0.8f).setDescription("the amplifier to the default motion");
  public static Property<Float> PROP_EXTENDED_AMPLIFIER = new Property<>("extended_amplifier", 0.6f).setDescription("how much should be added to the default amplifier when the faster modifier is enabled");
  public static Property<Integer> PROP_FALL_DURATION = new Property<>("fall_duration", 20 * 15).setDescription("the duration for which fall damage should be suppressed after a boost ends");
  public static Property<Integer> PROP_LIFETIME = new Property<>("lifetime", 28).setDescription("how long the boost entity should exist for (in ticks)");
  public static Property<Integer> PROP_EXTENDED_LIFETIME = new Property<>("extended_lifetime", 28).setDescription("a value that should be added to the lifetime when the eternal flame modifier is used (in ticks)");

  public static Modifier SLOW_FALL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "slow_fall"), ModifierCores.PERESKIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 0.275)));
  public static Modifier NO_COLLIDE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "arboreal_bypass"), ModifierCores.WILDEWHEET, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 0.125)));
  public static Modifier FASTER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "momentum"), ModifierCores.WILDROOT, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 0.345)));
  public static Modifier VERTICAL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "verticality"), ModifierCores.MOONGLOW_LEAF, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 0.345)));
  public static Modifier JAUNT = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "jaunt"), ModifierCores.SPIRIT_HERB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 0.345)));
  public static Modifier REGENERATION = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "regenerative_sky"), ModifierCores.TERRA_MOSS, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 0.275)));
  public static Modifier CHEM_TRAILS = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "chem_trails"), ModifierCores.BAFFLE_CAP, Cost.of(Cost.cost(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 0.05), Cost.cost(CostType.ALL_COST_MULTIPLIER, -0.15))));
  public static Modifier ETERNAL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "eternal_flame"), ModifierCores.INFERNAL_BULB, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 0.45)));
  public static Modifier NO_FALL_DAMAGE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "friendly_earth"), ModifierCores.STALICRIPE, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.STALICRIPE, 0.275)));
  public static Modifier HORIZONTAL = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "aquaplane"), ModifierCores.DEWGONIA, Cost.single(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 0.345)));

  static {
    JAUNT.addConflicts(NO_FALL_DAMAGE, CHEM_TRAILS, ETERNAL, VERTICAL, FASTER, NO_COLLIDE, SLOW_FALL);
    VERTICAL.addConflict(HORIZONTAL);
  }

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_sky_soarer");
  public static SpellSkySoarer instance = new SpellSkySoarer(spellName);

  public float amplifier, extended_amplifier;
  public int slow_duration, jaunt_distance, regen_duration, regen_amplifier, fall_duration, lifetime, extended_liftime;

  public SpellSkySoarer(ResourceLocation name) {
    super(name, TextFormatting.BLUE, 32f / 255f, 200f / 255f, 255f / 255f, 32f / 255f, 64f / 255f, 255f / 255f);
    properties.add(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_SLOW_FALL_DURATION, PROP_JAUNT_DISTANCE, PROP_REGEN_AMPLIFIER, PROP_REGEN_DURATION, PROP_AMPLIFIER, PROP_FALL_DURATION, PROP_LIFETIME, PROP_EXTENDED_LIFETIME, PROP_EXTENDED_AMPLIFIER);
    acceptModifiers(SLOW_FALL, NO_COLLIDE, FASTER, VERTICAL, JAUNT, REGENERATION, CHEM_TRAILS, ETERNAL, NO_FALL_DAMAGE, HORIZONTAL);
  }

  @Override
  public void init() {
    addIngredients(
        new ItemStack(Item.getItemFromBlock(Blocks.LADDER)),
        new ItemStack(ModItems.petals),
        new ItemStack(Items.BOW),
        new ItemStack(epicsquid.mysticalworld.init.ModItems.aubergine_seed),
        new ItemStack(ModItems.cloud_berry)
    );
    setCastSound(ModSounds.Spells.SKY_SOARER);
  }

  private boolean playerSafe(EntityPlayer player, BlockPos.MutableBlockPos position, EnumFacing direction) {
    int safe_count = 0;
    int start_y = position.getY();

    position.setY(start_y - (direction == EnumFacing.DOWN ? 2 : 1));
    IBlockState state = player.world.getBlockState(position);
    if (!state.isSideSolid(player.world, position, EnumFacing.UP)) {
      return false;
    }

    position.setY(start_y);
    state = player.world.getBlockState(position);
    if (state.getBlock().isPassable(player.world, position)) {
      safe_count++;
    }

    position.move(direction, 1);
    state = player.world.getBlockState(position);
    if (state.getBlock().isPassable(player.world, position)) {
      safe_count++;
    }

    return safe_count == 2;
  }

  @Nullable
  private Vec3d findSafePosition(EntityPlayer player) {
    Vec3d realPos = new Vec3d(player.posX, player.posY, player.posZ).add(Vec3d.fromPitchYaw(0, player.rotationYaw).scale(jaunt_distance));
    BlockPos real = new BlockPos(realPos);
    BlockPos pos = player.world.getHeight(real);
    int height = player.world.provider.isNether() ? 128 : 256;
    if (height == 128) {
      BlockPos.MutableBlockPos dest = new BlockPos.MutableBlockPos(real);
      int safe_y = -1;
      for (int i = height - 1; i > real.getY(); i--) {
        dest.setY(i);
        if (playerSafe(player, dest, EnumFacing.UP)) {
          safe_y = i;
          break;
        }
      }

      if (safe_y == -1) {
        for (int i = 0; i < real.getY(); i++) {
          dest.setY(i);
          if (playerSafe(player, dest, EnumFacing.DOWN)) {
            safe_y = i;
            break;
          }
        }
      }

      if (safe_y == -1 || safe_y >= height) {
        return null;
      }

      return new Vec3d(realPos.x, safe_y + 0.01, realPos.z);
    } else {
      IBlockState state = player.world.getBlockState(pos);
      IBlockState state2 = player.world.getBlockState(pos.up());
      IBlockState state3 = player.world.getBlockState(pos.down());
      if (state.getBlock().isPassable(player.world, pos) && state2.getBlock().isPassable(player.world, pos.up()) && state3.isSideSolid(player.world, pos.down(), EnumFacing.UP)) {
        return new Vec3d(realPos.x, pos.getY() + 0.01, realPos.z);
      }

      return null;
    }
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    if (!player.world.isRemote) {
      if (info.has(JAUNT)) {
        Vec3d dest = findSafePosition(player);
        if (dest == null) {
          return false;
        }

        if (dest.y >= 128) {
        }

        if (!player.world.isRemote) {
          player.setPositionAndUpdate(dest.x, dest.y, dest.z);
          player.fallDistance = 0f;
        }
      } else {
        int life = lifetime;
        if (info.has(ETERNAL)) {
          life += extended_liftime;
        }
        life = life;
        EntityBoost boost = new EntityBoost(player.world, life);
        boost.setModifiers(info);
        boost.setPlayer(player.getUniqueID());
        boost.setPosition(player.posX, player.posY, player.posZ);
        player.world.spawnEntity(boost);
      }
      if (info.has(REGENERATION)) {
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, regen_duration, regen_amplifier, false, false));
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
    this.regen_amplifier = properties.get(PROP_REGEN_AMPLIFIER);
    this.regen_duration = properties.get(PROP_REGEN_DURATION);
    this.amplifier = properties.get(PROP_AMPLIFIER);
    this.fall_duration = properties.get(PROP_FALL_DURATION);
    this.lifetime = properties.get(PROP_LIFETIME);
    this.extended_liftime = properties.get(PROP_EXTENDED_LIFETIME);
    this.extended_amplifier = properties.get(PROP_EXTENDED_AMPLIFIER);
  }
}
