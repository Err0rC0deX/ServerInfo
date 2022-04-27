package net.fabricmc.err.serverinfo.commands;

import java.io.File;
import java.text.DecimalFormat;

import net.minecraft.text.LiteralText;
import net.fabricmc.err.serverinfo.util.ChatColour;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

public class ServerInfo implements Command<ServerCommandSource> {
	private static final double GIBIBYTE = Math.pow(2, 30);

	private static String tpsDisplayString(double tps, Boolean useColour) {
		String tpsValue = new DecimalFormat("#.##").format(tps);
		if(!useColour) return tpsValue;
		if (tps>=20) return ChatColour.DARK_GREEN + tpsValue;
		if (tps>=17) return ChatColour.GREEN + tpsValue;
		if (tps>=15) return ChatColour.YELLOW + tpsValue;
		if (tps>=10) return ChatColour.GOLD + tpsValue;
		if (tps>=5) return ChatColour.RED + tpsValue;
		return ChatColour.DARK_RED + tpsValue;
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerCommandSource source = context.getSource();

		try{
			source.getPlayer();
			printColour(source);
		}
		catch (CommandSyntaxException exception) {
			print(source);
		}
		
		return Command.SINGLE_SUCCESS;
	}

	private static void print(ServerCommandSource source) {
		double tickTime = source.getServer().getTickTime();
		double tps = 1000.0 / tickTime;
		source.sendFeedback(new LiteralText("TPS: " + tpsDisplayString(tps, false)), false);		

		source.sendFeedback(new LiteralText("OS: " + System.getProperty("os.name")), false);

		File diskSpace = new File(FabricLoader.getInstance().getConfigDir() + "/..");
		double free = diskSpace.getFreeSpace() / GIBIBYTE;
		double total = diskSpace.getTotalSpace() / GIBIBYTE;
		double percentage = ((total - free) / total) * 100;
		source.sendFeedback(new LiteralText(
			"Disk space used: " +
			new DecimalFormat("#.##").format(total-free) + "/" +
			new DecimalFormat("#.##").format(total) + " GB " +
			"(" + new DecimalFormat("#.##").format(percentage) + "% used)"
		), false);
	
		free = Runtime.getRuntime().freeMemory()/1048576;
		total = Runtime.getRuntime().totalMemory()/1048576;
		percentage = ((total - free) / total) * 100;
		source.sendFeedback(new LiteralText(
			"RAM Used: " + 
			new DecimalFormat("#.###").format(total-free) + "/" + new DecimalFormat("#.###").format(total) + " MB " +
			 "(" + new DecimalFormat("#.##").format(percentage) + "% used)"
		), false);

		source.sendFeedback(new LiteralText("Number of cores: " + Runtime.getRuntime().availableProcessors()), false);
		source.sendFeedback(new LiteralText("Java version: " + System.getProperty("java.version")), false);
		source.sendFeedback(new LiteralText("Chunks loaded: " + source.getWorld().getChunkManager().getLoadedChunkCount()), false);
	}

	private static void printColour(ServerCommandSource source) {
		double tickTime = source.getServer().getTickTime();
		double tps = 1000.0 / tickTime;
		source.sendFeedback(new LiteralText(ChatColour.AQUA + "TPS: " + tpsDisplayString(tps, true)), false);

		source.sendFeedback(new LiteralText(ChatColour.AQUA + "OS: " + ChatColour.YELLOW + System.getProperty("os.name")), false);

		File diskSpace = new File(FabricLoader.getInstance().getConfigDir() + "/..");
		double free = diskSpace.getFreeSpace() / GIBIBYTE;
		double total = diskSpace.getTotalSpace() / GIBIBYTE;
		double percentage = ((total - free) / total) * 100;
		source.sendFeedback(new LiteralText(
			ChatColour.AQUA + "Disk space used: " +
			ChatColour.GREEN + new DecimalFormat("#.##").format(total-free) + ChatColour.YELLOW + "/" +	new DecimalFormat("#.##").format(total) + ChatColour.YELLOW + " GB " +
			"(" + new DecimalFormat("#.##").format(percentage) + "% used)"
		), false);
	
		free = Runtime.getRuntime().freeMemory()/1048576;
		total = Runtime.getRuntime().totalMemory()/1048576;
		percentage = ((total - free) / total) * 100;
		source.sendFeedback(new LiteralText(
			ChatColour.AQUA + "RAM Used: " + 
			ChatColour.GREEN + new DecimalFormat("#.###").format(total-free) + ChatColour.YELLOW + "/" + new DecimalFormat("#.###").format(total) + ChatColour.YELLOW + " MB " +
			"(" + new DecimalFormat("#.##").format(percentage) + "% used)"
		), false);
		
		source.sendFeedback(new LiteralText(ChatColour.AQUA + "Number of cores: " + ChatColour.YELLOW + Runtime.getRuntime().availableProcessors()), false);
		source.sendFeedback(new LiteralText(ChatColour.AQUA + "Java version: " + ChatColour.YELLOW + System.getProperty("java.version")), false);
		source.sendFeedback(new LiteralText(ChatColour.AQUA + "Chunks loaded: " + ChatColour.YELLOW + source.getWorld().getChunkManager().getLoadedChunkCount()), false);
	}
}
