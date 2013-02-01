/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.beach.inquisitor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;

import static org.jboss.beach.inquisitor.Inquisitor.isArchive;
import static org.jboss.beach.inquisitor.LoggerHelper.getLogger;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class JarScanner extends AbstractScanner<File, JarEntry> {
    private static final Logger LOG = getLogger(JarScanner.class);
    private final JarInputStreamScanner jarInputStreamScanner;

    JarScanner(final Inquisitor inquisitor) {
        super(inquisitor);
        jarInputStreamScanner = new JarInputStreamScanner(inquisitor);
    }

    @Override
    void scan(final URL codeSourceLocation, final File file) throws IOException {
        final JarFile jarFile = new JarFile(file);
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String name = entry.getName();
            if (entry.isDirectory()) {
                // ignore
            } else if (name.endsWith(".class")) {
                final InputStream in = jarFile.getInputStream(entry);
                try {
                    scanClass(codeSourceLocation, in);
                } finally {
                    in.close();
                }
            } else if (isArchive(name)) {
                final JarInputStream in = new JarInputStream(jarFile.getInputStream(entry));
                try {
                    jarInputStreamScanner.scan(new URL(codeSourceLocation, name), in);
                } finally {
                    in.close();
                }
            } else if (ignore(name)) {
                //System.err.println("ignore jar entry " + entry);
            } else
                LOG.warning("unknown resource " + codeSourceLocation + " / " + name);
        }
    }
}
