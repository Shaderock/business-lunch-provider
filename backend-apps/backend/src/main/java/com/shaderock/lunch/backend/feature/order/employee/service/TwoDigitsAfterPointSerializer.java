package com.shaderock.lunch.backend.feature.order.employee.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class TwoDigitsAfterPointSerializer extends JsonSerializer<Double> {

  @Override
  public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    String formattedValue = String.format("%.2f", value);
    gen.writeString(formattedValue);
  }
}
