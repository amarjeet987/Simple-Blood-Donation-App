package com.example.amarjeet.hola;

import android.support.annotation.NonNull;

/**
 * Created by Amarjeet on 19-04-2018.
 */

public class AcceptId {
    public String user_id;

    public <T extends AcceptId> T withId(@NonNull final String id)
    {
        this.user_id = id;
        return (T) this;
    }
}
