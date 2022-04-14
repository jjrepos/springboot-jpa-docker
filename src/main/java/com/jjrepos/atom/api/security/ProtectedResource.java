package com.jjrepos.atom.api.security;

import java.lang.annotation.*;

/**
 * Indicates that a  Resource or a Resource method is protected and the user needs to authenticate before
 * accessing the resource.
 *
 * @author Justin Jesudass
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface ProtectedResource {
    /**
     * gets {@link ResourceAccessType}
     *
     * @return {@link ResourceAccessType}
     */
    ResourceAccessType type() default ResourceAccessType.ANYONE;
}
