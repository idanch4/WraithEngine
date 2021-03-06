package unit.engine.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import net.whg.we.main.AbstractBehavior;
import net.whg.we.main.IFixedUpdatable;
import net.whg.we.main.PhysicsPipeline;
import net.whg.we.main.PipelineConstants;
import net.whg.we.main.Timer;

public class PhysicsPipelineActionTest
{
    @Test
    public void ensurePipelinePriority()
    {
        assertEquals(PipelineConstants.PHYSICS_UPDATES, new PhysicsPipeline(mock(Timer.class), 1f).getPriority());
    }

    @Test
    public void updateBehaviors()
    {
        Timer timer = mock(Timer.class);
        when(timer.getElapsedTime()).thenReturn(0.0)
                                    .thenReturn(1.0);

        PhysicsPipeline action = new PhysicsPipeline(timer, 1f);
        action.enableBehavior(mock(AbstractBehavior.class)); // To make sure no casting issues occur

        UpdatableAction behavior = new UpdatableAction();
        assertEquals(0, behavior.calls);

        action.enableBehavior(behavior);
        action.run();
        assertEquals(1, behavior.calls);

        action.disableBehavior(behavior);
        action.run();
        assertEquals(1, behavior.calls);
    }

    @Test
    public void update_twice()
    {
        Timer timer = mock(Timer.class);
        when(timer.getElapsedTime()).thenReturn(1.0);

        PhysicsPipeline action = new PhysicsPipeline(timer, 1f);
        UpdatableAction behavior = new UpdatableAction();
        action.enableBehavior(behavior);

        action.run();
        assertEquals(2, behavior.calls);
    }

    class UpdatableAction extends AbstractBehavior implements IFixedUpdatable
    {
        int calls = 0;

        @Override
        public void fixedUpdate()
        {
            calls++;
        }
    }
}
