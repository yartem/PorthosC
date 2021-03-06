package porthosc.tests.unit.languages.converters;

import porthosc.tests.unit.AbstractUnitTest;
import porthosc.utils.CollectionUtils;

import java.util.Iterator;


public abstract class AbstractConverterUnitTest<TElement> extends AbstractUnitTest<TElement> {

protected abstract Iterator<? extends TElement> parseTestFile(String testFile);

    protected void run(String testFile,
                       Iterator<? extends TElement> expectedResultIterator) {
        Iterator<? extends TElement> actualResultIterator = parseTestFile(testFile);
        assertIteratorsEqual(expectedResultIterator, actualResultIterator);
    }

    // TODO: fix heap pollution
    protected Iterator<? extends TElement> getIterator(TElement... values) {
        return CollectionUtils.createIteratorFrom(values);
    }
}
