package com.shaderock.lunch.backend.feature.config.preference.supplier.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryTag {
  BURGERS("Burgers"),
  PIZZA("Pizza"),
  SUSHI("Sushi"),
  KEBAB("Kebab"),
  PASTRY("Pastry"),
  STEAK("Steak"),
  BBQ("BBQ"),
  PASTA("Pasta"),
  SEAFOOD("Seafood"),
  WOK("Wok"),
  MEDITERRANEAN("Mediterranean"),
  TRADITIONAL("Traditional"),
  BREAKFAST("Breakfast"),
  PANCAKES("Pancakes"),
  ORIENTAL("Oriental"),
  VEGETARIAN("Vegetarian"),
  SALADS("Salads"),
  DESSERTS("Desserts"),
  OTHER("Other");

  @JsonValue
  private final String tag;
}

