package com.example.amarjeet.hola;

import android.support.annotation.NonNull;

/**
 * Created by Amarjeet on 16-04-2018.
 */

public class UserId {
    public String user_id;

    public <T extends UserId> T withId(@NonNull final String id)
    {
        this.user_id = id;
        return (T) this;
    }
}
