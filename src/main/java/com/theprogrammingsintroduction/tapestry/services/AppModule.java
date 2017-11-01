package com.theprogrammingsintroduction.tapestry.services;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.validator.ValidatorMacro;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */

public class AppModule
{
	

    @ApplicationDefaults
    @Contribute(SymbolProvider.class)
    public static void configureTapestryHotelBooking(
            MappedConfiguration<String, String> configuration)
    {

        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,es");
        configuration.add(SymbolConstants.APPLICATION_VERSION, "2.0-SNAPSHOT");
        configuration.add(SymbolConstants.PRODUCTION_MODE, "true");
        configuration.add(SymbolConstants.RESTRICTIVE_ENVIRONMENT, "true");

        // Generate a random HMAC key for form signing (not cluster safe).
        // Normally it would be better to use a fixed password-like string, but
        // we can't because this file is published as open source software.
        configuration.add(SymbolConstants.HMAC_PASSPHRASE,
                new BigInteger(130, new SecureRandom()).toString(32));

        // use jquery instead of prototype as foundation JS library
        configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER, "jquery");
        
        // false turns off switching between HTTP and HTTPS (ignoring @Secure
        // annotations), so if app is served under HTTP it will stay that way,
        // and if served under HTTPS it will also stay that way, for all pages
        configuration.add(SymbolConstants.SECURE_ENABLED, "false");
    }

    @Contribute(ValidatorMacro.class)
    public static void combineValidators(MappedConfiguration<String, String> configuration)
    {
        configuration.add("username", "required, minlength=3, maxlength=15");
        configuration.add("password", "required, minlength=6, maxlength=12");
    }

    @Contribute(ComponentRequestHandler.class)
    public static void contributeComponentRequestHandler(
            OrderedConfiguration<ComponentRequestFilter> configuration)
    {
        //configuration.addInstance("RequiresLogin", AuthenticationFilter.class);
    }

    /**
     * Redirect the user to the intended page when browsing through
     * tapestry forms through browser history or over-eager auto-complete
     * Credit to Lenny Primak.
     */
    public RequestExceptionHandler decorateRequestExceptionHandler(
            final ComponentSource componentSource,
            final Response response,
            final RequestExceptionHandler oldHandler)
    {
        return new RequestExceptionHandler()
        {
            @Override
            public void handleRequestException(Throwable exception) throws IOException
            {
                if (exception.getMessage() == null || !exception.getMessage().contains("Forms require that the request method be POST and that the t:formdata query parameter have values"))
                {
                    oldHandler.handleRequestException(exception);
                    return;
                }
                ComponentResources cr = componentSource.getActivePage().getComponentResources();
                Link link = cr.createEventLink("");
                String uri = link.toRedirectURI().replaceAll(":", "");
                response.sendRedirect(uri);
            }
        };
    }
}
