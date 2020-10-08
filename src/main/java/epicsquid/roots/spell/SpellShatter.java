package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.*;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.network.fx.MessageShatterBurstFX;
import epicsquid.roots.properties.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreIngredient;

public class SpellShatter extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("stalicripe", 0.0625));
  public static Property.PropertyDamage PROP_DAMAGE = new Property.PropertyDamage(5.0f);

  public static Modifier WIDER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "wider"), ModifierCores.PERESKIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.PERESKIA, 1)));
  public static Modifier HOE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "farmers_ray"), ModifierCores.WILDEWHEET, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDEWHEET, 1)));
  public static Modifier MAGNETISM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "magnetic_ray"), ModifierCores.WILDROOT, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.WILDROOT, 1)));
  public static Modifier DEEPER = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "deeper"), ModifierCores.MOONGLOW_LEAF, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.MOONGLOW_LEAF, 1)));
  public static Modifier SINGLE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "singularity"), ModifierCores.SPIRIT_HERB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.SPIRIT_HERB, 1)));
  public static Modifier KNIFE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "moss_harvest"), ModifierCores.TERRA_MOSS, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.TERRA_MOSS, 1)));
  public static Modifier MUSHROOM = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "mushroom_consumer"), ModifierCores.BAFFLE_CAP, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.BAFFLE_CAP, 1)));
  public static Modifier TREE = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "leaf_consumer"), ModifierCores.CLOUD_BERRY, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.CLOUD_BERRY, 1)));
  public static Modifier SMELTING = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "smelting_ray"), ModifierCores.INFERNAL_BULB, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.INFERNAL_BULB, 1)));
  public static Modifier SILK_TOUCH = ModifierRegistry.register(new Modifier(new ResourceLocation(Roots.MODID, "shatter_silk_touch"), ModifierCores.DEWGONIA, ModifierCost.of(CostType.ADDITIONAL_COST, ModifierCores.DEWGONIA, 1)));

  private float damage;

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_shatter");
  public static SpellShatter instance = new SpellShatter(spellName);

  public SpellShatter(ResourceLocation name) {
    super(name, TextFormatting.GRAY, 96f / 255f, 96f / 255f, 96f / 255f, 192f / 255f, 192f / 255f, 192f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1, PROP_DAMAGE);
    acceptsModifiers(WIDER, HOE, MAGNETISM, DEEPER, SINGLE, KNIFE, MUSHROOM, TREE, SMELTING, SILK_TOUCH);
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
  }

  @Override
  public boolean cast(EntityPlayer player, StaffModifierInstanceList info, int ticks) {
    if (!player.world.isRemote) {
      RayTraceResult result = player.world.rayTraceBlocks(player.getPositionVector().add(0, player.getEyeHeight(), 0),
          player.getLookVec().scale(8.0f).add(player.getPositionVector().add(0, player.getEyeHeight(), 0)));
      if (result != null) {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
          BlockPos pos = result.getBlockPos();
          IBlockState state = player.world.getBlockState(pos);
          boolean doParticles = false;
          if (state.getBlockHardness(player.world, pos) > 0) {
            player.world.destroyBlock(pos, true);
            //player.world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8); // Should already be air
            doParticles = true;
          }
          if (result.sideHit.getAxis() != EnumFacing.Axis.Y)
            pos = result.getBlockPos().down();
          else {
            pos = pos.offset(player.getHorizontalFacing().getOpposite());
          }
          state = player.world.getBlockState(pos);
          if (state.getBlockHardness(player.world, pos) > 0) {
            player.world.destroyBlock(pos, true);
            player.world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
          }
          if (doParticles) {
            float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
            float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
            PacketHandler.sendToAllTracking(new MessageShatterBurstFX(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, result.hitVec.x, result.hitVec.y, result.hitVec.z), player);
          }
        } else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
          if (result.entityHit instanceof EntityLivingBase) {
            boolean doDamage = true;
            if (result.entityHit instanceof EntityPlayer) {
              MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
              if (!server.isPVPEnabled()) {
                doDamage = false;
              }
            }
            if (doDamage) {
              result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(player), damage);
              ((EntityLivingBase) result.entityHit).setLastAttackedEntity(player);
              ((EntityLivingBase) result.entityHit).setRevengeTarget(player);
              float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
              float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
              PacketHandler.sendToAllTracking(new MessageShatterBurstFX(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, result.hitVec.x, result.hitVec.y, result.hitVec.z), player);
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public void doFinalise() {
    this.castType = properties.get(PROP_CAST_TYPE);
    this.cooldown = properties.get(PROP_COOLDOWN);
    damage = properties.get(PROP_DAMAGE);
  }
}
