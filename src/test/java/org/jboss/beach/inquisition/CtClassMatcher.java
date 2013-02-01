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
package org.jboss.beach.inquisition;

import javassist.CtClass;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class CtClassMatcher extends BaseMatcher<CtClass> {
    private final String name;

    public CtClassMatcher(final String name) {
        this.name = name;
    }

    public static Matcher<CtClass> ctClassOf(final Class<?> cls) {
        return new CtClassMatcher(cls.getName());
    }

    public static Matcher<CtClass> ctClassNamed(final String name) {
        return new CtClassMatcher(name);
    }

    @Override
    public boolean matches(final Object o) {
        return o instanceof CtClass && ((CtClass) o).getName().equals(name);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("a CtClass named ").appendValue(name);
    }
}
