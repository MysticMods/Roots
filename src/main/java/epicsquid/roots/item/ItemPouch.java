package epicsquid.roots.item;

import java.util.List;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.world.RootsTemplate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPouch extends ItemBase {
  public static final double capacity = 128.0;

  public ItemPouch(String name) {
    super(name);
    setMaxStackSize(1);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (!worldIn.isRemote) {
      WorldServer worldserver = (WorldServer) worldIn;
      MinecraftServer minecraftserver = worldIn.getMinecraftServer();
      TemplateManager templatemanager = worldserver.getStructureTemplateManager();
      ResourceLocation loc = new ResourceLocation(Roots.MODID,"natural_grove");
      Template template = templatemanager.getTemplate(minecraftserver, loc);
      System.out.println("=======0======="+template);
      if (template != null) {
        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE)
            .setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk((ChunkPos) null)
            .setReplacedBlock(Blocks.AIR).setIgnoreStructureBlock(false);

        System.out.println("=======1======="+loc);


        template.addBlocksToWorld(worldIn, pos.add(0, 1, 0), placementsettings);

        IBlockState iblockstate = worldIn.getBlockState(pos);
        worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

        System.out.println("========2======="+pos);
      }
    }
    return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
  }

  public static ItemStack createData(ItemStack stack, String plantName, double quantity) {
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
    stack.getTagCompound().setString("plant", plantName);
    stack.getTagCompound().setDouble("quantity", quantity);
    return stack;
  }

  public static double getQuantity(ItemStack stack, String plantName) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        return stack.getTagCompound().getDouble("quantity");
      }
    }
    return 0.0;
  }

  public static void setQuantity(ItemStack stack, String plantName, double quantity) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        stack.getTagCompound().setDouble("quantity", Math.min(128.0, quantity));
        if (stack.getTagCompound().getDouble("quantity") <= 0) {
          stack.getTagCompound().removeTag("quantity");
          stack.getTagCompound().removeTag("plant");
        }
      }
    }
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return Util.intColor(96, 255, 96);
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        return 1.0 - stack.getTagCompound().getDouble("quantity") / 128.0;
      }
    }
    return 0;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
    if (stack.hasTagCompound()) {
      if (stack.getTagCompound().hasKey("quantity")) {
        tooltip.add(I18n.format(stack.getTagCompound().getString("plant") + ".name") + I18n.format("roots.tooltip.pouch_divider") + (int) Math
            .ceil(stack.getTagCompound().getDouble("quantity")));
      }
    }
  }
}