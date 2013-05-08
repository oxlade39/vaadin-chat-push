package com.github.oxlade39.rx;

import com.vaadin.ui.UI;
import rx.util.functions.Action1;

/**
 * @author dan
 */
public class Actions {
    private Actions(){}

    public static <T> Action1<T> runActionSafely(final UI ui, final Action1<T> toRun) {
        return new Action1<T>() {
            @Override
            public void call(final T o) {
                ui.access(new Runnable() {
                    @Override
                    public void run() {
                        toRun.call(o);
                    }
                });
            }
        };
    }
}
