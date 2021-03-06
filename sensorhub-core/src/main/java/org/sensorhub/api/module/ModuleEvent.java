/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.sensorhub.api.module;

import org.sensorhub.api.common.Event;
import org.sensorhub.api.module.ModuleEvent.Type;


/**
 * <p>
 * Event type generated at various times during a module's lifecycle
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Sep 5, 2013
 */
public class ModuleEvent extends Event<Type>
{
    public enum Type
    {
        /**
         * after the module state has changed
         */
        STATE_CHANGED,
        
        /**
         * after the module configuration has been changed and accepted through updateConfig()
         */
        CONFIG_CHANGED,
        
        /**
         * after module is unloaded from registry
         */
        UNLOADED,
        
        /**
         * after module is fully deleted (along with its configuration) 
         */
        DELETED
    }
    
    
    public enum ModuleState 
    {
        /**
         * after module class is first instantiated and init() has been called
         */
        LOADED,
        
        /**
         * when asynchronous processing goes on during init
         */
        INITIALIZING,
        
        /**
         * after module was successfully initialized
         */
        INITIALIZED,
        
        /**
         * when module start is requested
         */
        STARTING,
        
        /**
         * after module was successfully started
         */
        STARTED,
        
        /**
         * when module stop is requested asynchronously
         */
        STOPPING,
        
        /**
         * after module was stopped
         */
        STOPPED
    }
    
    
    public Type type;
    public ModuleState newState;
    
    
    public ModuleEvent(IModule<?> module, Type type)
    {
        this.source = module;
        this.type = type;
        if (type == Type.STATE_CHANGED)
            this.newState = module.getCurrentState();
    }
    
    
    public IModule<?> getModule()
    {
        return (IModule<?>)source;
    }
    
    
    public ModuleState getNewState()
    {
        return newState;
    }
}
