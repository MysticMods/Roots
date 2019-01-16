package epicsquid.roots.rune;

import epicsquid.roots.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RuneRegistry {
    public static Map<Item, Class<? extends Rune>> runeRegistry = new HashMap<>();

    public static void init() {
        runeRegistry.put(ModItems.aer_ash, AerRune.class);
    }

    public static Rune getRune(NBTTagCompound compound){
        if(!compound.hasKey("reagent")){
            return null;
        }

        Item runeItem = new ItemStack(compound.getCompoundTag("reagent")).getItem();
        Rune rune = null;
        if(runeRegistry.get(runeItem) != null){
            try {

                rune =  runeRegistry.get(runeItem).getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        if(rune != null){
            rune.readFromEntity(compound);
        }

        return rune;
    }

    public static Rune getRune(Item item){
        Rune rune = null;
        if(runeRegistry.get(item) != null){
            try {

                rune =  runeRegistry.get(item).getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        return rune;
    }
}
