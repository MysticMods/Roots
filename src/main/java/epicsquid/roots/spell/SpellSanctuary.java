package epicsquid.roots.spell;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.network.fx.MessageSanctuaryBurstFX;
import epicsquid.roots.network.fx.MessageSanctuaryRingFX;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;

public class SpellSanctuary extends SpellBase {
  public static String spellName = "spell_sanctuary";
  public static SpellSanctuary instance = new SpellSanctuary(spellName);

  public SpellSanctuary(String name) {
    super(name, TextFormatting.DARK_PURPLE, 208f / 255f, 16f / 255f, 80f / 255f, 224f / 255f, 32f / 255f, 144f / 255f);
    this.castType = SpellBase.EnumCastType.CONTINUOUS;
    this.cooldown = 0;

    addCost(HerbRegistry.getHerbByName("pereskia"), 0.125f);
    addCost(HerbRegistry.getHerbByName("wildroot"), 0.125f);
    addIngredients(
        new ItemStack(Items.DYE, 1, 1),
        new ItemStack(ModItems.pereskia),
        new OreIngredient("vine"),
        new ItemStack(ModItems.pereskia),
        new ItemStack(ModItems.wildroot)
    );
  }

  @Override
  public boolean cast(EntityPlayer player, List<SpellModule> modules) {
    List<Entity> entities = player.world.getEntitiesWithinAABB(Entity.class,
        new AxisAlignedBB(player.posX - 4.0, player.posY - 4.0, player.posZ - 4.0, player.posX + 4.0, player.posY + 5.0, player.posZ + 4.0));
    if (entities.size() > 0) {
      for (Entity e : entities) {
        if (e.getUniqueID().compareTo(player.getUniqueID()) != 0) {
          if (Math.pow((e.posX - player.posX), 2) + Math.pow((e.posY - player.posY), 2) + Math.pow((e.posZ - player.posZ), 2) < 9.0f) {
            e.motionX = 0.125f * (e.posX - player.posX);
            e.motionY = 0.125f * (e.posY - player.posY);
            e.motionZ = 0.125f * (e.posZ - player.posZ);
            e.velocityChanged = true;
            if (!e.isInvisible()) {
              PacketHandler.sendToAllTracking(new MessageSanctuaryBurstFX(e.posX, e.posY + 0.6f * e.getEyeHeight(), e.posZ), e);
            }
          }
        }
      }
    }
    if (player.ticksExisted % 2 == 0) {
      PacketHandler.sendToAllTracking(new MessageSanctuaryRingFX(player.posX, player.posY + 0.875f, player.posZ), player);
    }
    return true;
  }

}
