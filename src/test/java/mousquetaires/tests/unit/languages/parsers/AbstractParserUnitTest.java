package mousquetaires.tests.unit.languages.parsers;

import mousquetaires.languages.ProgramExtensions;
import mousquetaires.languages.ProgramLanguage;
import mousquetaires.languages.parsers.YtreeParser;
import mousquetaires.languages.syntax.ytree.YSyntaxTree;
import mousquetaires.tests.AbstractTest;
import mousquetaires.tests.TestFailedException;

import java.io.File;
import java.io.IOException;


public abstract class AbstractParserUnitTest extends AbstractTest {

    protected final String parsersDirectory = resourcesDirectory + "parsers/";

    protected YSyntaxTree runTest(String filePath) {
        YSyntaxTree internalRepr;
        try {
            File file = new File(filePath);
            ProgramLanguage language = ProgramExtensions.parseProgramLanguage(file.getName());
            return YtreeParser.parse(file, language);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TestFailedException(e);
        }
    }
}
