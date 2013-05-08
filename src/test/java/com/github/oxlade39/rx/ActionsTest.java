package com.github.oxlade39.rx;

import com.vaadin.ui.UI;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rx.util.functions.Action1;

public class ActionsTest {
    @Test
    public void runActionSafelyWillExecuteActionInsideUiAccessCallback() throws Exception {
        UI ui = Mockito.mock(UI.class);
        Action1<String> toRun = Mockito.mock(Action1.class);
        Action1<String> wrappedAction = Actions.runActionSafely(ui, toRun);

        wrappedAction.call("called");

        ArgumentCaptor<Runnable> argument = ArgumentCaptor.forClass(Runnable.class);
        Mockito.verify(ui).access(argument.capture());
        argument.getValue().run();
        Mockito.verify(toRun).call("called");
    }
}
