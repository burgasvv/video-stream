package org.burgas.backendserver.listener;

import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class GlobalEventListener {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GlobalEventListener.class);

    @EventListener
    public void listenCategoryEvent(CategoryEvent categoryEvent) {
        log.info("Listen to category event: {}", categoryEvent.getSource());
    }

    @EventListener
    public void listenInvitationEvent(InvitationEvent invitationEvent) {
        log.info("Listen to invitation: {}", invitationEvent.getSource());
    }
}
