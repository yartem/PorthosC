package mousquetaires.app.modules;

import com.google.common.collect.ImmutableMap;
import mousquetaires.app.errors.AppError;
import mousquetaires.app.options.AppOptions;
import mousquetaires.languages.syntax.xgraph.XEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AppVerdict {

    public enum ProgramStage {
        Interpretation,
        Unrolling,
        ProgramEncoding,
        ProgramDomainEncoding,
        MemoryModelEncoding,
        Solving,
        ;

        private String separatedByCapitals() {
            String name = this.toString();
            StringBuilder res = new StringBuilder();
            res.append(name.charAt(0));
            for(int i = 1; i < name.length(); i++) {
                Character ch = name.charAt(i);
                if(Character.isUpperCase(ch))
                    res.append(" ").append(Character.toLowerCase(ch));
                else
                    res.append(ch);
            }
            return res.toString();
        }
    }

    private final AppOptions options;
    private final Timer timerMain;
    private final ImmutableMap<ProgramStage, Timer> timers;
    private final HashMap<String, Integer> countEntities;
    private final HashMap<String, Integer> countEntitiesUnrolled;

    private final List<AppError> errors;

    public AppVerdict(AppOptions options) {
        this.options = options;
        this.timerMain = new Timer();
        HashMap<ProgramStage, Timer> timersValues = new HashMap<>();
        for (ProgramStage programStage : ProgramStage.values()) {
            timersValues.put(programStage, new Timer());
        }
        this.timers = ImmutableMap.copyOf(timersValues);
        this.errors = new ArrayList<>();
        this.countEntities = new HashMap<>();
        this.countEntitiesUnrolled = new HashMap<>();
    }

    public void setEntitiesNumber(boolean unrolled, Class<? extends XEntity> entityType, int number) {
        setEntitiesNumber(unrolled, entityType.getSimpleName(), number);
    }

    public void setEntitiesNumber(boolean unrolled, String entityName, int number) {
        Map<String, Integer> map = unrolled ? countEntitiesUnrolled : countEntities;
        map.put(entityName, number);
    }

    public void startAll() {
        timerMain.start();
    }

    public void onStart(ProgramStage stage) {
        String currentTime = String.format("%.3f: ", ((System.currentTimeMillis() - timerMain.getStartTime()) / 1000));
        System.out.println( currentTime + stage.separatedByCapitals() + "...");
        timers.get(stage).start();
    }

    public void onFinish(ProgramStage stage) {
        timers.get(stage).finish();
    }

    public void finishAll() {
        timerMain.finish();
    }


    public void addError(AppError error) {
        errors.add(error);
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public List<AppError> getErrors() {
        return errors;
    }

    // TODO: store original SMT formula here + num of iterations

}
