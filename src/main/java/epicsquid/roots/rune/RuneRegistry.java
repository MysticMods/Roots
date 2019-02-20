package epicsquid.roots.rune;

import epicsquid.roots.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RuneRegistry {
    public static Map<String, Class<? extends RuneBase>> runeRegistry = new HashMap<>();

    public static void init() {
        runeRegistry.put("fleetness_rune", FleetnessRune.class);
        runeRegistry.put("overgrowth_rune", OvergrowthRune.class);
    }

    public static RuneBase getRune(NBTTagCompound compound){
        String runeString = compound.getString("rune");
        RuneBase rune = null;
        if(runeRegistry.get(runeString) != null){
            try {
                rune =  runeRegistry.get(runeString).getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        if(rune != null){
            rune.readFromEntity(compound);
        }

        return rune;
    }

    public static RuneBase getRune(String runeString){
        RuneBase rune = null;
        if(runeRegistry.get(runeString) != null){
            try {
                rune =  runeRegistry.get(runeString).getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        return rune;
    }

}
