package org.burgas.backendserver.exception;

public class WrongInvitationAnswerException extends RuntimeException {

  public WrongInvitationAnswerException(String message) {
    super(message);
  }
}
