package epicsquid.roots.spell;

import epicsquid.roots.init.HerbRegistry;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.modules.SpellModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.List;

public class SpellFeyLight extends SpellBase {
    public static String spellName = "spell_fey_light";
    public static SpellFeyLight instance = new SpellFeyLight(spellName);

    public SpellFeyLight(String name) {
        super(name, TextFormatting.LIGHT_PURPLE, 247f / 255f, 246 / 255f, 210f / 255f, 227f / 255f, 81f / 255f, 244f / 255f);
        this.castType = EnumCastType.INSTANTANEOUS;
        this.cooldown = 20;
        addCost(HerbRegistry.getHerbByName("cloud_berry"), 0.015f);
        addIngredients(new ItemStack(ModItems.cloud_berry), new OreIngredient("dustGold"), new ItemStack(Items.GUNPOWDER), new ItemStack(ModItems.bark_birch), new ItemStack(Items.FLINT_AND_STEEL)
        );

}

    @Override
    public boolean cast(EntityPlayer player, List<SpellModule> modules) {
        World world = player.world;
        RayTraceResult result = this.rayTrace(player, player.isSneaking() ? 1 : 10);
        if (result != null && (!player.isSneaking() && result.typeOfHit == RayTraceResult.Type.BLOCK)) {
            BlockPos pos = result.getBlockPos().offset(result.sideHit);
            if (world.isAirBlock(pos)) {
                if (!world.isRemote) {
                    world.setBlockState(pos, ModBlocks.fey_light.getDefaultState());
                } else {
                    player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5f, 1);
                }
                return true;
            }
        }
        return false;
    }

    @Nullable
    public RayTraceResult rayTrace(EntityPlayer player, double blockReachDistance) {
        Vec3d vec3d = player.getPositionEyes(1.0F);
        Vec3d vec3d1 = player.getLook(1.0F);
        Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        return player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
    }
}

