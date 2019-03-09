package epicsquid.roots.spell.modules;

import net.minecraft.item.ItemStack;

public class SpellModule {

    private ItemStack ingredient;
    private String name;

    public SpellModule(String name, ItemStack ingredient){
        this.name = name;
        this.ingredient = ingredient;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIngredient() {
        return ingredient;
    }
}
