package com.shaderock.backend.messaging.message;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorMessage {
  private final String errMessage;
}
