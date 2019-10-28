package epicsquid.roots.integration.crafttweaker.commands;

import crafttweaker.mc1120.commands.CTChatCommand;

public class Inject {
  public static void inject () {
    CTChatCommand.registerCommand(new CommandRecipes());
  }
}
