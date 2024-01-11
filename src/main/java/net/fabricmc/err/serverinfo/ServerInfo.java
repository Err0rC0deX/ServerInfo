package net.fabricmc.err.serverinfo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.err.serverinfo.commands.Commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerInfo implements ModInitializer
{
	public static final String MODID = "serverinfo";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize()
	{
		Commands.initialize();
	}
}
