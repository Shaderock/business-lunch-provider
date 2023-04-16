package com.shaderock.lunch.backend.messaging.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorMessage implements Serializable {

  private final boolean displayToUser;
  private final String errMessage;
  private List<String> errors = new ArrayList<>();
}
