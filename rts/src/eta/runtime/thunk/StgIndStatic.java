package eta.runtime.thunk;

import eta.runtime.stg.Capability;
import eta.runtime.stg.StgClosure;
import eta.runtime.stg.StgContext;

public class StgIndStatic extends StgThunk {

    public StgIndStatic() {
        super();
    }

    public StgIndStatic(StgClosure indirectee) {
        super(indirectee);
    }

    @Override
    public void enter(StgContext context) {
        super.enter(context);
        if (indirectee == null) {
            Capability cap = context.myCapability;
            StgThunk bh = cap.newCAF(this);
            if (bh == null) {
                enter(context);
            } else {
                context.pushFrame(new StgBHUpdateFrame(bh));
                thunkEnter(context);
            }
        } else {
            context.R(1, this);
            indirectee.enter(context);
        }
    }
}
