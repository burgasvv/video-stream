package org.burgas.backendserver.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public final class InvitationEvent extends ApplicationEvent {

    public InvitationEvent(@Qualifier("invitationResponse") Object source) {
        super(source);
    }
}
