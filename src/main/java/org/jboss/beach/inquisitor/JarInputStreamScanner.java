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

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import static org.jboss.beach.inquisitor.Inquisitor.isArchive;
import static org.jboss.beach.inquisitor.LoggerHelper.getLogger;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class JarInputStreamScanner extends AbstractScanner<JarInputStream, JarEntry> {
    private static final Logger LOG = getLogger(JarInputStreamScanner.class);

    JarInputStreamScanner(final Inquisitor inquisitor) {
        super(inquisitor);
    }

    @Override
    void scan(final URL codeSourceLocation, final JarInputStream jar) throws IOException {
        ZipEntry entry;
        while((entry = jar.getNextEntry()) != null) {
            final String name = entry.getName();
            if (entry.isDirectory()) {
                // ignore
            } else if (name.endsWith(".class")) {
                scanClass(codeSourceLocation, jar);
            } else if (isArchive(name)) {
                // TODO: this does not callback into inquisitor
                // this is not a real URL!
                final URL childCodeSourceLocation = new URL(codeSourceLocation, name);
                final JarInputStream child = new JarInputStream(jar);
                scan(childCodeSourceLocation, child);
            } else if (ignore(name)) {
                //System.err.println("ignore jar entry " + entry);
            } else
                LOG.warning("unknown resource " + codeSourceLocation + " / " + name);
        }
    }
}
