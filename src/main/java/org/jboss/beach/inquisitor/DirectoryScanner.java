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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import static org.jboss.beach.inquisitor.LoggerHelper.getLogger;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
class DirectoryScanner extends AbstractScanner<File, String> {
    private static final Logger LOG = getLogger(DirectoryScanner.class);

    DirectoryScanner(final Inquisitor inquisitor) {
        super(inquisitor);
    }

    @Override
    void scan(final URL codeSourceLocation, final File dir) throws IOException {
        assert dir.isDirectory() : dir + " is not a directory";
        for (final String entry : dir.list()) {
            final File child = new File(dir, entry);
            final String name = child.getName();
            if (child.isDirectory())
                scan(codeSourceLocation, child);
            else if (name.endsWith(".class"))
                scanClass(codeSourceLocation, child);
            else if (Inquisitor.isArchive(name)) {
                inquisitor.inquire(child.toURI().toURL());
            } else if (ignore(name)) {
                //System.err.println("ignore " + child);
            } else
                LOG.warning("unknown resource " + dir + " / " + name);
        }
    }

    private void scanClass(final URL codeSourceLocation, final File classFile) throws IOException {
        final InputStream in = new BufferedInputStream(new FileInputStream(classFile));
        try {
            scanClass(codeSourceLocation, in);
        } finally {
            in.close();
        }
    }
}
