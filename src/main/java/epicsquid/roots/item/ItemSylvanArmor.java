package epicsquid.roots.item;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import java.util.List;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 14/02/2019 / 16:43
 * Class: ItemSylvanArmor
 * Project: Mystic Mods
 * Copyright - © - Davoleo - 2019
 **************************************************/

public class ItemSylvanArmor extends ItemArmor implements IModeledObject {

    private List<Herb> herbs;

    public ItemSylvanArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name)
    {
        super(material, 0, slot);
        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(Roots.MODID, name));
        setMaxDamage(500);
        setCreativeTab(Roots.tab);
    }

    @Override
    public void initModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        //TODO set a compound to hold information about linked herbs
        //stack.setTagCompound();
    }

    //Probably useless
    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items)
    {
        super.getSubItems(tab, items);

//        if (this.isInCreativeTab(tab))
//        {
//            for (Herb herb : HerbRegistry.REGISTRY)
//                items.add(ItemStack.EMPTY);
//        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        //TODO random consume of the linked herb stored in the inventory or in some pouch
        //itemStack.getTagCompound();

    }
}
