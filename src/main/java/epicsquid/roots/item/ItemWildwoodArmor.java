package epicsquid.roots.item;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import epicsquid.roots.model.ModelWildwoodArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemWildwoodArmor extends ItemArmor implements IModeledObject, ILivingRepair {
  public ItemWildwoodArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name) {
    super(material, 0, slot);
    setTranslationKey(name);
    setRegistryName(new ResourceLocation(Roots.MODID, name));
    setMaxDamage(750);
    setCreativeTab(Roots.tab);
  }

  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "handler"));
  }

  @Nullable
  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
    return Roots.MODID + ":textures/models/armor/wildwood_armor.png";
  }

  @Nullable
  @Override
  @SideOnly(Side.CLIENT)
  public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
    return ModelWildwoodArmor.getInstance(armorSlot);
  }

  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    switch (piecesWorn(player)) {
      case 1:
        if (itemRand.nextInt(80) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 2:
        if (itemRand.nextInt(60) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 3:
        if (itemRand.nextInt(40) == 0 && player.shouldHeal())
          player.heal(1);
        break;
      case 4:
        if (itemRand.nextInt(30) == 0 && player.shouldHeal())
          player.heal(2);
        break;
      default:
    }
  }

  public static int piecesWorn(EntityPlayer player) {
    int count = 0;
    for (ItemStack stack : player.getArmorInventoryList()) {
      if (stack.getItem() instanceof ItemWildwoodArmor)
        count++;
    }
    return count;
  }
}
