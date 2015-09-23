package org.phenotips.vocabulary.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.phenotips.vocabulary.Vocabulary;
import org.phenotips.vocabulary.VocabularyManager;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class DefaultVocabularyManagerTest {

    @Rule
    public MockitoComponentMockingRule<VocabularyManager> mocker =
            new MockitoComponentMockingRule<VocabularyManager>(DefaultVocabularyManager.class);

    private Map<String, Vocabulary> vocabularies;

    private DefaultVocabularyManager defaultVocabularyManager;

    @Before
    public void setUp() throws ComponentLookupException {
        MockitoAnnotations.initMocks(this);
        this.vocabularies = this.mocker.getInstance(HashMap.class);
        this.defaultVocabularyManager = (DefaultVocabularyManager) this.mocker.getComponentUnderTest();
    }

    @Test
    public void resolveBlankTermTest() {
        String testTermID = ":";
        Assert.assertNull(this.defaultVocabularyManager.resolveTerm(testTermID));
    }

}
