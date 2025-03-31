package org.burgas.backendserver.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public final class CategoryEvent extends ApplicationEvent {

    public CategoryEvent(@Qualifier("category") Object source) {
        super(source);
    }
}
