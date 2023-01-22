package com.shaderock.backend.organization.supplier.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.Duration;
import java.time.LocalTime;

public record SupplierRegistrationForm(
        @NotNull(message = "Name should be provided")
        @NotBlank(message = "Name can't be blank")
        String name,
        @NotBlank(message = "Email should be provided")
        @Email(message = "Wrong email")
        String email,
        String description,
        @NotNull(message = "Phone number should be provided")
        @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Not a phone number (e.g. +37312345678)")
        String phone,
        @NotNull(message = "Website URL should be provided")
        @Pattern(regexp = "((http|https)://)(www.)?[a-zA-Z0-9@:%._+~#?&/=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._+~#?&/=]*)",
                message = "Wrong website url")
        String websiteUrl,

        @NotNull(message = "Menu URL should be provided")
        @Pattern(regexp = "((http|https)://)(www.)?[a-zA-Z0-9@:%._+~#?&/=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._+~#?&/=]*)",
                message = "Wrong menu url")
        String menuUrl,
        @NotNull(message = "Request offset duration time should be provided")
        Duration requestOffset,
        @NotNull(message = "Delivery start time should be provided")
        LocalTime deliveryPeriodStartTime,
        @NotNull(message = "Delivery end time should be provided")
        LocalTime deliveryPeriodEndTime) {
}
