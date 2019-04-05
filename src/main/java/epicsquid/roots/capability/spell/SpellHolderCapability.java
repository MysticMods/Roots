package epicsquid.roots.capability.spell;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class SpellHolderCapability implements ISpellHolderCapability {

    private Map<Integer, SpellBase> spells = new Int2ObjectOpenHashMap<>();
    private Map<Integer, List<SpellModule>> spellModules = new Int2ObjectOpenHashMap<>();
    private int selectedSlot = 0;
    private int cooldown = 0;
    private int lastCooldown = 0;

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();
        for(Map.Entry<Integer, SpellBase> entry : this.spells.entrySet()){
            compound.setString("spell_"+entry.getKey(), (entry.getValue() == null) ? "" : entry.getValue().getName());
        }

        for(Map.Entry<Integer, List<SpellModule>> entry : this.spellModules.entrySet()){
            List<SpellModule> modules = entry.getValue();
            for (int i = 0; i < modules.size(); i++){
                compound.setString(entry.getKey() + "_spell_" + i, modules.get(i).getName());
            }
        }

        compound.setInteger("selectedSlot", this.selectedSlot);
        compound.setInteger("cooldown", this.cooldown);
        compound.setInteger("lastCooldown", this.lastCooldown);
        return compound;
    }

    @Override
    public void setData(NBTTagCompound tag) {
        for(int i = 0; i < 5; i++){
            if(!tag.getString("spell_" + i).isEmpty()){
                spells.put(i, SpellRegistry.getSpell(tag.getString("spell_" + i)));
            }
        }

        for(int i = 0; i < 5; i++){
            List<SpellModule> modules = new ArrayList<>();
            for(int j = 0; j < 5; j++){
                if(!tag.getString(i +"_spell_" + j).isEmpty()){
                    modules.add(ModuleRegistry.getModule(tag.getString(i +"_spell_" + j)));
                }
            }
            if(modules.size() > 0){
                this.spellModules.put(i, modules);
            }
        }

        this.selectedSlot = tag.getInteger("selectedSlot");
        this.cooldown = tag.getInteger("cooldown");
        this.lastCooldown = tag.getInteger("lastCooldown");
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void clean() {

    }

    @Override
    public boolean hasSpell() {
        if(!spells.isEmpty()){
            for(Map.Entry<Integer, SpellBase> entry : this.spells.entrySet()){
                if(entry != null){
                    if(entry.getValue() != null){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasSpellInSlot() {
        return spells.getOrDefault(this.selectedSlot, null) != null;
    }

    @Override
    public boolean isEmpty() {
        return spells.values().stream().filter(Objects::isNull).count() != 5;
    }

    @Override
    public SpellBase getSpellInSlot (int slot) {
        if (slot < 0 || slot >= 5) return null;
        return spells.getOrDefault(slot, null);
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public int getLastCooldown() {
        return lastCooldown;
    }

    @Override
    public void setLastCooldown(int lastCooldown) {
        this.lastCooldown = lastCooldown;
    }

    @Override
    public SpellBase getSelectedSpell() {
        return spells.getOrDefault(this.selectedSlot, null);
    }

    @Override
    public void clearSelectedSlot() {
        spells.put(this.selectedSlot, null);
    }

    @Override
    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    @Override
    public void setSelectedSlot(int slot) {
        this.selectedSlot = slot;
    }

    @Override
    public void setSpellToSlot(SpellBase spell) {
        assert hasFreeSlot();
        setSelectedSlot(getNextFreeSlot());
        this.spells.put(this.selectedSlot, spell);
        this.spellModules.remove(this.selectedSlot);
    }

    @Override
    public void addModule(SpellModule module) {
        if(this.spellModules.getOrDefault(this.selectedSlot, null) == null){
            List<SpellModule> modules = new ArrayList<>();
            modules.add(module);
            this.spellModules.put(this.selectedSlot, modules);
        }
        else{
            List<SpellModule> modules = this.spellModules.get(this.selectedSlot);
            modules.add(module);
        }
    }

    @Override
    public List<SpellModule> getSelectedModules() {
        return this.spellModules.getOrDefault(this.selectedSlot, new ArrayList<>());
    }

    @Override
    public int getNextFreeSlot () {
        for (int i = 0; i < 5; i++) {
            if (spells.getOrDefault(i, null) == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean hasFreeSlot () {
        return getNextFreeSlot() != -1;
    }
}
