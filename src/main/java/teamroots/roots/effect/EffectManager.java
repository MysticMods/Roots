package teamroots.roots.effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import teamroots.roots.Constants;
import teamroots.roots.capability.PlayerDataProvider;

public class EffectManager {
	public static Map<String, Effect> effects = new HashMap<String, Effect>();
	
	public static Effect effect_time_stop, 
						effect_invulnerability, 
						effect_fireresist, 
						effect_arcanism, 
						effect_naturescure, 
						effect_regen;
	
	public static void init(){
		effects.put("effect_invulnerability", effect_invulnerability = new EffectInvulnerability("effect_invulnerability",false));
		effects.put("effect_time_stop", effect_time_stop = new EffectTimeStop("effect_time_stop",false));
		effects.put("effect_fireresist", effect_fireresist = new EffectFireResist("effect_fireresist",false));
		effects.put("effect_arcanism", effect_arcanism = new EffectArcanism("effect_arcanism",false));
		effects.put("effect_naturescure", effect_naturescure = new EffectNaturesCure("effect_naturescure",false));
		effects.put("effect_regen", effect_regen = new EffectRegen("effect_regen",false));
	}
	
	public static void assignEffect(EntityLivingBase entity, String effect, int duration, NBTTagCompound data){
		if (!(entity instanceof EntityPlayer)){
			if (!entity.getEntityData().hasKey(Constants.EFFECT_TAG)){
				entity.getEntityData().setTag(Constants.EFFECT_TAG, new NBTTagCompound());
			}
		}
		else if (entity.hasCapability(PlayerDataProvider.playerDataCapability, null)){
			if (!entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().hasKey(Constants.EFFECT_TAG)){
				entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().setTag(Constants.EFFECT_TAG, new NBTTagCompound());
				entity.getCapability(PlayerDataProvider.playerDataCapability, null).markDirty();
			}
		}
		NBTTagCompound tag = null;
		if (!(entity instanceof EntityPlayer)){
			tag = entity.getEntityData().getCompoundTag(Constants.EFFECT_TAG);
		}
		else if (entity.hasCapability(PlayerDataProvider.playerDataCapability, null)){
			tag = entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().getCompoundTag(Constants.EFFECT_TAG);
		}
		if (tag != null){
			if (!tag.hasKey(effect)){
				effects.get(effect).onApplied(entity, data);
			}
			tag.setInteger(effect, duration);
			tag.setTag(effect+"_data", data);
			if (entity.hasCapability(PlayerDataProvider.playerDataCapability, null)){
				entity.getCapability(PlayerDataProvider.playerDataCapability, null).markDirty();
			}
		}
	}
	
	public static boolean hasEffect(EntityLivingBase entity, String effect){
		if (entity != null){
			if (!(entity instanceof EntityPlayer)){
				if (entity.getEntityData().hasKey(Constants.EFFECT_TAG)){
					return entity.getEntityData().getCompoundTag(Constants.EFFECT_TAG).hasKey(effect);
				}
			}
			else if (entity.hasCapability(PlayerDataProvider.playerDataCapability, null)){
				if (entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().hasKey(Constants.EFFECT_TAG)){
					return entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().getCompoundTag(Constants.EFFECT_TAG).hasKey(effect);
				}
			}
		}
		return false;
	}
	
	public static void setEffectData(EntityLivingBase entity, String effect, NBTTagCompound data){
		if (entity != null){
			if (!(entity instanceof EntityPlayer)){
				if (entity.getEntityData().hasKey(Constants.EFFECT_TAG)){
					entity.getEntityData().getCompoundTag(Constants.EFFECT_TAG).setTag(effect+"_data",data);
				}
			}
			else if (entity.hasCapability(PlayerDataProvider.playerDataCapability, null)){
				if (entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().hasKey(Constants.EFFECT_TAG)){
					entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().getCompoundTag(Constants.EFFECT_TAG).setTag(effect+"_data",data);
					entity.getCapability(PlayerDataProvider.playerDataCapability, null).markDirty();
				}
			}
		}
	}
	
	public static NBTTagCompound getEffectData(EntityLivingBase entity, String effect){
		if (entity != null){
			if (!(entity instanceof EntityPlayer)){
				if (entity.getEntityData().hasKey(Constants.EFFECT_TAG)){
					if (entity.getEntityData().getCompoundTag(Constants.EFFECT_TAG).hasKey(effect+"_data")){
						return entity.getEntityData().getCompoundTag(Constants.EFFECT_TAG).getCompoundTag(effect+"_data");
					}
				}
			}
			else if (entity.hasCapability(PlayerDataProvider.playerDataCapability, null)){
				if (entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().hasKey(Constants.EFFECT_TAG)){
					if (entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().getCompoundTag(Constants.EFFECT_TAG).hasKey(effect+"_data")){
						return entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().getCompoundTag(Constants.EFFECT_TAG).getCompoundTag(effect+"_data");
					}
				}
			}
		}
		return null;
	}
	
	public static int getDuration(EntityLivingBase entity, String effect){
		if (entity != null){
			if (!(entity instanceof EntityPlayer)){
				if (entity.getEntityData().hasKey(Constants.EFFECT_TAG)){
					if (entity.getEntityData().getCompoundTag(Constants.EFFECT_TAG).hasKey(effect)){
						return entity.getEntityData().getCompoundTag(Constants.EFFECT_TAG).getInteger(effect);
					}
				}
			}
			else if (entity.hasCapability(PlayerDataProvider.playerDataCapability, null)){
				if (entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().hasKey(Constants.EFFECT_TAG)){
					if (entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().getCompoundTag(Constants.EFFECT_TAG).hasKey(effect)){
						return entity.getCapability(PlayerDataProvider.playerDataCapability, null).getData().getCompoundTag(Constants.EFFECT_TAG).getInteger(effect);
					}
				}
			}
		}
		return 0;
	}
	
	public static void tickEffects(EntityLivingBase entity){
		if (entity != null){
			if (!(entity instanceof EntityPlayer)){
				if (entity.getEntityData().hasKey(Constants.EFFECT_TAG)){
					NBTTagCompound tag = entity.getEntityData().getCompoundTag(Constants.EFFECT_TAG);
					if (tag != null){
						ArrayList<String> keys = new ArrayList<String>();
						for (String s : tag.getKeySet()){
							if (!s.endsWith("_data")){
								NBTTagCompound data_tag = tag.getCompoundTag(s+"_data");
								if (data_tag != null){
									effects.get(s).onTick(entity,tag.getInteger(s)-1,data_tag);
									tag.setInteger(s, tag.getInteger(s)-1);
									if (tag.getInteger(s) <= 0){
										keys.add(s);
									}
								}
							}
						}
						for (int i = 0; i < keys.size(); i ++){
							String s = keys.get(i);
							NBTTagCompound data_tag = tag.getCompoundTag(s+"_data");
							effects.get(s).onEnd(entity, data_tag);
							tag.removeTag(s);
							tag.removeTag(s+"_data");
							if (tag.getKeySet().size() == 0){
								entity.getEntityData().removeTag(Constants.EFFECT_TAG);
							}
						}
					}
				}
			}
			else {
				EntityPlayer player = (EntityPlayer)entity;
				if (player.hasCapability(PlayerDataProvider.playerDataCapability, null)){
					NBTTagCompound tag = player.getCapability(PlayerDataProvider.playerDataCapability, null).getData().getCompoundTag(Constants.EFFECT_TAG);
					if (tag != null){
						ArrayList<String> keys = new ArrayList<String>();
						for (String s : tag.getKeySet()){
							if (!s.endsWith("_data")){
								NBTTagCompound data_tag = tag.getCompoundTag(s+"_data");
								if (data_tag != null){
									effects.get(s).onTick(entity,tag.getInteger(s)-1, data_tag);
									tag.setInteger(s, tag.getInteger(s)-1);
									if (tag.getInteger(s) <= 0){
										keys.add(s);
									}
								}
							}
						}
						for (int i = 0; i < keys.size(); i ++){
							String s = keys.get(i);
							NBTTagCompound data_tag = tag.getCompoundTag(s+"_data");
							effects.get(s).onEnd(entity, data_tag);
							tag.removeTag(s);
							tag.removeTag(s+"_data");
							if (tag.getKeySet().size() == 0){
								player.getCapability(PlayerDataProvider.playerDataCapability, null).getData().removeTag(Constants.EFFECT_TAG);
							}
						}
	    				player.getCapability(PlayerDataProvider.playerDataCapability, null).markDirty();
					}
				}
			}
		}
	}
}
