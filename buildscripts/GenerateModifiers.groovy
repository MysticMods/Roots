import groovy.json.JsonSlurper

// BLAME CLAUDE, I JUST KEPT YELLING AT IT WHILE IT MADE THINGS MORE COMPLICATED AND I SAID, PLEASE, NO
// SO IN SUMMARY, IS IT WORTH IT TO SPEND ALMOST 3 HOURS CAJOLING AN AI TO CONVERT A FILE TO A DATA FORMAT, OR DO IT YOURSELF? ANSWER: UNCERTAIN, LOST 3 HOURS & ALL HOPE.

// ---------------------------------------------------------------------------
// Usage: groovy GenerateModifiers.groovy <modifiers.json> <output-src-root>
// Also runs in-process from build.gradle via GroovyShell; invalid data throws
// rather than exiting, so it must never call System.exit.
//
// SCHEMA
//   <spell>.<name>   a modifier; add `tiers` to make it a ladder, i.e. a
//                    chain of tiers sharing a GroupId
//   conflicts        reserved top-level key: mutual-exclusion clusters
//                    (symmetric); an entry is a modifier id or a ladder id
//
// Per-modifier keys, all optional except icon:
//   cost              a cost term, or a list of them; omitted -> empty
//                     term: {"herb": "cloud_berry", "amount": "BASE_0250"}
//                     amount is a SpellCosts constant name, or a raw number
//                     type: add (default) | mult | mult_total | negate | negate_base
//                     {"type": "negate_base"} takes no herb or amount
//   icon              "minecraft:arrow" (item)  |  "spells/foo" (texture)
//   parent            sibling modifier name within the same spell
//   charge            ChildChargeType value, e.g. "specified"
//   no_token_item     true -> skip TokenItem registration
//   excludes          one-directional conflicts (this modifier only)
//   aliases           legacy registry names this modifier was renamed from;
//                     emitted as addAlias on both registries
//   constant          override the derived java constant name
//
// Extra keys that turn a modifier into a ladder:
//   tiers             list; each entry is a bare icon string or a modifier map
//   costs             per-tier costs, parallel to tiers (vs. `cost`, shared)
//   group_description defaults to true; set false to opt a ladder out
//   Tiers are numbered automatically: roman in the registry id
//   (foo_iii), arabic in the java constant (FOO_3).
//   Any other key is a default for every tier, overridable per tier. `parent`
//   is the ladder's own: it becomes the parent of tier 1, and the rest chain.
//   id_numerals       "roman" (default) | "arabic"   -- registry id suffix
//   constant_numerals "roman" (default) | "arabic"   -- java constant suffix
// ---------------------------------------------------------------------------

def data = new JsonSlurper().parse(new File(args[0]))
def outRoot = new File(args[1])

def ROMAN = ['i', 'ii', 'iii', 'iv', 'v', 'vi', 'vii', 'viii', 'ix', 'x']

def errors = []
def fail = { errors << it }

def COST_TERM_KEYS = ['herb', 'amount', 'type'] as Set
def COST_TYPES = ['add', 'mult', 'mult_total', 'negate', 'negate_base'] as Set

def checkCost = { String id, c ->
  if (c == null) return
  def terms = (c instanceof List) ? c : [c]
  terms.each { t ->
    if (!(t instanceof Map)) { fail("$id: cost term must be an object, got '$t'"); return }
    def bad = t.keySet() - COST_TERM_KEYS
    if (bad) fail("$id: unknown cost key(s) $bad")
    def type = t.type ?: 'add'
    if (!COST_TYPES.contains(type)) fail("$id: unknown cost type '$type'")
    if (type == 'negate_base') {
      if (t.herb || t.amount) fail("$id: negate_base takes no herb or amount")
    } else {
      if (!t.herb) fail("$id: cost term needs a herb")
      if (t.amount == null) fail("$id: cost term needs an amount")
    }
  }
}

// ---------------------------------------------------------------- flatten
def flat = []            // ordered list of resolved modifier maps
def groups = []          // ordered list of group declarations
def byId = [:]
def ladderMembers = [:]  // "shatter/fortune" -> [tier ids...]

def MODIFIER_KEYS = ['cost', 'icon', 'parent', 'charge',
                     'no_token_item', 'constant', 'excludes', 'aliases'] as Set
def LADDER_KEYS   = ['tiers', 'costs', 'group_description'] as Set
// consumed by the ladder itself; every other key becomes a tier default
def LADDER_OWN = LADDER_KEYS + ['parent', 'aliases']

def RESERVED_TOP = ['conflicts'] as Set

data.each { spellName, spell ->
  if (RESERVED_TOP.contains(spellName)) return
  if (!(spell instanceof Map)) { fail("$spellName: expected a map of modifiers"); return }
  def SPELL = spellName.toUpperCase()

  def add = { String name, Map m, Map extra ->
    def id = "$spellName/$name"
    def unknown = m.keySet() - MODIFIER_KEYS
    if (unknown) fail("$id: unknown key(s) $unknown")
    checkCost(id, m.cost)
    def rec = [
      id              : id,
      constant        : m.constant ?: "${SPELL}_${name}".toUpperCase(),
      spell           : SPELL,
      cost            : m.cost,
      icon            : m.icon,
      charge          : m.charge,
      tokenItem       : !(m.no_token_item as boolean),
      parent          : null,
      group           : null,
      conflicts       : [],
      excludes        : (m.excludes ?: []) as List,
      aliases         : (m.aliases ?: []) as List,
    ] + extra
    if (!rec.icon) fail("$id: no icon")
    if (byId.containsKey(id)) fail("$id: duplicate")
    byId[id] = rec
    flat << rec
    rec
  }

  spell.each { name, raw ->
    if (!(raw instanceof Map)) { fail("$spellName/$name: expected a modifier object"); return }
    def parentRef = raw.parent ? "$spellName/${raw.parent}" : null

    if (!raw.containsKey('tiers')) {
      add(name, raw, [parentRef: parentRef])
      return
    }

    // ---- ladder ----
    def where = "$spellName/$name"
    def bad = raw.keySet() - MODIFIER_KEYS - LADDER_KEYS
    if (bad) fail("$where: unknown ladder key(s) $bad")
    if (!(raw.tiers instanceof List) || !raw.tiers) {
      fail("$where: a ladder needs a non-empty tiers list"); return
    }
    if (raw.aliases) fail("$where: aliases belong on individual tiers, not the ladder")
    if (raw.costs != null) {
      if (raw.cost != null) fail("$where: use either cost or costs, not both")
      if (!(raw.costs instanceof List) || raw.costs.size() != raw.tiers.size())
        fail("$where: costs must be parallel to tiers " +
             "(${raw.costs instanceof List ? raw.costs.size() : '?'} vs ${raw.tiers.size()})")
    }

    def gconst = "${SPELL}_${name}".toUpperCase()
    // group descriptions are the norm; only an explicit false opts out
    if (raw.containsKey('group_description') && !(raw.group_description instanceof Boolean)) {
      fail("$where: group_description must be true or false")
    }
    def useGroupDesc = !raw.containsKey('group_description') || raw.group_description
    groups << [constant: gconst, name: where, description: useGroupDesc]

    def tierDefaults = raw.findAll {
      !LADDER_OWN.contains(it.key) && MODIFIER_KEYS.contains(it.key)
    }
    def ids = []
    def prevId = parentRef

    raw.tiers.eachWithIndex { tier, i ->
      def tierMap = (tier instanceof Map) ? tier : [icon: tier]
      if (tierMap.parent) fail("$where tier ${i + 1}: tiers chain automatically; remove parent")
      def m = tierDefaults + tierMap
      if (raw.costs != null && !tierMap.containsKey('cost')) m.cost = raw.costs[i]
      def tierName = "${name}_${ROMAN[i]}"
      // registry ids use roman numerals; java constants use arabic
      def constant = tierMap.constant ?: "${SPELL}_${name}_${i + 1}".toUpperCase()
      def rec = add(tierName, m + [constant: constant], [parentRef: prevId, group: gconst])
      prevId = rec.id
      ids << rec.id
    }
    ladderMembers[where] = ids
  }
}

// ---------------------------------------------------------------- link
flat.each { rec ->
  if (rec.parentRef) {
    def p = byId[rec.parentRef]
    if (!p) fail("$rec.id: unknown parent '$rec.parentRef'")
    else rec.parent = p.constant
  }
}

// conflicts: each cluster entry is one participant (a modifier or a whole
// ladder); conflicts apply BETWEEN participants, never within one.
(data.conflicts ?: []).each { cluster ->
  def participants = cluster.collect { ref ->
    ladderMembers[ref] ?: (byId[ref] ? [ref] : { fail("conflict: unknown '$ref'"); [] }())
  }
  participants.eachWithIndex { a, i ->
    participants.eachWithIndex { b, j ->
      if (i != j) a.each { x -> b.each { y -> byId[x].conflicts << byId[y].constant } }
    }
  }
}

// one-directional conflicts declared on the modifier itself
flat.each { rec ->
  rec.excludes.each { ref ->
    def targets = ladderMembers[ref] ?: (byId[ref] ? [ref] : null)
    if (targets == null) fail("$rec.id: unknown excludes target '$ref'")
    else targets.each { rec.conflicts << byId[it].constant }
  }
}

flat.each { it.conflicts = it.conflicts.unique() - it.constant }

// legacy registry names; every lookup of one resolves to the modifier's id
def aliasPairs = []
flat.each { rec ->
  rec.aliases.each { a ->
    if (a == rec.id) fail("$rec.id: aliases itself")
    else if (byId[a]) fail("$rec.id: alias '$a' is a live modifier id")
    else aliasPairs << [from: a, to: rec.id, item: rec.tokenItem]
  }
}
aliasPairs.groupBy { it.from }.findAll { it.value.size() > 1 }.each { k, v ->
  fail("alias '$k' claimed by ${v*.to}")
}

// cycle check
flat.each { rec ->
  def seen = [rec.constant] as Set
  def cur = rec.parent
  def byConst = flat.collectEntries { [(it.constant): it] }
  while (cur) {
    if (!seen.add(cur)) { fail("$rec.id: parent cycle at $cur"); break }
    cur = byConst[cur]?.parent
  }
}

def dupConsts = flat.groupBy { it.constant }.findAll { it.value.size() > 1 }
dupConsts.each { k, v -> fail("duplicate constant $k: ${v*.id}") }

if (errors) {
  def report = (["Modifier data is invalid:"] + errors.unique().collect { "  - $it" }).join("\n")
  // never System.exit here: this script also runs in-process inside Gradle
  throw new IllegalStateException(report)
}

// ---------------------------------------------------------------- exprs
def COST_OPS = [add: 'add', mult: 'mult', mult_total: 'multTotal', negate: 'negate']

def amountExpr = { a ->
  if (a == null) throw new IllegalArgumentException("cost term needs an amount")
  a instanceof Number ? a.toString() : "SpellCosts.${a}"
}

def costTerm
costTerm = { Map t ->
  def type = t.type ?: 'add'
  if (type == 'negate_base') return 'Cost.negateBase()'
  def op = COST_OPS[type]
  if (!op) throw new IllegalArgumentException("unknown cost type '$type'")
  def herb = "ModHerbs.${t.herb?.toUpperCase()}"
  if (type == 'negate') return "Cost.negate(Cost.add($herb, ${amountExpr(t.amount)}))"
  "Cost.${op}($herb, ${amountExpr(t.amount)})"
}

def costTerms = { c ->
  if (c == null) return []
  if (c instanceof Map) return [c]
  if (c instanceof List) return c.collect { it instanceof Map ? it : [herb: it] }
  throw new IllegalArgumentException("cost must be a term or a list of terms, got: $c")
}

def costExpr = { c ->
  def terms = costTerms(c)
  if (!terms) return 'CostInstance.empty()'
  if (terms.size() == 1) {
    def t = terms[0]
    def type = t.type ?: 'add'
    if (COST_OPS[type] && type != 'negate') {
      return "CostInstance.${COST_OPS[type]}(ModHerbs.${t.herb?.toUpperCase()}, ${amountExpr(t.amount)})"
    }
  }
  "CostInstance.of(${terms.collect { costTerm(it) }.join(', ')})"
}

def iconExpr = { String icon ->
  if (!icon.contains(':')) return "\"$icon\""                    // texture path
  def (ns, path) = icon.tokenize(':')
  ns == 'minecraft' ? "Items.${path.toUpperCase()}" : "ModItems.${path.toUpperCase()}.value()"
}

// The SpellModifier overloads that actually exist. A ChildChargeType can only
// be passed alongside a parent slot AND an explicit GroupId, so those are
// forced on rather than authored.
def LEGAL_SHAPES = [
  ['cost', 'spell'],
  ['cost', 'spell', 'group'],
  ['cost', 'spell', 'charge'],
  ['cost', 'parent', 'spell'],
  ['cost', 'parent', 'spell', 'group'],
  ['cost', 'parent', 'spell', 'charge', 'group'],
  ['cost', 'parent', 'spell', 'conflicts'],
  ['cost', 'parent', 'spell', 'group', 'conflicts'],
  ['cost', 'parent', 'spell', 'charge', 'group', 'conflicts'],
] as Set

def ctorShape = { rec ->
  // a charge type with a group has no parentless overload, so force the slot
  def parentSlot = rec.parent || rec.conflicts || (rec.charge && rec.group)
  def shape = ['cost']
  if (parentSlot) shape << 'parent'
  shape << 'spell'
  if (rec.charge) shape << 'charge'
  if (rec.group || (rec.charge && parentSlot)) shape << 'group'
  if (rec.conflicts) shape << 'conflicts'
  shape
}

def ctorArgs = { rec ->
  ctorShape(rec).collectMany { part ->
    switch (part) {
      case 'cost':      return [costExpr(rec.cost)]
      case 'parent':    return [rec.parent ? "ModModifiers.${rec.parent}.getKey()" : 'null']
      case 'spell':     return ["ModSpells.${rec.spell}.getKey()"]
      case 'charge':    return ["ChildChargeType.${rec.charge.toUpperCase()}"]
      case 'group':     return [rec.group ?: 'GroupId.NONE']
      case 'conflicts': return rec.conflicts.collect { "ModModifiers.${it}.getKey()" }
    }
  }.join(', ')
}

flat.each { rec ->
  def shape = ctorShape(rec)
  if (!LEGAL_SHAPES.contains(shape)) fail("$rec.id: no SpellModifier overload for $shape")
}

if (errors) {
  def report = (["Modifier data is invalid:"] + errors.unique().collect { "  - $it" }).join("\n")
  // never System.exit here: this script also runs in-process inside Gradle
  throw new IllegalStateException(report)
}

def HEADER = '// GENERATED FILE - DO NOT EDIT.\n' +
             '// Source: data/modifiers.json  ->  :generateModifiers\n'

// ---------------------------------------------------------------- ModModifiers
def mods = new StringBuilder()
mods << HEADER << """package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.modifier.ChildChargeType;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.reference.SpellCosts;
import mysticmods.roots.api.registry.GroupId;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModModifiers {
  private static final DeferredRegister<SpellModifier> REGISTER = DeferredRegister.create(RootsRegistries.Keys.SPELL_MODIFIERS, RootsAPI.MODID);
  private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RootsAPI.MODID);

  public static final List<GroupId> GROUP_IDS = new ArrayList<>();

"""

groups.each { g ->
  mods << "  public static final GroupId ${g.constant} = " +
          (g.description ? "group(\"${g.name}\", true);\n" : "group(\"${g.name}\");\n")
}
mods << "\n"

flat.each { rec ->
  mods << "  public static final DeferredHolder<SpellModifier, SpellModifier> ${rec.constant} = " +
          "REGISTER.register(\"${rec.id}\", () -> new SpellModifier(${ctorArgs(rec)}));\n"
}

mods << "\n  static {\n"
if (aliasPairs) {
  aliasPairs.each { a ->
    mods << "    REGISTER.addAlias(RootsAPI.rl(\"$a.from\"), RootsAPI.rl(\"$a.to\"));\n"
    if (a.item) mods << "    ITEMS.addAlias(RootsAPI.rl(\"$a.from\"), RootsAPI.rl(\"$a.to\"));\n"
  }
  mods << "\n"
}
flat.findAll { it.tokenItem }.each { mods << "    modifier(ITEMS, ModModifiers.${it.constant});\n" }
mods << """  }

  private static TokenItem.SpellModifierTokenItem modifier(Holder<SpellModifier> modifier) {
    return new TokenItem.SpellModifierTokenItem(modifier.getKey(), new Item.Properties().stacksTo(1));
  }

  private static DeferredHolder<Item, TokenItem.SpellModifierTokenItem> modifier(DeferredRegister.Items reg, Holder<SpellModifier> modifier) {
    return reg.register(modifier.getKey().location().getPath(), () -> modifier(modifier));
  }

  public static GroupId group(String name) {
    return group(name, false);
  }

  public static GroupId group(String name, boolean useGroupDescription) {
    var id = new GroupId(name, useGroupDescription);
    GROUP_IDS.add(id);
    return id;
  }

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
    ITEMS.register(bus);
  }
}
"""

// ---------------------------------------------------------------- provider
def prov = new StringBuilder()
prov << HEADER << """package mysticmods.roots.gen.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.init.ModModifiers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class RootsModifierModelProvider extends ItemModelProvider {
  public RootsModifierModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    for (SpellModifier modifier : RootsRegistries.SPELL_MODIFIERS) {
      if (BuiltInRegistries.ITEM.get(modifier.builtInRegistryHolder().getKey().location()) == Items.AIR) {
        throw new NullPointerException("Modifier " + modifier.builtInRegistryHolder().getKey()
            .location() + " does not have an equivalent item!");
      }
    }

"""

flat.each { prov << "    modifier(ModModifiers.${it.constant}, ${iconExpr(it.icon)});\n" }

prov << """  }

  public ItemModelBuilder modifier(Holder<SpellModifier> itemHolder, String location) {
    if (!location.contains(":")) {
      return modifier(itemHolder, RootsAPI.rl(location));
    } else {
      return modifier(itemHolder, ResourceLocation.parse(location));
    }
  }

  public ItemModelBuilder modifier(Holder<SpellModifier> itemHolder, ResourceLocation location) {
    if (!location.getPath().startsWith("item")) {
      location = location.withPrefix("item/");
    }
    return getBuilder(itemHolder.getKey().location().withPrefix("item/").toString())
        .parent(new ModelFile.UncheckedModelFile("item/generated"))
        .texture("layer0", location);
  }

  public ItemModelBuilder modifier(Holder<SpellModifier> itemHolder, Item icon) {
    ResourceLocation item = itemHolder.getKey().location();

    return getBuilder(item.withPrefix("item/").toString())
        .parent(getExistingFile(icon.builtInRegistryHolder().getKey().location()));
  }

  @Override
  public String getName() {
    return "Roots Modifier Model Provider";
  }
}
"""

def write = { String rel, StringBuilder content ->
  def f = new File(outRoot, rel); f.parentFile.mkdirs(); f.text = content.toString()
  println "wrote ${f.path}"
}
write('mysticmods/roots/init/ModModifiers.java', mods)
write('mysticmods/roots/gen/client/RootsModifierModelProvider.java', prov)
println "${flat.size()} modifiers, ${groups.size()} groups"
