/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flowable.engine.impl.event;

import java.util.Map;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.engine.impl.util.ProcessInstanceHelper;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * @author Daniel Meyer
 * @author Joram Barrez
 */
public class SignalEventHandler extends AbstractEventHandler {

    public static final String EVENT_HANDLER_TYPE = "signal";

    @Override
    public String getEventHandlerType() {
        return EVENT_HANDLER_TYPE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleEvent(EventSubscriptionEntity eventSubscription, Object payload, CommandContext commandContext) {
        if (eventSubscription.getExecutionId() != null) {

            super.handleEvent(eventSubscription, payload, commandContext);

        } else if (eventSubscription.getProcessDefinitionId() != null) {

            // Find initial flow element matching the signal start event
            String processDefinitionId = eventSubscription.getProcessDefinitionId();
            org.flowable.bpmn.model.Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);
            ProcessDefinition processDefinition = ProcessDefinitionUtil.getProcessDefinition(processDefinitionId);

            if (processDefinition.isSuspended()) {
                throw new FlowableException("Could not handle signal: process definition with id: " + processDefinitionId + " is suspended");
            }

            FlowElement flowElement = process.getFlowElement(eventSubscription.getActivityId(), true);
            if (flowElement == null) {
                throw new FlowableException("Could not find matching FlowElement for activityId " + eventSubscription.getActivityId());
            }

            // Start process instance via that flow element
            Map<String, Object> variables = null;
            if (payload instanceof Map) {
                variables = (Map<String, Object>) payload;
            }
            ProcessInstanceHelper processInstanceHelper = CommandContextUtil.getProcessEngineConfiguration(commandContext).getProcessInstanceHelper();
            processInstanceHelper.createAndStartProcessInstanceWithInitialFlowElement(processDefinition, null, null, flowElement, process, variables, null, true);

        } else {
            throw new FlowableException("Invalid signal handling: no execution nor process definition set");
        }
    }

}
