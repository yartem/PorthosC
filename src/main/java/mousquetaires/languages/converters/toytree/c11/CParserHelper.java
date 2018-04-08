package mousquetaires.languages.converters.toytree.c11;

import mousquetaires.languages.parsers.C11Parser;
import org.antlr.v4.runtime.ParserRuleContext;


public class CParserHelper {
    // TODO: check this manually-restored method
    public static boolean hasToken(ParserRuleContext ctx, int tokenType) {
        return ctx.getTokens(tokenType).size() > 0;
    }

    // TODO: check this manually-restored method
    public static boolean hasParentheses(ParserRuleContext ctx) {
        return hasToken(ctx, C11Parser.LeftParen) && hasToken(ctx, C11Parser.RightParen);
    }
}