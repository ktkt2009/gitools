/*
 *  Copyright 2009 cperez.
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

package org.gitools.ui.view.entity;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.gitools.ui.view.AbstractView;

//TODO allow definition of a <Class, Controller> map from outside
public abstract class EntityView extends AbstractView {

	private JScrollPane scrollPane;

	private Map<Class<?>, EntityController> controllerCache;

	public EntityView() {
		controllerCache = new HashMap<Class<?>, EntityController>();

		createComponents();
	}

	private void createComponents() {
		scrollPane = new JScrollPane();

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	public void update(Object context) {
		EntityController controller = controllerCache.get(context.getClass());
		if (controller == null) {
			controller = createController(context);
			controllerCache.put(context.getClass(), controller);
		}

		JComponent component = controller.getComponent(context);
		component.setBorder(null);
		scrollPane.setViewportView(component);
	}

	protected abstract EntityController createController(Object context);
}
