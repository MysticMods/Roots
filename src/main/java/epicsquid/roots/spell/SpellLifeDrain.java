package epicsquid.roots.spell;

import java.util.List;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.network.fx.MessageLifeDrainAbsorbFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpellLifeDrain extends SpellBase {

  public SpellLifeDrain(String name) {
    super(name, TextFormatting.DARK_GRAY, 144f / 255f, 32f / 255f, 64f / 255f, 255f / 255f, 196f / 255f, 240f / 255f);
    this.castType = SpellBase.EnumCastType.CONTINUOUS;
    this.cooldown = 28;

    addCost(ModItems.pereskia_bulb, 0.125f);
    addIngredients(
        new ItemStack(Items.BEETROOT, 1),
        new ItemStack(Blocks.RED_FLOWER, 1, 7),
        new ItemStack(Items.BEETROOT_SEEDS, 1),
        new ItemStack(Items.ROTTEN_FLESH, 1),
        new ItemStack(ModItems.pereskia_bulb, 1)
    );
  }

  @Override
  public void cast(EntityPlayer player) {
    if (!player.world.isRemote) {
      boolean foundTarget = false;
      PacketHandler.INSTANCE.sendToAll(new MessageLifeDrainAbsorbFX(player.getUniqueID(), player.posX, player.posY + player.getEyeHeight(), player.posZ));
      for (int i = 0; i < 4 && !foundTarget; i++) {
        double x = player.posX + player.getLookVec().x * 3.0 * (float) i;
        double y = player.posY + player.getEyeHeight() + player.getLookVec().y * 3.0 * (float) i;
        double z = player.posZ + player.getLookVec().z * 3.0 * (float) i;
        List<EntityLivingBase> entities = player.world
            .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - 2.0, y - 2.0, z - 2.0, x + 2.0, y + 2.0, z + 2.0));
        for (EntityLivingBase e : entities) {
          if (!(e instanceof EntityPlayer && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())
              && e.getUniqueID().compareTo(player.getUniqueID()) != 0) {
            foundTarget = true;
            if (e.hurtTime <= 0 && !e.isDead) {
              e.attackEntityFrom(DamageSource.WITHER.causeMobDamage(player), 1.0f);
              e.setRevengeTarget(player);
              e.setLastAttackedEntity(player);
              player.heal(0.5f);
            }
          }
        }
      }
    }
  }

}
