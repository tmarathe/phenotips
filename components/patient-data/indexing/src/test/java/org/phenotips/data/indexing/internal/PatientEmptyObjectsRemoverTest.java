/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.data.indexing.internal;

import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.context.Execution;
import org.xwiki.context.ExecutionContext;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.observation.EventListener;
import org.xwiki.observation.event.Event;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.ArrayList;
import java.util.List;
import org.mockito.MockitoAnnotations;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.objects.BaseStringProperty;

public class PatientEmptyObjectsRemoverTest {

    @Rule
    public MockitoComponentMockingRule<EventListener> mocker =
        new MockitoComponentMockingRule<EventListener>(PatientEmptyObjectsRemover.class);
    @Mock
    private XWikiContext context;

    @Mock
    private XWiki xWiki;

    @Mock
    private ExecutionContext executionContext;

    private Execution execution;

    private PatientEmptyObjectsRemover patientEmptyObjectsRemover;

    private List<BaseObject> xWikiObjects;

    @Mock
    private XWikiDocument xWikiDocument;

    @Before
    public void setUp() throws ComponentLookupException {

        MockitoAnnotations.initMocks(this);
        this.patientEmptyObjectsRemover = (PatientEmptyObjectsRemover) this.mocker.getComponentUnderTest();

        this.execution = this.mocker.getInstance(Execution.class);

        doReturn(this.executionContext).when(this.execution).getContext();
        doReturn(this.context).when(this.executionContext).getProperty("xwikicontext");
        doReturn(this.xWiki).when(this.context).getWiki();
        xWikiObjects = new ArrayList<BaseObject>();
        doReturn(xWikiObjects).when(xWikiDocument).getXObjects(any(EntityReference.class));
    }

    @Test
    public void emptyObjectRemovedTest() throws XWikiException {

        BaseObject obj = mock(BaseObject.class);
        xWikiObjects.add(obj);
        BaseStringProperty property = mock(BaseStringProperty.class);
        doReturn(property).when(obj).getField(anyString());
        doReturn("").when(property).getValue();

        this.patientEmptyObjectsRemover.onEvent(mock(Event.class), xWikiDocument, mock(Object.class));
        verify(this.xWikiDocument, times(2)).removeXObject((BaseObject) anyObject());
        verify(this.xWiki, times(2)).saveDocument(this.xWikiDocument, "Removed empty object", true, this.context);
    }

    @Test
    public void exitOnNullObjectTest() {
        this.patientEmptyObjectsRemover.onEvent(mock(Event.class), xWikiDocument, mock(Object.class));
        verify(this.xWikiDocument, never()).removeXObject((BaseObject) anyObject());
    }
}
