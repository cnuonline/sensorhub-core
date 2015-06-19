/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
The Initial Developer is Botts Innovative Research Inc. Portions created by the Initial
Developer are Copyright (C) 2014 the Initial Developer. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.sensorhub.impl.sensor.trupulse;

import java.io.IOException;
import org.sensorhub.api.comm.ICommProvider;
import org.sensorhub.api.common.SensorHubException;
import org.sensorhub.impl.sensor.AbstractSensorModule;
import org.sensorhub.impl.sensor.trupulse.TruPulseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * Driver implementation supporting the Laser Technology TruPulse 360 Laser Rangefinder.
 * The TruPulse 360 includes GeoSpatial Orientation ("azimuth"), as well as inclination
 * and direct distance. When combined with a sensor that measures GPS location of the 
 * TruPulse sensor, one can calculate the geospatial position of the target.
 * </p>
 *
 * @author Mike Botts <mike.botts@botts-inc.com>
 * @since June 8, 2015
 */
public class TruPulseSensor extends AbstractSensorModule<TruPulseConfig>
{
    static final Logger log = LoggerFactory.getLogger(TruPulseSensor.class);
    
    ICommProvider commProvider;
    TruPulseOutput dataInterface;
    
    
    public TruPulseSensor()
    {
        dataInterface = new TruPulseOutput(this);
        addOutput(dataInterface, false);
        dataInterface.init();
    }
    
    
    @Override
    protected void updateSensorDescription()
    {
        synchronized (sensorDescription)
        {
            super.updateSensorDescription();
            sensorDescription.setId("TruPulse");
            sensorDescription.setUniqueIdentifier("urn:test:sensors:trupulse360");
            sensorDescription.setDescription("Laser RangeFinder for determining distance, inclination, and azimuth");
        }
    }


    @Override
    public void start() throws SensorHubException
    {
        // init comm provider
        try
        {
            if (commProvider == null)
            {
                commProvider = config.commSettings.getProvider();
                commProvider.init(config.commSettings);
            }        
        }
        catch (IOException e)
        {
            commProvider = null;
            log.error("Error while initializing communications ", e);
        }
        
        dataInterface.start(commProvider);        
    }
    

    @Override
    public void stop() throws SensorHubException
    {
        dataInterface.stop();
        
        if (commProvider != null)
        {
            commProvider.close();
            commProvider = null;
        }
    }
    

    @Override
    public void cleanup() throws SensorHubException
    {
       
    }
    
    
    @Override
    public boolean isConnected()
    {
        return (commProvider != null);
    }
}