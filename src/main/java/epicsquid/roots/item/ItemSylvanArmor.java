package epicsquid.roots.item;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.mysticalworld.init.ModItems;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.util.PowderInventoryUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemSylvanArmor extends ItemArmor implements IModeledObject {

    private List<String> herbs = new ArrayList<>();
    private int delayTicks = 0;

    public ItemSylvanArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name)
    {
        super(material, 0, slot);
        setUnlocalizedName(this.getUnlocalizedName());
        setRegistryName(new ResourceLocation(Roots.MODID, name));
        setMaxDamage(500);
        setCreativeTab(Roots.tab);
        addHerbs();
    }

    private void addHerbs()
    {
        for (Herb herb : HerbRegistry.REGISTRY)
            herbs.add(herb.getName());
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound NBTData = stack.getTagCompound();

            if (NBTData.hasKey("herb"))
            {
                return "item.sylvan_armor." + NBTData.getString("herb");
            }
        }
        return "item.sylvan_armor.null";
    }

    @Override
    public void initModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    //Temporary method to add an armor for each herb
    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items)
    {
        super.getSubItems(tab, items);

        if (isInCreativeTab(tab)) {
            for (String h : herbs) {
                ItemStack stack = new ItemStack(this);
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setString("herb", h);
                items.add(stack);
            }
        }
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        delayTicks++;

        //One minute delay
        if (itemStack.hasTagCompound() && delayTicks == 1200)
        {
            NBTTagCompound compound = itemStack.getTagCompound();
            Herb herb = HerbRegistry.getHerbByName(compound.getString("herb"));

            if (PowderInventoryUtil.getPowderTotal(player, herb) >= 1)
                PowderInventoryUtil.removePowder(player, herb, 1);

            delayTicks = 0;
        }

    }
}
