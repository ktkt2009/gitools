/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.obo;

import java.lang.reflect.Field;

public class OBOEvent implements OBOEventTypes {

	private int type;
	private String stanzaName;
	private String tagName;
	private String tagContents;

	protected int linePos;

	public OBOEvent(int type, int line) {
		this.type = type;
		this.linePos = line;
	}

	public OBOEvent(int type, int line, String stanzaName) {
		this.type = type;
		this.linePos = line;
		this.stanzaName = stanzaName;
	}

	public OBOEvent(int type, int line, String stanzaName, String tagName) {
		this.type = type;
		this.linePos = line;
		this.stanzaName = stanzaName;
		this.tagName = tagName;
	}

	public OBOEvent(int type, int line, String stanzaName, String tagName, String tagContents) {
		this.type = type;
		this.linePos = line;
		this.stanzaName = stanzaName;
		this.tagName = tagName;
		this.tagContents = tagContents;
	}

	public int getType() {
		return type;
	}

	public int getLinePos() {
		return linePos;
	}
	
	public String getStanzaName() {
		return stanzaName;
	}

	public String getTagName() {
		return tagName;
	}

	public String getTagContents() {
		return tagContents;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		try {
			for (Field f : OBOEventTypes.class.getFields()) {
				if (f.getInt(null) == type) {
					sb.append(f.getName());
					break;
				}
			}
		}
		catch (Exception ex) {}

		if (sb.length() == 0)
			sb.append(type);

		sb.append(" ");

		switch (type) {
			case STANZA_START: sb.append(stanzaName); break;
			case TAG_START: sb.append(tagName); break;
		}

		return sb.toString();
	}
}