/*
 * Copyright (c) 2002-2021, Mairie de Paris
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

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.paris.lutece.plugins.releaser.business.Cluster;
import fr.paris.lutece.plugins.releaser.business.ClusterHome;
import fr.paris.lutece.plugins.releaser.modules.rest.util.Constants;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

/**
 * ClusterRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.CLUSTER_PATH )
public class ClusterRest
{
    /**
     * Get Clusters List
     * @return the Id clusters List
     */
    @GET
    @Path( Constants.CLUSTERS_ID )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getClustersList( @Context HttpServletRequest request )
    {
    	try
        {
    		HashMap<Integer, String> clusterIdName = new HashMap<Integer, String>();
    		
    		List<Cluster> clusterList = ClusterHome.getClustersListWithoutSites( );
    		if (clusterList != null && !clusterList.isEmpty())
    		{
    			for (Cluster cluster : clusterList)
    			{
    				clusterIdName.put(cluster.getId(), cluster.getName());
    			}
    		}
       		    		
            AppLogService.debug( "Site releaser - Get clusters IDs" );

            return Response.status( Response.Status.OK ).entity( JsonUtil.buildJsonResponse( new JsonResponse( clusterIdName ) ) ).build( );
        }
        catch (Exception ex) 
        {
        	AppLogService.error( "Site releaser - Error : " + ex.getMessage(), ex );
        	return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( ex.getMessage( ) ).build( );
		}    	
    }        
 }

            