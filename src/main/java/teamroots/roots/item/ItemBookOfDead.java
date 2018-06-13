package teamroots.roots.item;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.Constants;
import teamroots.roots.RegistryManager;
import teamroots.roots.Roots;
import teamroots.roots.entity.ISpecter;
import teamroots.roots.network.PacketHandler;
import teamroots.roots.network.message.MessageTrapPaperFX;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.spell.SpellRegistry;
import teamroots.roots.util.Misc;

public class ItemBookOfDead extends ItemBase {
	public ItemBookOfDead(String name, boolean addToTab){
		super(name, addToTab);
		setMaxStackSize(16);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        ItemStack stack = player.getHeldItem(hand);
        if (stack.hasTagCompound() && !player.isSneaking()){
        	if (stack.getTagCompound().hasKey(Constants.BOOK_OF_THE_DEAD_LIST)){
	        	try {
	        		NBTTagList l = stack.getTagCompound().getTagList(Constants.BOOK_OF_THE_DEAD_LIST, 10);
	        		if (l.tagCount() > 0){
		        		NBTTagCompound tag = l.getCompoundTagAt(l.tagCount()-1);
						EntityLivingBase entity = (EntityLivingBase) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString(Constants.TRAP_PAPER_ENTITY_TAG))).getEntityClass().getConstructor(World.class).newInstance(world);
						entity.setPosition(pos.getX()+0.5, pos.getY()+1.5, pos.getZ()+0.5);
						entity.readEntityFromNBT(tag.getCompoundTag(Constants.TRAP_PAPER_ENTITY_DATA_TAG));
						if (!world.isRemote){
							world.spawnEntity(entity);
							PacketHandler.INSTANCE.sendToAll(new MessageTrapPaperFX(entity.posX,entity.posY+entity.height/2.0f,entity.posZ));
						}
						l.removeTag(l.tagCount()-1);
	        		}
	        	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	return EnumActionResult.SUCCESS;
        	}
        }
        if (stack.hasTagCompound() && player.isSneaking()){
        	if (stack.getTagCompound().hasKey(Constants.BOOK_OF_THE_DEAD_LIST)){
	        	try {
	        		NBTTagList l = stack.getTagCompound().getTagList(Constants.BOOK_OF_THE_DEAD_LIST, 10);
	        		for (int i = 0; i < l.tagCount(); i ++){
		        		NBTTagCompound tag = l.getCompoundTagAt(i);
						EntityLivingBase entity = (EntityLivingBase) ForgeRegistries.ENTITIES.getValue(new ResourceLocation(tag.getString(Constants.TRAP_PAPER_ENTITY_TAG))).getEntityClass().getConstructor(World.class).newInstance(world);
						entity.setPosition(pos.getX()+0.5+(Misc.random.nextFloat()-0.5f), pos.getY()+1.5+(Misc.random.nextFloat()-0.5f), pos.getZ()+0.5+(Misc.random.nextFloat()-0.5f));
						entity.readEntityFromNBT(tag.getCompoundTag(Constants.TRAP_PAPER_ENTITY_DATA_TAG));
						if (!world.isRemote){
							world.spawnEntity(entity);
							PacketHandler.INSTANCE.sendToAll(new MessageTrapPaperFX(entity.posX,entity.posY+entity.height/2.0f,entity.posZ));
						}
	        		}
	        		stack.getTagCompound().setTag(Constants.BOOK_OF_THE_DEAD_LIST, new NBTTagList());
	        	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	return EnumActionResult.SUCCESS;
        	}
        }
        return EnumActionResult.FAIL;
    }
	
	@Override
	public boolean onEntityItemUpdate(EntityItem item){
		if (!item.getItem().hasTagCompound()){
			item.getItem().setTagCompound(new NBTTagCompound());
		}
		if (!item.getItem().getTagCompound().hasKey(Constants.BOOK_OF_THE_DEAD_LIST)){
			item.getItem().getTagCompound().setTag(Constants.BOOK_OF_THE_DEAD_LIST, new NBTTagList());
		}
		if (item.getItem().getTagCompound().getTagList(Constants.BOOK_OF_THE_DEAD_LIST, 10).tagCount() < 13){
			List<EntityLivingBase> entities = item.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(item.posX-3.5,item.posY,item.posZ-3.5,item.posX+3.5,item.posY+7,item.posZ+3.5));
			for (EntityLivingBase e : entities){
				if (e instanceof ISpecter){
					if (item.world.isRemote){
						ParticleUtil.spawnParticleLineGlow(item.world, (float)e.posX+0.5f*(Misc.random.nextFloat()-0.5f), (float)e.posY+e.height/2.0f+0.5f*(Misc.random.nextFloat()-0.5f), (float)e.posZ+0.5f*(Misc.random.nextFloat()-0.5f), (float)item.posX,(float)item.posY+0.5f,(float)item.posZ, 137, 186, 127, 0.5f, 6.0f, 10);
					}
					e.knockBack(item, 0.05f, e.posX-item.posX, e.posZ-item.posZ);
					if (!e.world.isRemote && Math.pow((e.posX-item.posX),2)+Math.pow((e.posY-item.posY),2)+Math.pow((e.posZ-item.posZ),2) < 0.5f && item.getItem().getTagCompound().getTagList(Constants.BOOK_OF_THE_DEAD_LIST, 10).tagCount() < 13){
						NBTTagCompound tag = new NBTTagCompound();
						tag.setString(Constants.TRAP_PAPER_ENTITY_TAG, EntityRegistry.getEntry(e.getClass()).getRegistryName().toString());
						tag.setTag(Constants.TRAP_PAPER_ENTITY_DATA_TAG, e.getEntityData());
						item.getItem().getTagCompound().getTagList(Constants.BOOK_OF_THE_DEAD_LIST, 10).appendTag(tag);
						e.setDead();
						if (!item.world.isRemote){
							PacketHandler.INSTANCE.sendToAll(new MessageTrapPaperFX(item.posX,item.posY+0.5f,item.posZ));
						}
					}
				}
			}
		}
		return super.onEntityItemUpdate(item);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void initModel(){
		ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName().toString()), new ModelResourceLocation(getRegistryName().toString()+"_on"));
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition(){
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				if (stack.hasTagCompound()){
					if (stack.getTagCompound().hasKey(Constants.BOOK_OF_THE_DEAD_LIST)){
						if (stack.getTagCompound().getTagList(Constants.BOOK_OF_THE_DEAD_LIST, 10).tagCount() > 0){
							return new ModelResourceLocation(getRegistryName().toString()+"_on");
						}
					}
				}
				return new ModelResourceLocation(getRegistryName().toString());
			}
		});
	}
}
