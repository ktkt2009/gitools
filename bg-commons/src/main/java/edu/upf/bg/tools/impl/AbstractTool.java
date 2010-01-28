package edu.upf.bg.tools.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.upf.bg.tools.ToolLifeCycle;
import edu.upf.bg.tools.args.BaseArguments;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolUsageException;
import edu.upf.bg.tools.exception.ToolValidationException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class AbstractTool implements ToolLifeCycle {

	@Override
	public void initialize() throws ToolException {
	}

	@Override
	public void validate(Object argsObject) throws ToolException {
		if (!(argsObject instanceof BaseArguments))
			return;
		
		BaseArguments args = (BaseArguments) argsObject;
		if (args.loglevel != null) {
			Pattern pat = Pattern.compile("^(.*)=(.*)$");
			
			for (String loglevel : args.loglevel) {
				Matcher mat = pat.matcher(loglevel);
				if (!mat.matches() || mat.groupCount() != 2)
					throw new ToolValidationException("Invalid -loglevel argument: " + loglevel);
				
				final String pkg = mat.group(1);
				final String levelName = mat.group(2);
				if (pkg == null || levelName == null)
					throw new ToolValidationException("Invalid -loglevel package: " + loglevel);

				final Level level = Level.toLevel(levelName);
				if (level == null)
					throw new ToolValidationException("Invalid -loglevel level name: " + loglevel);
				
				Logger.getLogger(pkg).setLevel(level);
			}
		}
		
		if (args.help)
			throw new ToolUsageException();
	}
	
	@Override
	public void run(Object argsObject) throws ToolException {
		/*if (!(argsObject instanceof BaseArguments))
			return;
		
		BaseArguments args = (BaseArguments) argsObject;*/
	}

	@Override
	public void uninitialize() throws ToolException {
	}
}