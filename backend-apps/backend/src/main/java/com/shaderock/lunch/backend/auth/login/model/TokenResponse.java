package com.shaderock.lunch.backend.auth.login.model;

import java.io.Serializable;

public record TokenResponse(String token) implements Serializable {

}
