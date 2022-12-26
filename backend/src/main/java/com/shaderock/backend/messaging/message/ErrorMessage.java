package com.shaderock.backend.messaging.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorMessage {
  private final boolean displayToUser;
  private final String errMessage;
}
