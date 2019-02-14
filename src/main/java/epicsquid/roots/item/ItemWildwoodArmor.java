package epicsquid.roots.item;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 14/02/2019 / 18:05
 * Class: ItemWildwoodArmor
 * Project: Mystic Mods
 * Copyright - © - Davoleo - 2019
 **************************************************/

public class ItemWildwoodArmor extends ItemArmor implements IModeledObject {

    public ItemWildwoodArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name)
    {
        super(material, 0, slot);
        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(Roots.MODID, name));
        setMaxDamage(750);
        setCreativeTab(Roots.tab);
    }

    @Override
    public void initModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        //TODO Might need a nerf / dunno how balanced they want it to be
        if (player.shouldHeal() & itemRand.nextInt(50) == 5)
            player.heal(1);
    }
}
