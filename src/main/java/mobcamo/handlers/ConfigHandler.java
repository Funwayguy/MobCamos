package mobcamo.handlers;

import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.Level;
import mobcamo.core.MobCamo;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler
{
	public static void LoadConfigs(File file)
	{
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch(IOException e)
			{
				MobCamo.logger.log(Level.ERROR, "An error occered while creating config file. Game may crash", e);
			}
		}
		
		Configuration config = new Configuration(file);
		
		config.save();
	}
}
