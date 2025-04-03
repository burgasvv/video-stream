package org.burgas.backendserver.listener;

import org.springframework.context.ApplicationEvent;


public final class InvitationEvent extends ApplicationEvent {

    public InvitationEvent(Object source) {
        super(source);
    }
}
