package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.instance.ModifierInstanceList;
import epicsquid.roots.network.fx.MessageShatterBurstFX;
import epicsquid.roots.util.types.Property;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

public class SpellShatter extends SpellBase {
  public static Property.PropertyCooldown PROP_COOLDOWN = new Property.PropertyCooldown(20);
  public static Property.PropertyCastType PROP_CAST_TYPE = new Property.PropertyCastType(EnumCastType.INSTANTANEOUS);
  public static Property.PropertyCost PROP_COST_1 = new Property.PropertyCost(0, new SpellCost("stalicripe", 0.0625));

  public static ResourceLocation spellName = new ResourceLocation(Roots.MODID, "spell_shatter");
  public static SpellShatter instance = new SpellShatter(spellName);

  public SpellShatter(ResourceLocation name) {
    super(name, TextFormatting.GRAY, 96f / 255f, 96f / 255f, 96f / 255f, 192f / 255f, 192f / 255f, 192f / 255f);
    properties.addProperties(PROP_COOLDOWN, PROP_CAST_TYPE, PROP_COST_1);
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
  public boolean cast(EntityPlayer player, ModifierInstanceList modifiers, int ticks) {
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
            player.world.notifyBlockUpdate(pos, state, Blocks.AIR.getDefaultState(), 8);
            doParticles = true;
          }
//          for (int i = 0; i < 4; i++) {
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
//          }
          if (doParticles) {
            float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
            float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
            PacketHandler.sendToAllTracking(new MessageShatterBurstFX(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, result.hitVec.x, result.hitVec.y, result.hitVec.z), player);
          }
        } else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
          if (result.entityHit instanceof EntityLivingBase) {
            result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(player), 5.0f);
            ((EntityLivingBase) result.entityHit).setLastAttackedEntity(player);
            ((EntityLivingBase) result.entityHit).setRevengeTarget(player);
            float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - player.rotationYaw));
            float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - player.rotationYaw));
            PacketHandler.sendToAllTracking(new MessageShatterBurstFX(player.posX + offX, player.posY + player.getEyeHeight(), player.posZ + offZ, result.hitVec.x, result.hitVec.y, result.hitVec.z), player);
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
  }
}
