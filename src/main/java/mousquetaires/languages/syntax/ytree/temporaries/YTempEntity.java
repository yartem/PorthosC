package mousquetaires.languages.syntax.ytree.temporaries;

import mousquetaires.languages.common.citation.CodeLocation;
import mousquetaires.languages.syntax.ytree.YEntity;
import mousquetaires.languages.syntax.ytree.visitors.ytree.YtreeVisitor;


public interface YTempEntity extends YEntity {

    @Override
    default <T> T accept(YtreeVisitor<T> visitor) {
        throw new UnsupportedOperationException();
    }

    @Override
    default CodeLocation codeLocation() {
        throw new UnsupportedOperationException();
    }
}
