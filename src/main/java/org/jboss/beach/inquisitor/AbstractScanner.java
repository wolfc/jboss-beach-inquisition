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

import javassist.ClassPool;
import javassist.CtClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
abstract class AbstractScanner<T, E> {
    protected final Inquisitor inquisitor;

    protected AbstractScanner(final Inquisitor inquisitor) {
        this.inquisitor = inquisitor;
    }

    boolean ignore(final String name) {
        if (name.equals("built_using_java_1.6"))
            return true;

        if (name.endsWith(".bak")) // TODO: should not be encountered
            return true;
        if (name.endsWith(".bat"))
            return true;
        if (name.endsWith(".bsh"))
            return true;
        if (name.endsWith(".cert"))
            return true;
        if (name.endsWith(".cfg"))
            return true;
        if (name.endsWith(".conf"))
            return true;
        if (name.endsWith(".css"))
            return true;
        if (name.endsWith(".dat"))
            return true;
        if (name.endsWith(".ddl"))
            return true;
        if (name.endsWith(".dll"))
            return true;
        if (name.endsWith(".dtd"))
            return true;
        if (name.endsWith(".ent"))
            return true;
        if (name.endsWith(".exe"))
            return true;
        if (name.endsWith(".gif"))
            return true;
        if (name.endsWith(".htm"))
            return true;
        if (name.endsWith(".html"))
            return true;
        if (name.endsWith(".iml"))
            return true;
        if (name.endsWith(".java")) // TODO: should not be encountered
            return true;
        if (name.endsWith(".java_")) // TODO: should not be encountered
            return true;
        if (name.endsWith(".jj"))
            return true;
        if (name.endsWith(".jpg"))
            return true;
        if (name.endsWith(".js"))
            return true;
        if (name.endsWith(".jsp"))
            return true;
        if (name.endsWith(".jspx"))
            return true;
        if (name.endsWith(".key"))
            return true;
        if (name.endsWith(".keystore"))
            return true;
        if (name.endsWith(".log"))
            return true;
        if (name.endsWith(".lst"))
            return true;
        if (name.endsWith(".mappings"))
            return true;
        if (name.endsWith(".MF"))
            return true;
        if (name.endsWith(".png"))
            return true;
        if (name.endsWith(".policy"))
            return true;
        if (name.endsWith(".properties"))
            return true;
        if (name.endsWith(".py"))
            return true;
        if (name.endsWith(".rej"))
            return true;
        if (name.endsWith(".res"))
            return true;
        if (name.endsWith(".rng"))
            return true;
        if (name.endsWith(".RSA"))
            return true;
        if (name.endsWith(".rsc"))
            return true;
        if (name.endsWith(".sav")) // TODO: should not be encountered
            return true;
        if (name.endsWith(".ser"))
            return true;
        if (name.endsWith(".sh"))
            return true;
        if (name.endsWith(".src"))
            return true;
        if (name.endsWith(".sql"))
            return true;
        if (name.endsWith(".swf"))
            return true;
        if (name.endsWith(".tasks"))
            return true;
        if (name.endsWith(".template"))
            return true;
        if (name.endsWith(".text"))
            return true;
        if (name.endsWith(".tld"))
            return true;
        if (name.endsWith(".tmp"))
            return true;
        if (name.endsWith(".truststore"))
            return true;
        if (name.endsWith(".txt"))
            return true;
        if (name.endsWith(".xcss"))
            return true;
        if (name.endsWith(".xhtml"))
            return true;
        if (name.endsWith(".xjb"))
            return true;
        if (name.endsWith(".xml"))
            return true;
        if (name.endsWith(".xsd"))
            return true;
        if (name.endsWith(".xsl"))
            return true;
        if (name.endsWith(".xslt"))
            return true;
        if (name.endsWith(".xsdconfig"))
            return true;
        if (name.endsWith(".vm"))
            return true;
        if (name.endsWith(".wsdl"))
            return true;
        if (name.endsWith("COPYRIGHT"))
            return true;
        if (name.endsWith("VERSION"))
            return true;
        if (name.endsWith("KILL_ME")) // TODO: should not be encountered
            return true;
        if (name.endsWith("packageinfo"))
            return true;
        if (name.endsWith("README"))
            return true;

        if (name.startsWith("org/joda/time/tz/data"))
            return true;
        if (name.startsWith("LGPL"))
            return true;
        if (name.startsWith("LICENSE"))
            return true;
        if (name.startsWith("META-INF/"))
            return true;
        if (name.startsWith("NOTICE"))
            return true;
        if (name.startsWith("1.0"))
            return true;
        return false;
    }

    abstract void scan(final URL codeSourceLocation, final T target) throws IOException;

    void scanClass(final URL codeSourceLocation, final InputStream in) throws IOException {
        final ClassPool pool = new ClassPool();
        CtClass ctClass = pool.makeClass(in);
        inquisitor.interrogate(codeSourceLocation, ctClass);
    }
}
