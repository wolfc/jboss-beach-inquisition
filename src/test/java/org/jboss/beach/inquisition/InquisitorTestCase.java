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
import org.jboss.beach.inquisitor.Inquisitor;
import org.jboss.beach.inquisitor.interrogations.FinalizeInterrogation;
import org.junit.Test;
import org.mockito.Matchers;

import java.net.URL;

import static org.jboss.beach.inquisition.CtClassMatcher.ctClassOf;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class InquisitorTestCase {
    static class TestFinalizeInterrogation extends FinalizeInterrogation {
        @Override
        public void report(final Inquisitor inquisitor, final URL location, final CtClass ctClass) {
            super.report(inquisitor, location, ctClass);
        }
    };

    @Test
    public void test1() throws Exception {
        final TestFinalizeInterrogation interrogation = mock(TestFinalizeInterrogation.class, CALLS_REAL_METHODS);

        final Inquisitor inquisitor = new Inquisitor();
        inquisitor.getInterrogations().add(interrogation);
        final URL location = InquisitorTestCase.class.getProtectionDomain().getCodeSource().getLocation();
        inquisitor.inquire(location);

        verify(interrogation, atLeast(1)).interrogate(eq(inquisitor), eq(location), Matchers.<CtClass>any());
        verify(interrogation).report(eq(inquisitor), eq(location), argThat(ctClassOf(Offender.class)));
        verifyNoMoreInteractions(interrogation);
    }
}
