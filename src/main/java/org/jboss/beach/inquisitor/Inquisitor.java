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

import javassist.CtClass;
import javassist.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The inquisitor will interrogate at a location to find victims to interrogate.
 *
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class Inquisitor {
    // use scanners to find classes
    private final DirectoryScanner directoryScanner = new DirectoryScanner(this);
    private final JarScanner jarScanner = new JarScanner(this);

    private List<Interrogation> interrogations = new CopyOnWriteArrayList<Interrogation>();

    public List<Interrogation> getInterrogations() {
        return interrogations;
    }

    public void inquire(final URL codeSourceLocation) throws IOException {
        final File target = new File(uri(codeSourceLocation));
        if (target.isDirectory())
            directoryScanner.scan(codeSourceLocation, target);
        else if (isArchive(target.getName())) {
            jarScanner.scan(codeSourceLocation, target);
        } else
            throw new RuntimeException("NYI " + codeSourceLocation);
    }

    public void interrogate(final URL codeSourceLocation, final CtClass ctClass) {
        for (final Interrogation interrogation : interrogations) {
            interrogation.interrogate(this, codeSourceLocation, ctClass);
        }
    }

    static boolean isArchive(final String name) {
        return name.endsWith(".ear") || name.endsWith(".jar") || name.endsWith(".sar") || name.endsWith(".war");
    }

    public static void main(final String[] args) throws IOException {
        final URL base = new URL(args[0]);
        final Inquisitor inquisitor = new Inquisitor();
        inquisitor.getInterrogations().add(new Interrogation() {
            @Override
            public void interrogate(final Inquisitor inquisitor, final URL location, final CtClass ctClass) {
                try {
                    ctClass.getDeclaredMethod("finalize");
                    System.out.println(ctClass.getName() + " in " + location);
                } catch (NotFoundException e) {
                    // ignore
                }
            }
        });
        inquisitor.inquire(base);
    }

    private static URI uri(final URL codeSourceLocation) {
        try {
            return codeSourceLocation.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
