/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.clustering;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gitools.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.clustering.method.value.WekaCobWebMethod;
import org.gitools.clustering.method.value.WekaHCLMethod;
import org.gitools.clustering.method.value.WekaKmeansMethod;

public class ClusteringMethodFactory {

	private static final ClusteringMethodDescriptor[] DEFAULT_DESCRIPTORS = new ClusteringMethodDescriptor[] {
		new ClusteringMethodDescriptor(
				"Clustering from annotations",
				"Cluster data instances according to a set of selected annotations",
				AnnPatClusteringMethod.class),
		new ClusteringMethodDescriptor(
				"Agglomerative hierarchical clustering",
				"Cluster data instances according to classic agglomerative hierarchical clustering method",
				WekaHCLMethod.class),
		new ClusteringMethodDescriptor(
				"K-means clustering",
				"Cluster data instances according to k-means clustering method",
				WekaKmeansMethod.class),
		new ClusteringMethodDescriptor(
				"Cobweb clustering",
				"Cluster data instances according to cobweb clustering method",
				WekaCobWebMethod.class)
	};

	private static ClusteringMethodFactory instance;

	private List<ClusteringMethodDescriptor> descriptors;

	private ClusteringMethodFactory() {
		descriptors = new ArrayList<ClusteringMethodDescriptor>();
		registerMethods(DEFAULT_DESCRIPTORS);
	}

	public static ClusteringMethodFactory getDefault() {
		if (instance == null)
			instance = new ClusteringMethodFactory();
		return instance;
	}

	public List<ClusteringMethodDescriptor> getDescriptors() {
		return descriptors;
	}

	public final void registerMethods(ClusteringMethodDescriptor[] descriptors) {
		this.descriptors.addAll(Arrays.asList(descriptors));
	}

	public ClusteringMethod create(ClusteringMethodDescriptor descriptor) {
		Class<? extends ClusteringMethod> methodClass = descriptor.getMethodClass();
		
		try {
			Constructor<? extends ClusteringMethod> c = methodClass.getConstructor();
			return c.newInstance();
		}
		catch (Exception e) {
			return null;
		}
	}
}
