package com.example;

import com.example.commands.ExampleCommand;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.Mod.EventHandler;
import io.discloader.discloader.common.discovery.Mod.Instance;
import io.discloader.discloader.common.event.DLPreInitEvent;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.registry.CommandRegistry;

@Mod(desc = ExampleMod.DESC, modid = ExampleMod.MODID, name = ExampleMod.NAME, version = ExampleMod.VERSION, author = ExampleMod.AUTHORS)
public class ExampleMod extends EventListenerAdapter {
   
   public static final String DESC = "Example Mod";
   public static final String MODID = "examplemod";
   public static final String NAME = "ExampleMod";
   public static final String VERSION = "1.0.0";
   public static final String AUTHORS = "R3alCl0ud";
   public static final Command exampleCommand = new ExampleCommand();
   
   @Instance(MODID)
   public static ExampleMod instance;

   public ExampleMod() {
       DiscLoader.getDiscLoader().addEventListener(this);
   }

   @Override
   public void Ready(ReadyEvent e) {
       
   }
   
   @EventHandler
   public void preInit(DLPreInitEvent e) {
       CommandRegistry.registerCommand(exampleCommand, exampleCommand.getUnlocalizedName());
       
   }

}