/*
 *  Copyright 2009 chris.
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
package org.gitools.biomart;

import org.gitools.biomart.settings.BiomartSource;

public class BiomartConfiguration {

    private String restUrl;
    private String version;
    public String serviceNamespace;
    public String servicePortName;
    private String wdslUrl;
    public String serviceName;


    public BiomartConfiguration(String wdslUrl, String restUrl) {
        this.wdslUrl = wdslUrl;
        this.restUrl = restUrl;
    }

    public BiomartConfiguration(BiomartSource bs)
    {
        this.wdslUrl = bs.getWsdlLocation();
        this.restUrl = bs.getServiceRest();
        this.version = bs.getVersion();
        this.serviceName = bs.getServiceName();
        this.serviceNamespace = bs.getServiceNamespace();
        this.servicePortName = bs.getServicePortName();
    }
    
    public String getServiceName() {
        return serviceName;
    }

    public String getServiceNamespace() {
        return serviceNamespace;
    }

    public String getServicePortName() {
        return servicePortName;
    }


    public String getWdslUrl() {
        return wdslUrl;
    }

    public String getRestUrl() {
        return restUrl;
    }

    public String getVersion() {
        return version;
    }
}
