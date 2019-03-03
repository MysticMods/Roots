package epicsquid.roots.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.util.PowderInventoryUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
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

public class ItemSylvanArmor extends ItemArmor implements IModeledObject {

    private List<String> herbs = new ArrayList<>();
    private int delayTicks = 0;

    public ItemSylvanArmor(ArmorMaterial material, EntityEquipmentSlot slot, String name)
    {
        super(material, 0, slot);
        setUnlocalizedName(name);
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
            if (compound.hasKey("herb"))
            {
                Herb herb = HerbRegistry.getHerbByName(compound.getString("herb"));

                if (PowderInventoryUtil.getPowderTotal(player, herb) >= 1)
                    PowderInventoryUtil.removePowder(player, herb, 1);
            }

            delayTicks = 0;
        }

    }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
        {
            if (stack.hasTagCompound())
            {
                NBTTagCompound compound = stack.getTagCompound();
                if (compound.hasKey("herb"))
                {
                    String herb = compound.getString("herb");
                    tooltip.add("Infused Herb: " + herb);
                }
        }
    }
}
