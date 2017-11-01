package com.theprogrammingsintroduction.tapestry.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Layout component for all pages
 */
@Import(stylesheet =
{ "context:/static/style.css" }) //
public class Layout
{
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String pageTitle;

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block sidebar;

    @Inject
    private ComponentResources resources;



    @Inject
    private Messages messages;



    /**
     * Return a "Welcome, John Smith" message
     * @return the message, or an empty string if there is no logged-in user
     */
    public String getWelcomeMessage()
    {

        return "";
    }
}
