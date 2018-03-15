package mousquetaires.languages.converters.toytree;

import mousquetaires.languages.ProgramLanguage;
import mousquetaires.languages.parsers.C11Lexer;
import mousquetaires.languages.parsers.C11Parser;
import mousquetaires.utils.io.FileUtils;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;
import java.io.IOException;


class InputProgramParserFactory {

    static ParserRuleContext getParser(File programFile, ProgramLanguage language) throws IOException {
        CharStream charStream = FileUtils.getFileCharStream(programFile);

        switch (language) {
            case C11: {
                C11Lexer lexer = new C11Lexer(charStream);
                CommonTokenStream tokenStream = new CommonTokenStream(lexer);
                C11Parser parser = new C11Parser(tokenStream);
                return parser.main();
            }
            //case Porthos: {
            //    PorthosLexer lexer = new PorthosLexer(charStream);
            //    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            //    PorthosParser parser = new PorthosParser(tokenStream);
            //    return parser.main();
            //}
            //case Litmus: {
            //    LitmusLexer lexer = new LitmusLexer(charStream);
            //    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            //    return new LitmusParser(tokenStream)    .program(programFile.getProcessName()).p;
            //}
            default:
                throw new IllegalArgumentException(language.name());
        }
    }
}