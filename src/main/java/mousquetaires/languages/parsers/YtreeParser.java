package mousquetaires.languages.parsers;

import mousquetaires.languages.ProgramLanguage;
import mousquetaires.languages.converters.toytree.InputProgramToYtreeConverter;
import mousquetaires.languages.converters.toytree.InputProgramConvertersFactory;
import mousquetaires.languages.syntax.ytree.YSyntaxTree;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;
import java.io.IOException;


public class YtreeParser {

    public static YSyntaxTree parse(File inputProgramFile, ProgramLanguage language) throws IOException {

        InputProgramToYtreeConverter transformer = InputProgramConvertersFactory.getYtreeConverter(language);
        ParserRuleContext parserEntryPoint = InputProgramParserFactory.getParser(inputProgramFile, language);
        return transformer.convert(parserEntryPoint);
    }
}
