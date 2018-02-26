package mousquetaires.tests.unit.languages.converters.tozformula;

import mousquetaires.languages.ProgramExtensions;
import mousquetaires.languages.ProgramLanguage;
import mousquetaires.languages.converters.toxgraph.YtreeToXgraphConverter;
import mousquetaires.languages.parsers.YtreeParser;
import mousquetaires.languages.processors.encoders.xgraph.tosmt.XProgramToZ3Encoder;
import mousquetaires.languages.syntax.smt.ZBoolFormula;
import mousquetaires.languages.syntax.xgraph.XProgram;
import mousquetaires.languages.syntax.xgraph.datamodels.DataModel;
import mousquetaires.languages.syntax.ytree.YSyntaxTree;
import mousquetaires.tests.TestFailedException;
import mousquetaires.tests.unit.languages.converters.AbstractConverterUnitTest;
import mousquetaires.utils.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public abstract class AbstractZformulaUnitTest extends AbstractConverterUnitTest<ZBoolFormula> {

    @Override
    protected Iterator<? extends ZBoolFormula> parseTestFile(String testFile) {
        try {
            DataModel dataModel = null; // TODO: consider data model also
            File file = new File(testFile);
            ProgramLanguage language = ProgramExtensions.parseProgramLanguage(file.getName());
            YSyntaxTree internalRepr = YtreeParser.parse(file, language);
            YtreeToXgraphConverter converter = new YtreeToXgraphConverter(language, dataModel);
            XProgram program = converter.convert(internalRepr);
            XProgramToZ3Encoder encoder = new XProgramToZ3Encoder();
            ZBoolFormula smtFormula = encoder.encode(program);
            return CollectionUtils.createIteratorFrom(smtFormula);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TestFailedException(e);
        }
    }

    @Override
    protected void assertObjectsEqual(ZBoolFormula expected, ZBoolFormula actual) {
        // todo: add helper to the utils 'compare trees'
    }


}