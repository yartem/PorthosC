package mousquetaires.languages.syntax.ytree.specific;

import mousquetaires.languages.syntax.ytree.YEntity;
import mousquetaires.languages.syntax.ytree.statements.YCompoundStatement;
import mousquetaires.languages.syntax.ytree.statements.YUnlabeledStatement;
import mousquetaires.languages.syntax.ytree.visitors.ytree.YtreeVisitor;
import mousquetaires.utils.CollectionUtils;

import java.util.Iterator;


public final class YPreludeStatement extends YUnlabeledStatement {

    private final YCompoundStatement body;

    public YPreludeStatement(YCompoundStatement body) {
        this.body = body;
    }

    public YCompoundStatement getBody() {
        return body;
    }

    @Override
    public Iterator<? extends YEntity> getChildrenIterator() {
        return CollectionUtils.createIteratorFrom(body);
    }

    @Override
    public <T> T accept(YtreeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
