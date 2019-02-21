package epicsquid.roots.item;

import epicsquid.mysticallib.model.IModeledObject;
import epicsquid.roots.Roots;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemWildwoodArmor extends ItemArmor implements IModeledObject {

    private int delayTicks = 0;

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
        delayTicks++;
        if (delayTicks == 60 && player.shouldHeal())
            player.heal(1);

        delayTicks = 0;
    }
}
