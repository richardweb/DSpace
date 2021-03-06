/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.webui.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.webui.search.LuceneSearchRequestProcessor;
import org.dspace.app.webui.search.SearchProcessorException;
import org.dspace.app.webui.search.SearchRequestProcessor;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.core.PluginConfigurationError;
import org.dspace.core.PluginManager;

/**
 * Servlet for handling a simple search.
 * 
 */
public class SimpleSearchServlet extends DSpaceServlet
{
    private SearchRequestProcessor internalLogic;

    /** log4j category */
    private static Logger log = Logger.getLogger(SimpleSearchServlet.class);

    public void init()
    {
        try
        {
            internalLogic = (SearchRequestProcessor) PluginManager
                    .getSinglePlugin(SearchRequestProcessor.class);
        }
        catch (PluginConfigurationError e)
        {
            log.warn(
                    "SimpleSearchServlet not properly configurated, please configure the SearchRequestProcessor plugin",
                    e);
        }
        if (internalLogic == null)
        {   // backward compatibility
            internalLogic = new LuceneSearchRequestProcessor();
        }
    }

    protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        try
        {
            internalLogic.doSimpleSearch(context, request, response);
        }
        catch (SearchProcessorException e)
        {
            throw new ServletException(e.getMessage(), e);
        }
    }
}
