package mousquetaires.languages.syntax.wmodel.operators;

import mousquetaires.languages.common.citation.Origin;
import mousquetaires.languages.syntax.wmodel.WEntityBase;
import mousquetaires.languages.syntax.wmodel.WOperator;


public abstract class WOperatorBase extends WEntityBase implements WOperator {

    public final WOperator.Kind kind;

    WOperatorBase(Origin origin, boolean containsRecursion, WOperator.Kind kind) {
        super(origin, containsRecursion);
        this.kind = kind;
    }

    public WOperator.Kind getKind() {
        return kind;
    }
}
