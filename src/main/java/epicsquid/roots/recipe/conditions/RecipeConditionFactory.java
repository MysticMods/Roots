package epicsquid.roots.recipe.conditions;

import com.google.gson.JsonObject;
import epicsquid.roots.util.OreCondition;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class RecipeConditionFactory {
    public static class CopperExists implements IConditionFactory {

        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            return () -> OreCondition.oreExists("ingotCopper");
        }
    }

     public static class SilverExists implements IConditionFactory {

        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            return () -> OreCondition.oreExists("ingotSilver");
        }
    }
}
