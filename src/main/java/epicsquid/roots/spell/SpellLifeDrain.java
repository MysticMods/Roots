package epicsquid.roots.spell;

import java.util.List;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageLifeDrainAbsorbFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpellLifeDrain extends SpellBase {
  public static String spellName = "spell_life_drain";
  public static SpellLifeDrain instance = new SpellLifeDrain(spellName);

  public SpellLifeDrain(String name) {
    super(name, TextFormatting.DARK_GRAY, 144f / 255f, 32f / 255f, 64f / 255f, 255f / 255f, 196f / 255f, 240f / 255f);
    this.castType = SpellBase.EnumCastType.CONTINUOUS;
    this.cooldown = 28;

    addCost(HerbRegistry.getHerbByName("moonglow_leaf"), 0.25f);
    addCost(HerbRegistry.getHerbByName("baffle_cap"), 0.125f);
    addIngredients(
        new ItemStack(Items.BEETROOT),
        new ItemStack(Item.getItemFromBlock(ModBlocks.baffle_cap_mushroom)),
        new ItemStack(Items.DYE, 1, 15),
        new ItemStack(Items.ROTTEN_FLESH),
        new ItemStack(ModItems.moonglow_leaf)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    if (!player.world.isRemote) {
      boolean foundTarget = false;
      PacketHandler.sendToAllTracking(new MessageLifeDrainAbsorbFX(player.getUniqueID(), player.posX, player.posY + player.getEyeHeight(), player.posZ), player);
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
    return true;
  }

}
