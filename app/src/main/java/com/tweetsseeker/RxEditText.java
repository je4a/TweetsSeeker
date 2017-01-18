package com.tweetsseeker;

import android.support.annotation.NonNull;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

public final class RxEditText {
    public static Observable<Void> searchAction(@NonNull EditText view) {
        return Observable.create(new EditTextOnSubscribe(view));
    }

    private static class EditTextOnSubscribe implements Observable.OnSubscribe<Void> {
        private EditText view;

        public EditTextOnSubscribe(EditText view) {
            this.view = view;
        }

        @Override public void call(final Subscriber<? super Void> subscriber) {
            view.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    subscriber.onNext(null);
                    return true;
                }
                return false;
            });

            subscriber.add(new MainThreadSubscription() {
                @Override protected void onUnsubscribe() {
                    view.setOnEditorActionListener(null);
                }
            });
        }
    }

    private RxEditText() { throw new AssertionError("No instances."); }
}
