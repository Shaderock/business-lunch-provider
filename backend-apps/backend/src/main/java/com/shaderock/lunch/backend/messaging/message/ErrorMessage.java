package com.shaderock.lunch.backend.messaging.message;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorMessage implements Serializable {
  private final boolean displayToUser;
  private final String errMessage;
}
