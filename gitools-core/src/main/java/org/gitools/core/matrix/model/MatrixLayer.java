/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.core.matrix.model;

import com.jgoodies.binding.beans.Model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class MatrixLayer extends Model implements IMatrixLayer {

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_VALUE_CLASS = "valueClass";
    public static final String PROPERTY_DESCRIPTION_URL = "descriptionUrl";
    public static final String PROPERTY_VALUE_URL = "valueUrl";

    private String id;
    private String name;
    private String description;

    @XmlElement(name = "description-link")
    private String descriptionUrl;

    @XmlElement(name = "value-link")
    private String valueUrl;

    @XmlElement(name = "value-type")
    @XmlJavaTypeAdapter(ClassXmlAdapter.class)
    private Class<?> valueClass;

    public MatrixLayer() {
        // JAXB requirement
    }

    public MatrixLayer(String id, Class<?> valueClass) {
        this(id, valueClass, null, null);
    }

    public MatrixLayer(String id, Class<?> valueClass, String name, String description) {
        this.id = id;
        this.valueClass = valueClass;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        if (name == null) {
            return id;
        }

        return name;
    }

    @Override
    public String getDescription() {
        if (description == null) {
            return "";
        }

        return description;
    }

    @Override
    public Class<?> getValueClass() {
        return valueClass;
    }

    public void setDescription(String description) {
        String oldValue = this.description;
        this.description = description;
        firePropertyChange(PROPERTY_DESCRIPTION, oldValue, description);
    }

    public void setId(String id) {
        String old = this.id;
        this.id = id;
        firePropertyChange(PROPERTY_ID, old, id);
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        firePropertyChange(PROPERTY_NAME, old, name);
    }

    public void setValueClass(Class<?> valueClass) {
        Class old = this.valueClass;
        this.valueClass = valueClass;
        firePropertyChange(PROPERTY_VALUE_CLASS, old, valueClass);
    }

    public String getDescriptionUrl() {
        return descriptionUrl;
    }

    public void setDescriptionUrl(String descriptionUrl) {
        String old = this.descriptionUrl;
        this.descriptionUrl = descriptionUrl;
        firePropertyChange(PROPERTY_DESCRIPTION_URL, old, descriptionUrl);
    }

    public String getValueUrl() {
        return valueUrl;
    }

    public void setValueUrl(String valueUrl) {
        String old = this.valueUrl;
        this.valueUrl = valueUrl;
        firePropertyChange(PROPERTY_VALUE_URL, old, valueUrl);
    }
}
