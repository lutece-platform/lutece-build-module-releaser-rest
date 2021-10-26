/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.releaser.modules.rest.rs;

import fr.paris.lutece.plugins.releaser.business.Site;
import fr.paris.lutece.plugins.releaser.business.SiteHome;
import fr.paris.lutece.plugins.releaser.modules.rest.util.Constants;
import fr.paris.lutece.plugins.releaser.service.SiteService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.POST;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * SiteRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.SITE_PATH )
public class SiteRest
{   
    private static final String MESSAGE_SITE_CREATED = "module.releaser.rest.message.siteCreated";
    private static final String MESSAGE_SITE_ALREADY_EXIST = "module.releaser.rest.message.siteAlreadyExist";
    private static final String MESSAGE_ERROR_EMPTY_JSON = "module.releaser.rest.message.emptyJson";
    private static final String MESSAGE_ERROR_NULL_SITE = "module.releaser.rest.message.nullSite";

    @POST
    @Path( Constants.SITE_ADD )
    @Produces( MediaType.APPLICATION_JSON )
    public Response addSite( String siteAttributes, @Context HttpServletRequest request )
    {
		Site site = null;
		
    	// Get attributes from json
    	if ( StringUtils.isNotBlank( siteAttributes ) )
        {
	        try
	        {	        	
	            // Format from JSON
	            ObjectMapper mapper = new ObjectMapper( );
	
	            site = mapper.readValue( siteAttributes, Site.class );
	            
	            // Check if the site already exist
	            if ( SiteService.IsSiteAlreadyExist( site.getName( ), site.getArtifactId( ), site.getScmUrl( ) ) )
	            {
	            	AppLogService.error( "Site releaser - Error : " +  I18nService.getLocalizedString( MESSAGE_SITE_ALREADY_EXIST, request.getLocale() ) );
	            	return Response.status( Response.Status.CONFLICT ).entity( I18nService.getLocalizedString( MESSAGE_SITE_ALREADY_EXIST, request.getLocale() ) ).build( );
	            }
	            	            
	            AppLogService.debug( "Site releaser / Add site - Received strJson : " + siteAttributes );

	        	// Call site creating method
	        	return createSite( site, request );
	        }
	        catch (Exception ex) 
	        {
	        	AppLogService.error( "Site releaser - Error : " + ex.getMessage(), ex );
            	return Response.status( Response.Status.INTERNAL_SERVER_ERROR).entity( ex.getMessage( ) ).build( );
			}
        }
    	
    	AppLogService.error( "Site releaser - Error : " +  I18nService.getLocalizedString( MESSAGE_ERROR_EMPTY_JSON, request.getLocale() ) );
    	return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( I18nService.getLocalizedString( MESSAGE_ERROR_EMPTY_JSON, request.getLocale() )).build( );
    	
    }
           
    /**
     * Create Site
     * @param site a Site object
     * @return the Site if created for the version 1
     */
    private Response createSite( Site site, @Context HttpServletRequest request )
    {
        if ( site == null ) 
        {
        	AppLogService.error( "Site releaser - Error : " + I18nService.getLocalizedString( MESSAGE_ERROR_NULL_SITE, request.getLocale() ) );
        	return Response.status( Response.Status.BAD_REQUEST).entity( I18nService.getLocalizedString( MESSAGE_ERROR_NULL_SITE, request.getLocale() )).build( );
        }        

        try 
        {
			SiteHome.create( site );
		} 
        catch (Exception ex) 
        {
        	AppLogService.error( "Site releaser - Error : " + ex.getMessage(), ex );
        	return Response.status( Response.Status.INTERNAL_SERVER_ERROR).entity( ex.getMessage( ) ).build( );
		}
        
 		
        AppLogService.debug( "Site releaser - Site created" );
        return Response.status( Response.Status.OK ).entity(  I18nService.getLocalizedString( MESSAGE_SITE_CREATED, request.getLocale() )  ).build( );		
    }    
}