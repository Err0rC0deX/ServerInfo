package net.fabricmc.err.serverinfo.commands;


import net.minecraft.server.command.ServerCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

import com.mojang.brigadier.tree.LiteralCommandNode;

public class Commands {

	public class Permissions {
		public static int Player = 0;
		public static int Player_No_Spawn_Protection = 1;
		public static int Player_Commander = 2;
		public static int Server_Commander = 3;
		public static int OP = 4;
	}

	public static void initialize(){
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			LiteralCommandNode<ServerCommandSource> modNode = CommandManager
				.literal(net.fabricmc.err.serverinfo.ServerInfo.MODID)
				.executes(new ServerInfo())
				.build();

			LiteralCommandNode<ServerCommandSource> versionNode = CommandManager
				.literal("version")
				.executes(new Version())
				.build();
			
			dispatcher.getRoot().addChild(modNode);
			modNode.addChild(versionNode);
        });
	}
}
