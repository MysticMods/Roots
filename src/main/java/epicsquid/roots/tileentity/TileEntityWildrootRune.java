package epicsquid.roots.tileentity;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.tile.TileBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.rune.RuneBase;
import epicsquid.roots.rune.RuneRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class TileEntityWildrootRune extends TileBase implements ITickable {

    private TileEntityIncenseBurner incenseBurner;
    private static final BlockPos[] INCENSE_POSITIONS = new BlockPos[]{new BlockPos(0, -1, 1), new BlockPos(0, -1, -1), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0)};

    public final Map<Item, PotionEffect> effectItemMap = new HashMap<>();

    public TileEntityWildrootRune(){
        this.incenseBurner = null;

        effectItemMap.put(ModItems.moonglow_leaf, new PotionEffect(MobEffects.SPEED, 2400, 1));
        effectItemMap.put(ModItems.spirit_herb, new PotionEffect(MobEffects.NIGHT_VISION, 2400, 1));
        effectItemMap.put(ModItems.infernal_bulb, new PotionEffect(MobEffects.FIRE_RESISTANCE, 2400, 1));
        effectItemMap.put(ModItems.dewgonia, new PotionEffect(MobEffects.WATER_BREATHING, 3600, 1));
        effectItemMap.put(ModItems.stalicripe, new PotionEffect(MobEffects.STRENGTH, 1200, 1));
        effectItemMap.put(ModItems.spirit_herb, new PotionEffect(MobEffects.INVISIBILITY, 2400, 1));
        effectItemMap.put(ModItems.cloud_berry, new PotionEffect(MobEffects.JUMP_BOOST, 1200, 1));
        effectItemMap.put(ModItems.pereskia, new PotionEffect(MobEffects.REGENERATION, 200, 1));

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public boolean activate(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if(incenseBurner == null){
            return true;
        }
        if(heldItem.isEmpty() && incenseBurner.isLit()){
            if(this.effectItemMap.getOrDefault(incenseBurner.burningItem(), null) != null){
                player.addPotionEffect(this.effectItemMap.get(incenseBurner.burningItem()));
            }
        }
        return true;
    }

    @Override
    public void update() {
        if(world.getWorldTime() % 20 == 0){
            if(this.incenseBurner != null){
                if(!this.incenseBurner.isLit()){
                    this.incenseBurner = null;
                }
            }
            else{
                for(BlockPos pos : INCENSE_POSITIONS){
                    if(world.getBlockState(this.getPos().add(pos)).getBlock() == ModBlocks.incense_burner){
                        TileEntityIncenseBurner incenseBurner = (TileEntityIncenseBurner) world.getTileEntity(this.getPos().add(pos));
                        if(incenseBurner == null){
                            continue;
                        }
                        if(incenseBurner.isLit()){
                            this.incenseBurner = incenseBurner;
                            break;
                        }
                    }
                }
            }
        }
    }

    public TileEntityIncenseBurner getIncenseBurner() {
        return incenseBurner;
    }


}
