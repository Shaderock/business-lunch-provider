package com.shaderock.lunch.backend.feature.auth.login.model;

import java.io.Serializable;

public record TokenResponse(String token) implements Serializable {

}
