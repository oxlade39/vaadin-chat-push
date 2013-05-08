package com.github.oxlade39.rx;

import org.junit.Test;
import rx.Observable;

import java.util.Arrays;
import java.util.List;

import static com.github.oxlade39.rx.Functions.castTo;
import static com.github.oxlade39.rx.Functions.instanceOf;
import static org.junit.Assert.assertEquals;

/**
 * @author dan
 */
public class FunctionsTest {
    @Test
    public void instanceOfFiltersByType() throws Exception {
        EnqueueAction<List<String>> values = new EnqueueAction<>();

        Observable.from("one", 2, "three")
                .filter(instanceOf(String.class))
                .toList()
                .subscribe(values);

        assertEquals(Arrays.asList("one", "three"), values.next());
    }

    @Test
    public void castToWillCastAllElementsInTheObservable() throws Exception {
        Observable<Object> uncasted = Observable.from((Object) "one", "two", "three");

        Observable<String> casted = uncasted.map(castTo(String.class));

        EnqueueAction<List<String>> values = new EnqueueAction<>();
        casted.toList().subscribe(values);
        assertEquals(Arrays.asList("one", "two", "three"), values.next());
    }
}
