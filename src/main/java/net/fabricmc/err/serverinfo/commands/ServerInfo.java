package net.fabricmc.err.serverinfo.commands;

import java.io.File;
import java.text.DecimalFormat;

import net.fabricmc.err.serverinfo.util.ChatColour;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

public class ServerInfo implements Command<ServerCommandSource>
{
	private static final double GIBIBYTE = Math.pow(2, 30);

	private static String tpsDisplayString(double tps, Boolean useColour)
	{
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
	public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		ServerCommandSource source = context.getSource();
		ServerPlayerEntity player = source.getPlayer();
		if(player != null) printColour(source);
		else print(source);

		return Command.SINGLE_SUCCESS;
	}

	private static void print(ServerCommandSource source)
	{
		double tickTime = source.getServer().getAverageTickTime();
		double tps = 1000.0 / tickTime;
		source.sendFeedback(() -> Text.literal("TPS: " + tpsDisplayString(tps, false)), false);

		source.sendFeedback(() ->  Text.literal("OS: " + System.getProperty("os.name")), false);

		File diskSpace = new File(FabricLoader.getInstance().getConfigDir() + "/..");
		double freeDisk = diskSpace.getFreeSpace() / GIBIBYTE;
		double totalDisk = diskSpace.getTotalSpace() / GIBIBYTE;
		double diskPercentage = ((totalDisk - freeDisk) / totalDisk) * 100;
		source.sendFeedback(() -> Text.literal(
			"Disk space used: " +
			new DecimalFormat("#.##").format(totalDisk - freeDisk) + "/" +
			new DecimalFormat("#.##").format(totalDisk) + " GB " +
			"(" + new DecimalFormat("#.##").format(diskPercentage) + "% used)"
		), false);

		double freeRam = Runtime.getRuntime().freeMemory()/1048576;
		double totalRam = Runtime.getRuntime().totalMemory()/1048576;
		double ramPercentage = ((totalRam - freeRam) / totalRam) * 100;
		source.sendFeedback(() -> Text.literal(
			"RAM Used: " +
			new DecimalFormat("#.###").format(totalRam-freeRam) + "/" + new DecimalFormat("#.###").format(totalRam) + " MB " +
			 "(" + new DecimalFormat("#.##").format(ramPercentage) + "% used)"
		), false);

		source.sendFeedback(() -> Text.literal("Number of cores: " + Runtime.getRuntime().availableProcessors()), false);
		source.sendFeedback(() -> Text.literal("Java version: " + System.getProperty("java.version")), false);
		source.sendFeedback(() -> Text.literal("Chunks loaded: " + source.getWorld().getChunkManager().getLoadedChunkCount()), false);
	}

	private static void printColour(ServerCommandSource source)
	{
		double tickTime = source.getServer().getAverageTickTime();
		double tps = 1000.0 / tickTime;
		source.sendFeedback(() -> Text.literal(ChatColour.AQUA + "TPS: " + tpsDisplayString(tps, true)), false);

		source.sendFeedback(() -> Text.literal(ChatColour.AQUA + "OS: " + ChatColour.YELLOW + System.getProperty("os.name")), false);

		File diskSpace = new File(FabricLoader.getInstance().getConfigDir() + "/..");
		double freeDisk = diskSpace.getFreeSpace() / GIBIBYTE;
		double totalDisk = diskSpace.getTotalSpace() / GIBIBYTE;
		double diskPercentage = ((totalDisk - freeDisk) / totalDisk) * 100;
		source.sendFeedback(() -> Text.literal(
			ChatColour.AQUA + "Disk space used: " +
			ChatColour.GREEN + new DecimalFormat("#.##").format(totalDisk-freeDisk) + ChatColour.YELLOW + "/" +	new DecimalFormat("#.##").format(totalDisk) + ChatColour.YELLOW + " GB " +
			"(" + new DecimalFormat("#.##").format(diskPercentage) + "% used)"
		), false);

		double free = Runtime.getRuntime().freeMemory()/1048576;
		double total = Runtime.getRuntime().totalMemory()/1048576;
		double ramPercentage = ((total - free) / total) * 100;
		source.sendFeedback(() -> Text.literal(
			ChatColour.AQUA + "RAM Used: " +
			ChatColour.GREEN + new DecimalFormat("#.###").format(total-free) + ChatColour.YELLOW + "/" + new DecimalFormat("#.###").format(total) + ChatColour.YELLOW + " MB " +
			"(" + new DecimalFormat("#.##").format(ramPercentage) + "% used)"
		), false);

		source.sendFeedback(() -> Text.literal(ChatColour.AQUA + "Number of cores: " + ChatColour.YELLOW + Runtime.getRuntime().availableProcessors()), false);
		source.sendFeedback(() -> Text.literal(ChatColour.AQUA + "Java version: " + ChatColour.YELLOW + System.getProperty("java.version")), false);
		source.sendFeedback(() -> Text.literal(ChatColour.AQUA + "Chunks loaded: " + ChatColour.YELLOW + source.getWorld().getChunkManager().getLoadedChunkCount()), false);
	}
}
