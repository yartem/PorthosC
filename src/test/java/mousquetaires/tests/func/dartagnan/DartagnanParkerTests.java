package mousquetaires.tests.func.dartagnan;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import mousquetaires.app.modules.dartagnan.DartagnanVerdict;
import mousquetaires.tests.func.FuncTestsBase;
import mousquetaires.models.MemoryModelName;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;


@RunWith(ZohhakRunner.class)
public class DartagnanParkerTests extends FuncTestsBase {

    private final String parker_pts_rx = targetsDirectory + "/all_rx/parker.pts";
    private final String parker_pts_sc = targetsDirectory + "/all_sc/parker.pts";

    @TestWith({
            parker_pts_rx + ", " + "SC,    Reachable",
            parker_pts_rx + ", " + "TSO,   Reachable",
            parker_pts_rx + ", " + "PSO,   Reachable",
            parker_pts_rx + ", " + "RMO,   Reachable",
            parker_pts_rx + ", " + "Alpha, Reachable",
            parker_pts_rx + ", " + "Power, Reachable",
            parker_pts_rx + ", " + "ARM,   Reachable",
    })
    public void test_parker_pts_rx(String inputProgramFile, MemoryModelName sourceModel, DartagnanVerdict.Status expected) {
        DartagnanVerdict verdict = createDartagnanModule(inputProgramFile, sourceModel).run();
        assertEquals(verdict.result, expected);
    }

    @TestWith({
            parker_pts_sc + ", " + "SC,    NonReachable",
            parker_pts_sc + ", " + "TSO,   NonReachable",
            parker_pts_sc + ", " + "PSO,   NonReachable",
            parker_pts_sc + ", " + "RMO,   NonReachable",
            parker_pts_sc + ", " + "Alpha, NonReachable",
            parker_pts_sc + ", " + "Power, NonReachable",
            parker_pts_sc + ", " + "ARM,   NonReachable",
    })
    public void test_parker_pts_sc(String inputProgramFile, MemoryModelName sourceModel, DartagnanVerdict.Status expected) {
        DartagnanVerdict verdict = createDartagnanModule(inputProgramFile, sourceModel).run();
        assertEquals(verdict.result, expected);
    }


}