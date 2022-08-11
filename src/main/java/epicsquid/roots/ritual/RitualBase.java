package epicsquid.roots.ritual;

import epicsquid.roots.Roots;
import epicsquid.roots.block.BlockPyre;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.recipe.IRootsRecipe;
import epicsquid.roots.ritual.conditions.ICondition;
import epicsquid.roots.tileentity.TileEntityPyre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class RitualBase {
	protected static Random random = new Random();
	
	protected PropertyTable properties = new PropertyTable();
	private Class<? extends EntityRitualBase> entityClass;
	private List<ICondition> conditions = new ArrayList<>();
	private Item icon;
	private ItemStack stack = ItemStack.EMPTY;
	private String name;
	private TextFormatting color;
	private boolean bold;
	
	public RitualRecipe recipe = RitualRecipe.EMPTY;
	
	protected int duration;
	
	protected boolean disabled;
	private boolean finalised;
	
	public RitualBase(String name, boolean disabled) {
		this.name = name;
		this.disabled = disabled;
		this.duration = 0;
	}
	
	public void setRecipe(RitualRecipe recipe) {
		this.recipe = recipe;
	}
	
	public Class<? extends EntityRitualBase> getEntityClass() {
		return this.entityClass;
	}
	
	public void setEntityClass(Class<? extends EntityRitualBase> entityClass) {
		this.entityClass = entityClass;
	}
	
	public String getFormat() {
		String format = this.color + "";
		if (this.bold) {
			format += TextFormatting.BOLD;
		}
		return format;
	}
	
	public void setColor(TextFormatting color) {
		this.color = color;
	}
	
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	public Item getIcon() {
		return icon;
	}
	
	public ItemStack getItemStack() {
		if (stack.isEmpty()) {
			stack = new ItemStack(icon);
		}
		
		return stack;
	}
	
	public void setIcon(Item icon) {
		this.icon = icon;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public List<ICondition> getConditions() {
		return this.conditions;
	}
	
	public void addCondition(ICondition condition) {
		this.conditions.add(condition);
	}
	
	public boolean isRitualRecipe(TileEntityPyre tileEntityPyre, @Nullable EntityPlayer player) {
		if (isDisabled()) return false;
		return recipe.matches(tileEntityPyre, player);
	}
	
	public boolean canFire(TileEntityPyre Pyre, @Nullable EntityPlayer player, boolean refire) {
		if (!refire) {
			IBlockState state = Pyre.getWorld().getBlockState(Pyre.getPos());
			if (state.getValue(BlockPyre.BURNING)) {
				return false;
			}
		}
		
		return checkTileConditions(Pyre, player);
	}
	
	private boolean checkTileConditions(TileEntityPyre tileEntityPyre, @Nullable EntityPlayer player) {
		boolean success = true;
		for (ICondition condition : this.conditions) {
			if (!condition.check(tileEntityPyre, player)) {
				success = false;
			}
		}
		return success;
	}
	
	@Nullable
	public EntityRitualBase doEffect(World world, BlockPos pos, @Nullable EntityPlayer player) {
		return this.spawnEntity(world, pos, getEntityClass(), player);
	}
	
	@Nullable
	protected EntityRitualBase spawnEntity(World world, BlockPos pos, Class<? extends EntityRitualBase> entity, @Nullable EntityPlayer player) {
		List<EntityRitualBase> pastRituals = world
				.getEntitiesWithinAABB(entity, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 100, pos.getZ() + 1));
		pastRituals.removeIf(o -> !o.getClass().equals(entity));
		if (pastRituals.size() == 0 && !world.isRemote) {
			EntityRitualBase ritual = null;
			try {
				Constructor<? extends EntityRitualBase> cons = entity.getDeclaredConstructor(World.class);
				ritual = cons.newInstance(world);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
			if (ritual == null) {
				return null;
			}
			ritual.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			if (player != null) {
				ritual.setPlayer(player.getUniqueID());
			}
			world.spawnEntity(ritual);
			return ritual;
		} else if (pastRituals.size() > 0) {
			
			for (EntityRitualBase ritual : pastRituals) {
				if (ritual.getClass().equals(entity)) {
					ritual.getDataManager().set(EntityRitualBase.lifetime, getDuration() + 20);
					ritual.getDataManager().setDirty(EntityRitualBase.lifetime);
				}
			}
			return pastRituals.get(0);
		}
		return null;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void init();
	
	@SuppressWarnings("unchecked")
	public List<Ingredient> getIngredients() {
		return recipe.getIngredients();
	}
	
	public void finalise() {
		doFinalise();
		validateProperties();
		finalised = true;
	}
	
	public abstract void doFinalise();
	
	public void validateProperties() {
		List<String> values = properties.finalise();
		if (!values.isEmpty()) {
			StringJoiner join = new StringJoiner(",");
			values.forEach(join::add);
			Roots.logger.error("Ritual '" + name + "' property table has the following keys inserted but not fetched: |" + join.toString() + "|");
		}
	}
	
	public boolean finalised() {
		return finalised;
	}
	
	public PropertyTable getProperties() {
		return properties;
	}
	
	public AxisAlignedBB getBoundingBox() {
		if (properties.hasProperty("radius_x") && properties.hasProperty("radius_y") && properties.hasProperty("radius_z")) {
			int x = properties.getValue("radius_x");
			int y = properties.getValue("radius_y");
			int z = properties.getValue("radius_z");
			return new AxisAlignedBB(-x, -y, -z, x, y, z);
		} else {
			return null;
		}
	}
	
	public static class RitualRecipe implements IRootsRecipe<TileEntityPyre> {
		public static RitualRecipe EMPTY = new RitualRecipe(null, Collections.emptyList());
		
		public List<Ingredient> ingredients;
		public RitualBase ritual;
		
		public RitualRecipe(RitualBase ritual) {
			this.ritual = ritual;
		}
		
		public RitualRecipe(RitualBase ritual, List<Ingredient> ingredients) {
			this.ingredients = ingredients;
			this.ritual = ritual;
		}
		
		public RitualRecipe(RitualBase ritual, Object... stacks) {
			ingredients = new ArrayList<>();
			for (Object stack : stacks) {
				if (stack instanceof Ingredient) {
					ingredients.add((Ingredient) stack);
				} else if (stack instanceof ItemStack) {
					ingredients.add(Ingredient.fromStacks((ItemStack) stack));
				}
			}
			this.ritual = ritual;
		}
		
		@Override
		public List<Ingredient> getIngredients() {
			return ingredients;
		}
		
		public boolean matches(TileEntityPyre tile, @Nullable EntityPlayer player) {
			List<ItemStack> stacks = new ArrayList<>();
			for (int i = 0; i < tile.inventory.getSlots(); i++) {
				stacks.add(tile.inventory.extractItem(i, 1, true));
			}
			return matches(stacks);
		}
	}
}