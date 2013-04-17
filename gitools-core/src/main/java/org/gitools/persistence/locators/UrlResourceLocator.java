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
package org.gitools.persistence.locators;

import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlResourceLocator implements IResourceLocator {

    private static final Pattern ABSOLUTE_FILE_PATH = Pattern.compile("^(\\/.*|[a-zA-Z]\\:\\\\)");
    private static final Pattern ABSOLUTE_REMOTE_URL = Pattern.compile("[a-zA-Z]+:\\/\\/.*");

    private URL url;
    @Nullable
    private File file;

    public UrlResourceLocator(@NotNull File file) throws PersistenceException {
        this.file = file;
        try {
            this.url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new PersistenceException(e);
        }
    }

    public UrlResourceLocator(String url) throws PersistenceException {
        try {
            if (ABSOLUTE_REMOTE_URL.matcher(url).matches()) {
                this.file = null;
                this.url = new URL(url);

                if (this.url.getProtocol().equals("file")) {
                    this.file = new File(this.url.toURI());
                }

            } else {
                this.file = new File(url);
                this.url = file.toURI().toURL();
            }
        } catch (MalformedURLException e) {
            throw new PersistenceException(e);
        } catch (URISyntaxException e) {
            throw new PersistenceException(e);
        }

    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public String getBaseName() {
        String name = getName();

        int lastDot = name.lastIndexOf('.');
        if (lastDot != -1) {
            name = name.substring(0, lastDot);
        }

        return name;
    }

    @Override
    public String getName() {
        String name = url.toString();
        return name.substring(name.lastIndexOf('/') + 1);
    }

    @Override
    public String getExtension() {
        String name = getName();
        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public boolean isWritable() {
        return file != null;
    }

    @NotNull
    @Override
    public IResourceLocator getReferenceLocator(String referenceName) throws PersistenceException {

        // An absolute remote file
        if (ABSOLUTE_REMOTE_URL.matcher(referenceName).matches()) {
            return new UrlResourceLocator(referenceName);
        }

        // A relative path to a remote URL
        if (file == null) {
            String parentUrl = url.toString();
            parentUrl = parentUrl.substring(0, parentUrl.lastIndexOf('/'));
            return new UrlResourceLocator(parentUrl + '/' + referenceName);
        }

        // We allow absolute path references on local files
        if (ABSOLUTE_FILE_PATH.matcher(referenceName).matches()) {
            return new UrlResourceLocator(new File(referenceName));
        }

        // A relative path to the local file
        return new UrlResourceLocator(new File(file.getParentFile(), referenceName));
    }

    @Nullable
    @Override
    public InputStream openInputStream() throws IOException {

        if (file == null) {
            return url.openStream();
        }

        return new FileInputStream(file);
    }

    @Nullable
    @Override
    public OutputStream openOutputStream() throws IOException {

        if (!isWritable()) {
            throw new UnsupportedOperationException("Write to '" + url + "' is not supported.");
        }

        return new FileOutputStream(file);
    }


}
