package com.bui.projects.telegram.session;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface RequestContext {

    void authenticate(Update update);
    void refresh(Update update);
}
