package mousquetaires.languages.syntax.wmodel;

import mousquetaires.languages.common.citation.Origin;
import mousquetaires.languages.syntax.wmodel.visitors.WmodelVisitor;
import mousquetaires.utils.exceptions.NotImplementedException;


public class WMemoryModel implements WEntity {

    @Override
    public boolean containsRecursion() {
        throw new NotImplementedException();
    }

    @Override
    public <T> T accept(WmodelVisitor<T> visitor) {
        throw new NotImplementedException();
    }

    @Override
    public Origin origin() {
        throw new NotImplementedException();
    }
}
