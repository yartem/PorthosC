package mousquetaires.languages;

import mousquetaires.languages.cmin.transformers.tointernal.CminToYtreeTransformer;
import mousquetaires.utils.exceptions.NotImplementedException;


public class TransformerFactory {
    public static ProgramToYtreeTransformer getTransformer(ProgramLanguage inputLanguage) {
        switch (inputLanguage) {
            case Cmin:
                return new CminToYtreeTransformer();
            //case Porthos:
            //    return new PorthosToYtreeTransformer();
            case Litmus:
                throw new NotImplementedException();
            default:
                throw new IllegalArgumentException("Unsupported language: " + inputLanguage.toString());
        }
    }
}
