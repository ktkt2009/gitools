/*
 *  Copyright 2010 chris.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.cli;

import edu.upf.bg.tools.exception.ToolException;
import java.util.ArrayList;
import java.util.List;

public class TestMain {

	public static void main(String[] unusedArgs) throws ToolException {
		String cmd = "enrichment -t binomial-exact" +
				" -tc sample-size=100 -tc aproximation=none" +
				" -d /home/chris/temp/gitools-stuff/data.tsv -b gt,0.5" +
				" -m /home/chris/temp/gitools-stuff/modules.tsv" +
				" -w /home/chris/temp/gitools-temp/test -N test" +
				" -title 'Test Analysis'" +
				" -notes 'Testing...'" +
				" -A author=Christian" +
				" -verbose -debug -err-log -";

		String[] args = cmdLineSplit(cmd);

		Main.main(args);
	}

	@SuppressWarnings("empty-statement")
	private static String[] cmdLineSplit(String cmd) {
		List<String> args = new ArrayList<String>();

		int lastPos = 0;
		int pos = 0;
		while (pos < cmd.length()) {
			char ch = cmd.charAt(pos);
			switch (ch) {
				case ' ':
					args.add(cmd.substring(lastPos, pos));
					while (pos < cmd.length() && cmd.charAt(pos++) != ch);
					lastPos = pos;
					break;
				case '"':
				case '\'':
					lastPos = ++pos;
					while (pos < cmd.length() && cmd.charAt(pos++) != ch);
					args.add(cmd.substring(lastPos, pos - 1));
					while (pos < cmd.length() && cmd.charAt(pos) == ' ') pos++;
					lastPos = pos;
					break;

				default: pos++;	break;
			}
		}
		
		if (lastPos < cmd.length())
			args.add(cmd.substring(lastPos, pos));
		
		String[] retArgs = new String[args.size()];
		args.toArray(retArgs);
		return retArgs;
	}
}