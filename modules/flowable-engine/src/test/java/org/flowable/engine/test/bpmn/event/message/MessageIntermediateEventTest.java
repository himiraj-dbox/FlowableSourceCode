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

package org.flowable.engine.test.bpmn.event.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.impl.EventSubscriptionQueryImpl;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.runtime.EventSubscription;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.jupiter.api.Test;

/**
 * @author Tijs Rademakers
 */
public class MessageIntermediateEventTest extends PluggableFlowableTestCase {

    @Test
    @Deployment
    public void testSingleIntermediateMessageEvent() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("process");

        List<String> activeActivityIds = runtimeService.getActiveActivityIds(pi.getId());
        assertNotNull(activeActivityIds);
        assertEquals(1, activeActivityIds.size());
        assertTrue(activeActivityIds.contains("messageCatch"));

        String messageName = "newInvoiceMessage";
        Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName(messageName).singleResult();

        assertNotNull(execution);

        EventSubscription eventSubscription = runtimeService.createEventSubscriptionQuery().executionId(execution.getId()).singleResult();
        assertNotNull(eventSubscription);
        assertEquals(messageName, eventSubscription.getEventName());

        eventSubscription = runtimeService.createEventSubscriptionQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
        assertNotNull(eventSubscription);
        assertEquals(messageName, eventSubscription.getEventName());

        runtimeService.messageEventReceived(messageName, execution.getId());

        org.flowable.task.api.Task task = taskService.createTaskQuery().singleResult();
        assertNotNull(task);
        taskService.complete(task.getId());

    }

    @Test
    @Deployment
    public void testSingleIntermediateMessageExpressionEvent() {
        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("myMessageName", "testMessage");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("process", variableMap);

        List<String> activeActivityIds = runtimeService.getActiveActivityIds(pi.getId());
        assertNotNull(activeActivityIds);
        assertEquals(1, activeActivityIds.size());
        assertTrue(activeActivityIds.contains("messageCatch"));

        String messageName = "testMessage";
        Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName(messageName).singleResult();
        assertNotNull(execution);

        runtimeService.messageEventReceived(messageName, execution.getId());

        org.flowable.task.api.Task task = taskService.createTaskQuery().singleResult();
        assertNotNull(task);
        taskService.complete(task.getId());
    }

    @Test
    @Deployment
    public void testConcurrentIntermediateMessageEvent() {

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("process");

        List<String> activeActivityIds = runtimeService.getActiveActivityIds(pi.getId());
        assertNotNull(activeActivityIds);
        assertEquals(2, activeActivityIds.size());
        assertTrue(activeActivityIds.contains("messageCatch1"));
        assertTrue(activeActivityIds.contains("messageCatch2"));

        String messageName = "newInvoiceMessage";
        List<Execution> executions = runtimeService.createExecutionQuery().messageEventSubscriptionName(messageName).list();

        assertNotNull(executions);
        assertEquals(2, executions.size());

        runtimeService.messageEventReceived(messageName, executions.get(0).getId());

        org.flowable.task.api.Task task = taskService.createTaskQuery().singleResult();
        assertNull(task);

        runtimeService.messageEventReceived(messageName, executions.get(1).getId());

        task = taskService.createTaskQuery().singleResult();
        assertNotNull(task);

        taskService.complete(task.getId());
    }

    @Test
    @Deployment
    public void testAsyncTriggeredMessageEvent() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process");

        assertNotNull(processInstance);
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).messageEventSubscriptionName("newMessage").singleResult();
        assertNotNull(execution);
        assertEquals(1, createEventSubscriptionQuery().count());
        assertEquals(2, runtimeService.createExecutionQuery().count());

        runtimeService.messageEventReceivedAsync("newMessage", execution.getId());

        assertEquals(1, managementService.createJobQuery().messages().count());

        waitForJobExecutorToProcessAllJobs(8000L, 200L);
        assertEquals(0, createEventSubscriptionQuery().count());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
        assertEquals(0, managementService.createJobQuery().count());
    }

    private EventSubscriptionQueryImpl createEventSubscriptionQuery() {
        return new EventSubscriptionQueryImpl(processEngineConfiguration.getCommandExecutor());
    }
}
