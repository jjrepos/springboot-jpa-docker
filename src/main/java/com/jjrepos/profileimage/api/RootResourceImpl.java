package com.jjrepos.profileimage.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root resource simply to mark the root of the API.
 *
 * @author Seth Jordan
 */
@RestController
@RequestMapping("/")
public class RootResourceImpl implements RootResource {

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public String getWelcomeMessage() {
        return "Profile Image API";
    }
}
