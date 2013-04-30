/*
 * #%L
 * gitools-ui-platform
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
package org.gitools.ui.platform.imageviewer;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.EventObject;

/**
 * An event indicating that a mouse action occured over an image.
 * @author Kazó Csaba
 */
public class ImageMouseEvent extends EventObject {
	private BufferedImage image;
	private int x,y;
	private MouseEvent orig;
	public ImageMouseEvent(Object source, BufferedImage image, int x, int y, MouseEvent orig) {
		super(source);
		this.image=image;
		this.x=x;
		this.y=y;
		this.orig=orig;
	}
	/**
	 * Returns the image on which the event occured.
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}
	/**
	 * Returns the x coordinate of the pixel related to the event.
	 * @return the x coordinate of the pixel related to the event
	 */
	public int getX() {
		return x;
	}
	/**
	 * Returns the y coordinate of the pixel related to the event.
	 * @return the y coordinate of the pixel related to the event
	 */
	public int getY() {
		return y;
	}
	/**
	 * Returns the mouse event that caused this image mouse event. This can occasionally be <code>null</code>, for
	 * example for the exit event fired when the image is set to <code>null</code>.
	 * @return the original event
	 */
	public MouseEvent getOriginalEvent() {
		return orig;
	}
	
}
