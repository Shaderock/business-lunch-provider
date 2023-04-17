package com.shaderock.lunch.backend.feature.food.category.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefaultCategory {
  SOUP("Soup"), PASTA("Pasta"), SALAD("Salad"), MEAT("Meat");

  private final String name;
}
