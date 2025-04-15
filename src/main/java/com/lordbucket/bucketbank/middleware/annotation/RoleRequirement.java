package com.lordbucket.bucketbank.middleware.annotation;

import com.lordbucket.bucketbank.util.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RoleRequirement {
    Role value();
}
