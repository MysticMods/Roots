package mysticmods.roots.api.milestone;

import net.minecraft.network.chat.Component;

// TODO:
// - Milestones are "events" that are sent
// - Can milestones be repeated?
// - Milestones are triggered by player actions
//   - Learning a new spell is a milestone
//   - Starting a ritual for the first time is a milestone
//   - Discovering herbs for the first time is a milestone
//   - Breaking the pacifist pack is a milestone
//   - Redeeming yourself is a milestone
//   - Is breaking the pact again another milestone? How many loops is it?
public interface IMilestone {
  Component getTitle();
  Component getDescription();


}
