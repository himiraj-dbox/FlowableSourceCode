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
package org.flowable.engine.test.api.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.engine.impl.test.HistoryTestHelper;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.identitylink.api.IdentityLinkInfo;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Joram Barrez
 * @author Frederik Heremans
 * @author Falko Menge
 */
public class TaskQueryTest extends PluggableFlowableTestCase {

    private List<String> taskIds;

    @BeforeEach
    public void setUp() throws Exception {

        identityService.saveUser(identityService.newUser("kermit"));
        identityService.saveUser(identityService.newUser("gonzo"));
        identityService.saveUser(identityService.newUser("fozzie"));

        identityService.saveGroup(identityService.newGroup("management"));
        identityService.saveGroup(identityService.newGroup("accountancy"));

        identityService.createMembership("kermit", "management");
        identityService.createMembership("kermit", "accountancy");
        identityService.createMembership("fozzie", "management");

        taskIds = generateTestTasks();
    }

    @AfterEach
    public void tearDown() throws Exception {
        identityService.deleteGroup("accountancy");
        identityService.deleteGroup("management");
        identityService.deleteUser("fozzie");
        identityService.deleteUser("gonzo");
        identityService.deleteUser("kermit");
        taskService.deleteTasks(taskIds, true);
    }

    @Test
    public void testBasicTaskPropertiesNotNull() {
        org.flowable.task.api.Task task = taskService.createTaskQuery().taskId(taskIds.get(0)).singleResult();
        assertNotNull(task.getDescription());
        assertNotNull(task.getId());
        assertNotNull(task.getName());
        assertNotNull(task.getCreateTime());
    }

    @Test
    public void testQueryNoCriteria() {
        TaskQuery query = taskService.createTaskQuery();
        assertEquals(12, query.count());
        assertEquals(12, query.list().size());
        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByTaskId() {
        TaskQuery query = taskService.createTaskQuery().taskId(taskIds.get(0));
        assertNotNull(query.singleResult());
        assertEquals(1, query.list().size());
        assertEquals(1, query.count());
    }

    @Test
    public void testQueryByTaskIdOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId(taskIds.get(0)).taskName("INVALID NAME").endOr();
        assertNotNull(query.singleResult());
        assertEquals(1, query.list().size());
        assertEquals(1, query.count());
    }

    @Test
    public void testQueryByInvalidTaskId() {
        TaskQuery query = taskService.createTaskQuery().taskId("invalid");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().taskId(null);
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByInvalidTaskIdOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskName("invalid");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().taskId(null);
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByName() {
        TaskQuery query = taskService.createTaskQuery().taskName("testTask");
        assertEquals(6, query.list().size());
        assertEquals(6, query.count());

        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNameOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskName("testTask").taskId("invalid");
        assertEquals(6, query.list().size());
        assertEquals(6, query.count());

        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByInvalidName() {
        TaskQuery query = taskService.createTaskQuery().taskName("invalid");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskName(null).singleResult();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByInvalidNameOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskName("invalid");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskName(null).singleResult();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNameIn() {
        final List<String> taskNameList = new ArrayList<>(2);
        taskNameList.add("testTask");
        taskNameList.add("gonzoTask");

        TaskQuery query = taskService.createTaskQuery().taskNameIn(taskNameList);
        assertEquals(7, query.list().size());
        assertEquals(7, query.count());

        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNameInIgnoreCase() {
        final List<String> taskNameList = new ArrayList<>(2);
        taskNameList.add("testtask");
        taskNameList.add("gonzotask");

        TaskQuery query = taskService.createTaskQuery().taskNameInIgnoreCase(taskNameList);
        assertEquals(7, query.list().size());
        assertEquals(7, query.count());

        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNameInOr() {
        final List<String> taskNameList = new ArrayList<>(2);
        taskNameList.add("testTask");
        taskNameList.add("gonzoTask");

        TaskQuery query = taskService.createTaskQuery().or().taskNameIn(taskNameList).taskId("invalid");
        assertEquals(7, query.list().size());
        assertEquals(7, query.count());

        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNameInIgnoreCaseOr() {
        final List<String> taskNameList = new ArrayList<>(2);
        taskNameList.add("testtask");
        taskNameList.add("gonzotask");

        TaskQuery query = taskService.createTaskQuery().or().taskNameInIgnoreCase(taskNameList).taskId("invalid");
        assertEquals(7, query.list().size());
        assertEquals(7, query.count());

        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByInvalidNameIn() {
        final List<String> taskNameList = new ArrayList<>(1);
        taskNameList.add("invalid");

        TaskQuery query = taskService.createTaskQuery().taskNameIn(taskNameList);
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskNameIn(null).singleResult();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByInvalidNameInIgnoreCase() {
        final List<String> taskNameList = new ArrayList<>(1);
        taskNameList.add("invalid");

        TaskQuery query = taskService.createTaskQuery().taskNameInIgnoreCase(taskNameList);
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskNameIn(null).singleResult();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByInvalidNameInOr() {
        final List<String> taskNameList = new ArrayList<>(2);
        taskNameList.add("invalid");

        TaskQuery query = taskService.createTaskQuery().or().taskNameIn(taskNameList).taskId("invalid");
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskNameIn(null).singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByInvalidNameInIgnoreCaseOr() {
        final List<String> taskNameList = new ArrayList<>(2);
        taskNameList.add("invalid");

        TaskQuery query = taskService.createTaskQuery().or().taskNameInIgnoreCase(taskNameList).taskId("invalid");
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskNameIn(null).singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNameLike() {
        TaskQuery query = taskService.createTaskQuery().taskNameLike("gonzo%");
        assertNotNull(query.singleResult());
        assertEquals(1, query.list().size());
        assertEquals(1, query.count());
    }

    @Test
    public void testQueryByNameLikeOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskNameLike("gonzo%");
        assertNotNull(query.singleResult());
        assertEquals(1, query.list().size());
        assertEquals(1, query.count());
    }

    @Test
    public void testQueryByInvalidNameLike() {
        TaskQuery query = taskService.createTaskQuery().taskNameLike("1");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskNameLike(null).singleResult();
            fail();
        } catch (FlowableIllegalArgumentException e) {
        }
    }

    @Test
    public void testQueryByInvalidNameLikeOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskNameLike("1");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskNameLike(null).singleResult();
            fail();
        } catch (FlowableIllegalArgumentException e) {
        }
    }

    @Test
    public void testQueryByDescription() {
        TaskQuery query = taskService.createTaskQuery().taskDescription("testTask description");
        assertEquals(6, query.list().size());
        assertEquals(6, query.count());

        try {
            query.singleResult();
            fail();
        } catch (FlowableException e) {
        }
    }

    @Test
    public void testQueryByDescriptionOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskDescription("testTask description");
        assertEquals(6, query.list().size());
        assertEquals(6, query.count());

        try {
            query.singleResult();
            fail();
        } catch (FlowableException e) {
        }
    }

    @Test
    public void testQueryByInvalidDescription() {
        TaskQuery query = taskService.createTaskQuery().taskDescription("invalid");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskDescription(null).list();
            fail();
        } catch (FlowableIllegalArgumentException e) {

        }
    }

    @Test
    public void testQueryByInvalidDescriptionOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskDescription("invalid");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskDescription(null).list();
            fail();
        } catch (FlowableIllegalArgumentException e) {

        }
    }

    @Test
    public void testQueryByDescriptionLike() {
        TaskQuery query = taskService.createTaskQuery().taskDescriptionLike("%gonzo%");
        assertNotNull(query.singleResult());
        assertEquals(1, query.list().size());
        assertEquals(1, query.count());
    }

    @Test
    public void testQueryByDescriptionLikeOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskDescriptionLike("%gonzo%");
        assertNotNull(query.singleResult());
        assertEquals(1, query.list().size());
        assertEquals(1, query.count());
    }

    @Test
    public void testQueryByInvalidDescriptionLike() {
        TaskQuery query = taskService.createTaskQuery().taskDescriptionLike("invalid");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskDescriptionLike(null).list();
            fail();
        } catch (FlowableIllegalArgumentException e) {

        }
    }

    @Test
    public void testQueryByInvalidDescriptionLikeOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskDescriptionLike("invalid");
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        try {
            taskService.createTaskQuery().or().taskId("invalid").taskDescriptionLike(null).list();
            fail();
        } catch (FlowableIllegalArgumentException e) {

        }
    }

    @Test
    public void testQueryByPriority() {
        TaskQuery query = taskService.createTaskQuery().taskPriority(10);
        assertEquals(2, query.list().size());
        assertEquals(2, query.count());

        try {
            query.singleResult();
            fail();
        } catch (FlowableException e) {
        }

        query = taskService.createTaskQuery().taskPriority(100);
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        query = taskService.createTaskQuery().taskMinPriority(50);
        assertEquals(3, query.list().size());

        query = taskService.createTaskQuery().taskMinPriority(10);
        assertEquals(5, query.list().size());

        query = taskService.createTaskQuery().taskMaxPriority(10);
        assertEquals(9, query.list().size());

        query = taskService.createTaskQuery().taskMaxPriority(3);
        assertEquals(6, query.list().size());
    }

    @Test
    public void testQueryByPriorityOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskPriority(10);
        assertEquals(2, query.list().size());
        assertEquals(2, query.count());

        try {
            query.singleResult();
            fail();
        } catch (FlowableException e) {
        }

        query = taskService.createTaskQuery().or().taskId("invalid").taskPriority(100);
        assertNull(query.singleResult());
        assertEquals(0, query.list().size());
        assertEquals(0, query.count());

        query = taskService.createTaskQuery().or().taskId("invalid").taskMinPriority(50);
        assertEquals(3, query.list().size());

        query = taskService.createTaskQuery().or().taskId("invalid").taskMinPriority(10);
        assertEquals(5, query.list().size());

        query = taskService.createTaskQuery().or().taskId("invalid").taskMaxPriority(10);
        assertEquals(9, query.list().size());

        query = taskService.createTaskQuery().or().taskId("invalid").taskMaxPriority(3);
        assertEquals(6, query.list().size());
    }

    @Test
    public void testQueryByInvalidPriority() {
        try {
            taskService.createTaskQuery().taskPriority(null);
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByInvalidPriorityOr() {
        try {
            taskService.createTaskQuery().or().taskId("invalid").taskPriority(null);
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByAssignee() {
        TaskQuery query = taskService.createTaskQuery().taskAssignee("gonzo");
        assertEquals(1, query.count());
        assertEquals(1, query.list().size());
        assertNotNull(query.singleResult());

        query = taskService.createTaskQuery().taskAssignee("kermit");
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
        assertNull(query.singleResult());
    }

    @Test
    public void testQueryByAssigneeOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskAssignee("gonzo");
        assertEquals(1, query.count());
        assertEquals(1, query.list().size());
        assertNotNull(query.singleResult());

        query = taskService.createTaskQuery().or().taskId("invalid").taskAssignee("kermit");
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
        assertNull(query.singleResult());
    }

    @Test
    public void testQueryByAssigneeIds() {
        TaskQuery query = taskService.createTaskQuery().taskAssigneeIds(Arrays.asList("gonzo", "kermit"));
        assertEquals(1, query.count());
        assertEquals(1, query.list().size());
        assertNotNull(query.singleResult());

        query = taskService.createTaskQuery().taskAssigneeIds(Arrays.asList("kermit", "kermit2"));
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
        assertNull(query.singleResult());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskAssigneeIds(Arrays.asList("gonzo", "kermit")).count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().taskAssigneeIds(Arrays.asList("kermit", "kermit2")).count());
        }

        org.flowable.task.api.Task adhocTask = taskService.newTask();
        adhocTask.setName("test");
        adhocTask.setAssignee("testAssignee");
        taskService.saveTask(adhocTask);

        query = taskService.createTaskQuery().taskAssigneeIds(Arrays.asList("gonzo", "testAssignee"));
        assertEquals(2, query.count());
        assertEquals(2, query.list().size());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(2, historyService.createHistoricTaskInstanceQuery().taskAssigneeIds(Arrays.asList("gonzo", "testAssignee")).count());
        }

        taskService.deleteTask(adhocTask.getId(), true);
    }

    @Test
    public void testQueryByAssigneeIdsOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskAssigneeIds(Arrays.asList("gonzo", "kermit"));
        assertEquals(1, query.count());
        assertEquals(1, query.list().size());
        assertNotNull(query.singleResult());

        query = taskService.createTaskQuery().or().taskId("invalid").taskAssigneeIds(Arrays.asList("kermit", "kermit2"));
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
        assertNull(query.singleResult());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().or().taskId("invalid").taskAssigneeIds(Arrays.asList("gonzo", "kermit")).count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().or().taskId("invalid").taskAssigneeIds(Arrays.asList("kermit", "kermit2")).count());
        }

        org.flowable.task.api.Task adhocTask = taskService.newTask();
        adhocTask.setName("test");
        adhocTask.setAssignee("testAssignee");
        taskService.saveTask(adhocTask);

        query = taskService.createTaskQuery().or().taskId("invalid").taskAssigneeIds(Arrays.asList("gonzo", "testAssignee"));
        assertEquals(2, query.count());
        assertEquals(2, query.list().size());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(2, historyService.createHistoricTaskInstanceQuery().or().taskId("invalid").taskAssigneeIds(Arrays.asList("gonzo", "testAssignee")).count());
        }

        taskService.deleteTask(adhocTask.getId(), true);
    }

    @Test
    public void testQueryByInvolvedUser() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            adhocTask.setAssignee("kermit");
            adhocTask.setOwner("fozzie");
            taskService.saveTask(adhocTask);
            taskService.addUserIdentityLink(adhocTask.getId(), "gonzo", "customType");

            assertEquals(3, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(1, taskService.createTaskQuery().taskId(adhocTask.getId()).taskInvolvedUser("gonzo").count());
            assertEquals(1, taskService.createTaskQuery().taskId(adhocTask.getId()).taskInvolvedUser("kermit").count());
            assertEquals(1, taskService.createTaskQuery().taskId(adhocTask.getId()).taskInvolvedUser("fozzie").count());

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedUserOr() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            adhocTask.setAssignee("kermit");
            adhocTask.setOwner("fozzie");
            taskService.saveTask(adhocTask);
            taskService.addUserIdentityLink(adhocTask.getId(), "gonzo", "customType");

            assertEquals(3, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(1, taskService.createTaskQuery().taskId(adhocTask.getId()).or().taskId("invalid").taskInvolvedUser("gonzo").count());
            assertEquals(1, taskService.createTaskQuery().taskId(adhocTask.getId()).or().taskId("invalid").taskInvolvedUser("kermit").count());
            assertEquals(1, taskService.createTaskQuery().taskId(adhocTask.getId()).or().taskId("invalid").taskInvolvedUser("fozzie").count());

            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskId(adhocTask.getId()).or().taskId("invalid").taskInvolvedUser("fozzie").count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroups() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");

            assertEquals(1, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(1, taskService.createTaskQuery().taskId(adhocTask.getId()).taskInvolvedGroups(Collections.singleton("testGroup")).count());

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupOr() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");

            assertEquals(1, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(1, taskService.createTaskQuery().taskId(adhocTask.getId()).or().taskId("invalid").taskInvolvedGroups(Collections.singleton("testGroup")).count());

            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskId(adhocTask.getId()).or().taskId("invalid").taskInvolvedGroups(Collections.singleton("testGroup")).count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    private void deleteAllTasks() {
        List<org.flowable.task.api.Task> allTasks = taskService.createTaskQuery().list();
        for (org.flowable.task.api.Task task : allTasks) {
            if (task.getExecutionId() == null) {
                taskService.deleteTask(task.getId());
                if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                    historyService.deleteHistoricTaskInstance(task.getId());
                }
            }
        }
    }

    @Test
    public void testQueryByInvolvedGroupOrAssignee() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setAssignee("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");

            assertEquals(1, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(2, taskService.createTaskQuery().
                or().taskAssignee("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).endOr().count());

            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(2, historyService.createHistoricTaskInstanceQuery().
                        or().taskAssignee("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).endOr().count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupOrOwner() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setOwner("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");

            assertEquals(1, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(2, taskService.createTaskQuery().
                or().taskOwner("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).endOr().count());

            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(2, historyService.createHistoricTaskInstanceQuery().
                        or().taskOwner("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).endOr().count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupAndAssignee() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setAssignee("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setAssignee("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(1, taskService.createTaskQuery().
                taskAssignee("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).
                count());

            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                        taskAssignee("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).
                        count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupAndOwner() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setOwner("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setOwner("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(1, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(1, taskService.createTaskQuery().
                taskOwner("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).
                count());

            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                        taskOwner("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).
                        count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupAndOwnerLike() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setOwner("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setOwner("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(1, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(1, taskService.createTaskQuery().
                taskOwnerLike("ker%").taskInvolvedGroups(Collections.singleton("testGroup")).
                count());
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                        taskOwnerLike("ker%").taskInvolvedGroups(Collections.singleton("testGroup")).
                        count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupAndAssigneeLike() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setAssignee("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setAssignee("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(1, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(1, taskService.createTaskQuery().
                taskAssigneeLike("ker%").taskInvolvedGroups(Collections.singleton("testGroup")).
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                        taskAssigneeLike("ker%").taskInvolvedGroups(Collections.singleton("testGroup")).
                        count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupAndAssigneeIds() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setAssignee("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setAssignee("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(1, taskService.getIdentityLinksForTask(adhocTask.getId()).size());

            assertEquals(1, taskService.createTaskQuery().
                taskAssigneeIds(Collections.singletonList("kermit")).taskInvolvedGroups(Collections.singleton("testGroup")).
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                        taskAssigneeIds(Collections.singletonList("kermit")).taskInvolvedGroups(Collections.singleton("testGroup")).
                        count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupOrOwnerLike() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setOwner("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setOwner("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(3, taskService.createTaskQuery().
                or().
                    taskOwnerLike("ker%").taskInvolvedGroups(Collections.singleton("testGroup")).
                endOr().
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(3, historyService.createHistoricTaskInstanceQuery().
                    or().
                        taskOwnerLike("ker%").taskInvolvedGroups(Collections.singleton("testGroup")).
                    endOr().
                    count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupOrAssigneeLike() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setAssignee("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setAssignee("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(3, taskService.createTaskQuery().
                or().
                    taskAssigneeLike("ker%").taskInvolvedGroups(Collections.singleton("testGroup")).
                endOr().
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(3, historyService.createHistoricTaskInstanceQuery().
                        or().
                            taskAssigneeLike("ker%").taskInvolvedGroups(Collections.singleton("testGroup")).
                        endOr().
                        count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupOrAssigneeIds() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setAssignee("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setAssignee("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(3, taskService.createTaskQuery().
                or().
                    taskAssigneeIds(Collections.singletonList("kermit")).taskInvolvedGroups(Collections.singleton("testGroup")).
                endOr().
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(3, historyService.createHistoricTaskInstanceQuery().
                        or().
                            taskAssigneeIds(Collections.singletonList("kermit")).taskInvolvedGroups(Collections.singleton("testGroup")).
                        endOr().
                        count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupOrAssigneeId() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setAssignee("kermit");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            adhocTask3.setAssignee("kermit");
            taskService.saveTask(adhocTask3);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");

            assertEquals(3, taskService.createTaskQuery().
                or().
                    taskAssignee("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).
                endOr().
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(3, historyService.createHistoricTaskInstanceQuery().
                        or().
                            taskAssignee("kermit").taskInvolvedGroups(Collections.singleton("testGroup")).
                        endOr().
                        count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByInvolvedGroupTaskName() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setName("testName");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            taskService.saveTask(adhocTask3);
            org.flowable.task.api.Task adhocTask4 = taskService.newTask();
            adhocTask4.setName("testName");
            taskService.saveTask(adhocTask4);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", "customType");
            taskService.addGroupIdentityLink(adhocTask4.getId(), "testGroup", "customType");

            assertEquals(4, taskService.createTaskQuery().
                or().
                    taskName("testName").
                    taskInvolvedGroups(Collections.singleton("testGroup")).
                endOr().
                count());
            assertEquals(1, taskService.createTaskQuery().
                    taskName("testName").
                    taskInvolvedGroups(Collections.singleton("testGroup")).
                count());
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(4, historyService.createHistoricTaskInstanceQuery().
                    or().
                        taskName("testName").
                        taskInvolvedGroups(Collections.singleton("testGroup")).
                    endOr().
                    count());
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                        taskName("testName").
                        taskInvolvedGroups(Collections.singleton("testGroup")).
                    count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByCandidateGroupsTaskName() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setName("testName");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            taskService.saveTask(adhocTask3);
            org.flowable.task.api.Task adhocTask4 = taskService.newTask();
            adhocTask4.setName("testName");
            taskService.saveTask(adhocTask4);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", IdentityLinkType.CANDIDATE);
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", IdentityLinkType.CANDIDATE);
            taskService.addGroupIdentityLink(adhocTask4.getId(), "testGroup", IdentityLinkType.CANDIDATE);

            assertEquals(4, taskService.createTaskQuery().
                or().
                    taskName("testName").
                    taskCandidateGroupIn(Collections.singletonList("testGroup")).
                endOr().
                count());
            assertEquals(1, taskService.createTaskQuery().
                    taskName("testName").
                    taskCandidateGroupIn(Collections.singletonList("testGroup")).
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(4, historyService.createHistoricTaskInstanceQuery().
                        or().
                            taskName("testName").
                            taskCandidateGroupIn(Collections.singletonList("testGroup")).
                        endOr().
                        count());
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                    taskName("testName").
                    taskCandidateGroupIn(Collections.singletonList("testGroup")).
                    count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByCandidateUserTaskName() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setName("testName");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            taskService.saveTask(adhocTask3);
            org.flowable.task.api.Task adhocTask4 = taskService.newTask();
            adhocTask4.setName("testName");
            taskService.saveTask(adhocTask4);
            taskService.addUserIdentityLink(adhocTask.getId(), "homer", IdentityLinkType.CANDIDATE);
            taskService.addUserIdentityLink(adhocTask3.getId(), "homer", IdentityLinkType.CANDIDATE);
            taskService.addUserIdentityLink(adhocTask4.getId(), "homer", IdentityLinkType.CANDIDATE);

            assertEquals(4, taskService.createTaskQuery().
                or().
                    taskName("testName").
                    taskCandidateUser("homer").
                endOr().
                count());
            assertEquals(1, taskService.createTaskQuery().
                taskName("testName").
                taskCandidateUser("homer").
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(4, historyService.createHistoricTaskInstanceQuery().
                    or().
                        taskName("testName").
                        taskCandidateUser("homer").
                    endOr().
                    count());
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                    taskName("testName").
                    taskCandidateUser("homer").
                    count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByCandidateGroupTaskName() {
        try {
            org.flowable.task.api.Task adhocTask = taskService.newTask();
            taskService.saveTask(adhocTask);
            org.flowable.task.api.Task adhocTask2 = taskService.newTask();
            adhocTask2.setName("testName");
            taskService.saveTask(adhocTask2);
            org.flowable.task.api.Task adhocTask3 = taskService.newTask();
            taskService.saveTask(adhocTask3);
            org.flowable.task.api.Task adhocTask4 = taskService.newTask();
            adhocTask4.setName("testName");
            taskService.saveTask(adhocTask4);
            taskService.addGroupIdentityLink(adhocTask.getId(), "testGroup", IdentityLinkType.CANDIDATE);
            taskService.addGroupIdentityLink(adhocTask3.getId(), "testGroup", IdentityLinkType.CANDIDATE);
            taskService.addGroupIdentityLink(adhocTask4.getId(), "testGroup", IdentityLinkType.CANDIDATE);

            assertEquals(4, taskService.createTaskQuery().
                or().
                    taskName("testName").
                    taskCandidateGroup("testGroup").
                endOr().
                count());
            assertEquals(1, taskService.createTaskQuery().
                taskName("testName").
                taskCandidateGroup("testGroup").
                count());
            
            if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
                assertEquals(4, historyService.createHistoricTaskInstanceQuery().
                    or().
                        taskName("testName").
                        taskCandidateGroup("testGroup").
                    endOr().
                    count());
                assertEquals(1, historyService.createHistoricTaskInstanceQuery().
                    taskName("testName").
                    taskCandidateGroup("testGroup").
                    count());
            }

        } finally {
            deleteAllTasks();
        }
    }

    @Test
    public void testQueryByNullAssignee() {
        try {
            taskService.createTaskQuery().taskAssignee(null).list();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNullAssigneeOr() {
        try {
            taskService.createTaskQuery().or().taskId("invalid").taskAssignee(null).list();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByUnassigned() {
        TaskQuery query = taskService.createTaskQuery().taskUnassigned();
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());
    }

    @Test
    public void testQueryByUnassignedOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskUnassigned();
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());
    }

    @Test
    public void testQueryByCandidateUser() {
        TaskQuery query = taskService.createTaskQuery().taskCandidateUser("kermit");
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());
        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }

        query = taskService.createTaskQuery().taskCandidateUser("fozzie");
        assertEquals(3, query.count());
        assertEquals(3, query.list().size());
        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByCandidateUserOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateUser("kermit");
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());
        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }

        query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateUser("fozzie");
        assertEquals(3, query.count());
        assertEquals(3, query.list().size());
        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNullCandidateUser() {
        try {
            taskService.createTaskQuery().taskCandidateUser(null).list();
            fail();
        } catch (FlowableIllegalArgumentException e) {
        }
    }

    @Test
    public void testQueryByNullCandidateUserOr() {
        try {
            taskService.createTaskQuery().or().taskId("invalid").taskCandidateUser(null).list();
            fail();
        } catch (FlowableIllegalArgumentException e) {
        }
    }

    @Test
    public void testQueryByCandidateGroup() {
        TaskQuery query = taskService.createTaskQuery().taskCandidateGroup("management");
        assertEquals(3, query.count());
        assertEquals(3, query.list().size());
        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }

        query = taskService.createTaskQuery().taskCandidateGroup("sales");
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
    }

    @Test
    public void testQueryByCandidateGroupOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateGroup("management");
        assertEquals(3, query.count());
        assertEquals(3, query.list().size());
        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }

        query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateGroup("sales");
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
    }

    @Test
    public void testQueryByCandidateOrAssigned() {
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned("kermit");
        assertEquals(11, query.count());
        List<org.flowable.task.api.Task> tasks = query.list();
        assertEquals(11, tasks.size());

        // if dbIdentityUsed set false in process engine configuration of using
        // custom session factory of GroupIdentityManager
        ArrayList<String> candidateGroups = new ArrayList<>();
        candidateGroups.add("management");
        candidateGroups.add("accountancy");
        candidateGroups.add("noexist");
        query = taskService.createTaskQuery().taskCandidateGroupIn(candidateGroups).taskCandidateOrAssigned("kermit");
        assertEquals(11, query.count());
        tasks = query.list();
        assertEquals(11, tasks.size());

        query = taskService.createTaskQuery().taskCandidateOrAssigned("fozzie");
        assertEquals(3, query.count());
        assertEquals(3, query.list().size());

        // create a new task that no identity link and assignee to kermit
        org.flowable.task.api.Task task = taskService.newTask();
        task.setName("assigneeToKermit");
        task.setDescription("testTask description");
        task.setPriority(3);
        task.setAssignee("kermit");
        taskService.saveTask(task);

        query = taskService.createTaskQuery().taskCandidateOrAssigned("kermit");
        assertEquals(12, query.count());
        tasks = query.list();
        assertEquals(12, tasks.size());

        org.flowable.task.api.Task assigneeToKermit = taskService.createTaskQuery().taskName("assigneeToKermit").singleResult();
        taskService.deleteTask(assigneeToKermit.getId());
        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            historyService.deleteHistoricTaskInstance(assigneeToKermit.getId());
        }
    }

    @Test
    public void testQueryByCandidateOrAssignedOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateOrAssigned("kermit");
        assertEquals(11, query.count());
        List<org.flowable.task.api.Task> tasks = query.list();
        assertEquals(11, tasks.size());

        // if dbIdentityUsed set false in process engine configuration of using
        // custom session factory of GroupIdentityManager
        ArrayList<String> candidateGroups = new ArrayList<>();
        candidateGroups.add("management");
        candidateGroups.add("accountancy");
        candidateGroups.add("noexist");
        query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateGroupIn(candidateGroups).taskCandidateOrAssigned("kermit");
        assertEquals(11, query.count());
        tasks = query.list();
        assertEquals(11, tasks.size());

        query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateOrAssigned("fozzie");
        assertEquals(3, query.count());
        assertEquals(3, query.list().size());

        // create a new task that no identity link and assignee to kermit
        org.flowable.task.api.Task task = taskService.newTask();
        task.setName("assigneeToKermit");
        task.setDescription("testTask description");
        task.setPriority(3);
        task.setAssignee("kermit");
        taskService.saveTask(task);

        query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateOrAssigned("kermit");
        assertEquals(12, query.count());
        tasks = query.list();
        assertEquals(12, tasks.size());

        org.flowable.task.api.Task assigneeToKermit = taskService.createTaskQuery().or().taskId("invalid").taskName("assigneeToKermit").singleResult();
        taskService.deleteTask(assigneeToKermit.getId());
        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            historyService.deleteHistoricTaskInstance(assigneeToKermit.getId());
        }
    }
    
    @Test
    public void testQueryIgnoreAssigneeValue() {
        List<String> createdTasks = new ArrayList<>();
        Task kermitAssigneeTask = taskService.newTask();
        kermitAssigneeTask.setName("new kermit assignee task");
        taskService.saveTask(kermitAssigneeTask);
        taskService.setAssignee(kermitAssigneeTask.getId(), "kermit");
        createdTasks.add(kermitAssigneeTask.getId());

        Task magementTask = taskService.newTask();
        magementTask.setName("new management task");
        taskService.saveTask(magementTask);
        taskService.setAssignee(magementTask.getId(), "gozzie");
        taskService.addCandidateGroup(magementTask.getId(), "management");
        createdTasks.add(magementTask.getId());


        List<Task> kermitCandidateTasks = taskService.createTaskQuery()
                .taskCandidateUser("kermit")
                .taskName("testTask")
                .list();

        for (Task t : kermitCandidateTasks) {
            taskService.setAssignee(t.getId(), "gonzo");
        }

        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser("kermit")
                .list();
        assertEquals(5, tasks.size());

        tasks = taskService.createTaskQuery()
                .taskCandidateUser("kermit")
                .ignoreAssigneeValue()
                .list();
        assertEquals(12, tasks.size());

        tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned("kermit")
                .list();
        assertEquals(6, tasks.size());

        tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned("kermit")
                .ignoreAssigneeValue()
                .list();
        assertEquals(13, tasks.size());

        tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned("gonzo")
                .taskCandidateGroup("management")
                .list();
        assertEquals(10, tasks.size());

        tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned("gonzo")
                .taskCandidateGroup("management")
                .ignoreAssigneeValue()
                .list();
        assertEquals(11, tasks.size());

        taskService.deleteTasks(createdTasks, true);
    }

    @Test
    public void testQueryIgnoreAssigneeValueOr() {
        List<String> createdTasks = new ArrayList<>();
        Task kermitAssigneeTask = taskService.newTask();
        kermitAssigneeTask.setName("new kermit assignee task");
        taskService.saveTask(kermitAssigneeTask);
        taskService.setAssignee(kermitAssigneeTask.getId(), "kermit");
        createdTasks.add(kermitAssigneeTask.getId());

        Task magementTask = taskService.newTask();
        magementTask.setName("new management task");
        taskService.saveTask(magementTask);
        taskService.setAssignee(magementTask.getId(), "gozzie");
        taskService.addCandidateGroup(magementTask.getId(), "management");
        createdTasks.add(magementTask.getId());


        List<Task> kermitCandidateTasks = taskService.createTaskQuery()
                .taskCandidateUser("kermit")
                .taskName("testTask")
                .list();

        for (Task t : kermitCandidateTasks) {
            taskService.setAssignee(t.getId(), "gonzo");
        }

        List<Task> tasks = taskService.createTaskQuery()
                .or()
                .taskCandidateUser("kermit")
                .taskCandidateGroup("management")
                .endOr()
                .list();
        assertEquals(3, tasks.size());

        tasks = taskService.createTaskQuery()
                .or()
                .taskCandidateUser("kermit")
                .taskCandidateGroup("management")
                .ignoreAssigneeValue()
                .endOr()
                .list();
        assertEquals(10, tasks.size());

        tasks = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned("kermit")
                .endOr()
                .list();
        assertEquals(6, tasks.size());

        tasks = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned("kermit")
                .ignoreAssigneeValue()
                .endOr()
                .list();
        assertEquals(13, tasks.size());

        tasks = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned("gonzo")
                .taskCandidateGroup("management")
                .endOr()
                .list();
        assertEquals(10, tasks.size());

        tasks = taskService.createTaskQuery()
                .or()
                .taskCandidateOrAssigned("gonzo")
                .taskCandidateGroup("management")
                .ignoreAssigneeValue()
                .endOr()
                .list();
        assertEquals(11, tasks.size());

        taskService.deleteTasks(createdTasks, true);
    }

    @Test
    public void testQueryByNullCandidateGroup() {
        try {
            taskService.createTaskQuery().taskCandidateGroup(null).list();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNullCandidateGroupOr() {
        try {
            taskService.createTaskQuery().or().taskId("invalid").taskCandidateGroup(null).list();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByCandidateGroupIn() {
        List<String> groups = Arrays.asList("management", "accountancy");
        TaskQuery query = taskService.createTaskQuery().taskCandidateGroupIn(groups);
        assertEquals(5, query.count());
        assertEquals(5, query.list().size());

        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }

        query = taskService.createTaskQuery().taskCandidateUser("kermit").taskCandidateGroupIn(groups);
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());

        query = taskService.createTaskQuery().taskCandidateUser("kermit").taskCandidateGroup("unexisting");
        assertEquals(6, query.count());
        assertEquals(6, query.list().size());

        query = taskService.createTaskQuery().taskCandidateUser("unexisting").taskCandidateGroup("unexisting");
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());

        // Unexisting groups or groups that don't have candidate tasks shouldn't
        // influence other results
        groups = Arrays.asList("management", "accountancy", "sales", "unexising");
        query = taskService.createTaskQuery().taskCandidateGroupIn(groups);
        assertEquals(5, query.count());
        assertEquals(5, query.list().size());
    }

    @Test
    public void testQueryByCandidateGroupInOr() {
        List<String> groups = Arrays.asList("management", "accountancy");
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateGroupIn(groups);
        assertEquals(5, query.count());
        assertEquals(5, query.list().size());

        try {
            query.singleResult();
            fail("expected exception");
        } catch (FlowableException e) {
            // OK
        }

        query = taskService.createTaskQuery().or().taskCandidateUser("kermit").taskCandidateGroupIn(groups).endOr();
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());

        query = taskService.createTaskQuery().or().taskCandidateUser("kermit").taskCandidateGroup("unexisting").endOr();
        assertEquals(6, query.count());
        assertEquals(6, query.list().size());

        query = taskService.createTaskQuery().or().taskCandidateUser("unexisting").taskCandidateGroup("unexisting").endOr();
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());

        query = taskService.createTaskQuery().or().taskCandidateUser("kermit").taskCandidateGroupIn(groups).endOr()
                .or().taskCandidateUser("gonzo").taskCandidateGroupIn(groups);
        assertEquals(5, query.count());
        assertEquals(5, query.list().size());

        // Unexisting groups or groups that don't have candidate tasks shouldn't influence other results
        groups = Arrays.asList("management", "accountancy", "sales", "unexising");
        query = taskService.createTaskQuery().or().taskId("invalid").taskCandidateGroupIn(groups);
        assertEquals(5, query.count());
        assertEquals(5, query.list().size());
    }

    @Test
    public void testQueryByNullCandidateGroupIn() {
        try {
            taskService.createTaskQuery().taskCandidateGroupIn(null).list();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
        try {
            taskService.createTaskQuery().taskCandidateGroupIn(new ArrayList<>()).list();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByNullCandidateGroupInOr() {
        try {
            taskService.createTaskQuery().or().taskId("invalid").taskCandidateGroupIn(null).list();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
        try {
            taskService.createTaskQuery().or().taskId("invalid").taskCandidateGroupIn(new ArrayList<>()).list();
            fail("expected exception");
        } catch (FlowableIllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testQueryByDelegationState() {
        TaskQuery query = taskService.createTaskQuery().taskDelegationState(null);
        assertEquals(12, query.count());
        assertEquals(12, query.list().size());
        query = taskService.createTaskQuery().taskDelegationState(DelegationState.PENDING);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
        query = taskService.createTaskQuery().taskDelegationState(DelegationState.RESOLVED);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());

        String taskId = taskService.createTaskQuery().taskAssignee("gonzo").singleResult().getId();
        taskService.delegateTask(taskId, "kermit");

        query = taskService.createTaskQuery().taskDelegationState(null);
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());
        query = taskService.createTaskQuery().taskDelegationState(DelegationState.PENDING);
        assertEquals(1, query.count());
        assertEquals(1, query.list().size());
        query = taskService.createTaskQuery().taskDelegationState(DelegationState.RESOLVED);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());

        taskService.resolveTask(taskId);

        query = taskService.createTaskQuery().taskDelegationState(null);
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());
        query = taskService.createTaskQuery().taskDelegationState(DelegationState.PENDING);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
        query = taskService.createTaskQuery().taskDelegationState(DelegationState.RESOLVED);
        assertEquals(1, query.count());
        assertEquals(1, query.list().size());
    }

    @Test
    public void testQueryByDelegationStateOr() {
        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(null);
        assertEquals(12, query.count());
        assertEquals(12, query.list().size());
        query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(DelegationState.PENDING);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
        query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(DelegationState.RESOLVED);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());

        String taskId = taskService.createTaskQuery().or().taskId("invalid").taskAssignee("gonzo").singleResult().getId();
        taskService.delegateTask(taskId, "kermit");

        query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(null);
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());
        query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(DelegationState.PENDING);
        assertEquals(1, query.count());
        assertEquals(1, query.list().size());
        query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(DelegationState.RESOLVED);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());

        taskService.resolveTask(taskId);

        query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(null);
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());
        query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(DelegationState.PENDING);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
        query = taskService.createTaskQuery().or().taskId("invalid").taskDelegationState(DelegationState.RESOLVED);
        assertEquals(1, query.count());
        assertEquals(1, query.list().size());
    }

    @Test
    public void testQueryCreatedOn() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        // Exact matching of createTime, should result in 6 tasks
        Date createTime = sdf.parse("01/01/2001 01:01:01.000");

        TaskQuery query = taskService.createTaskQuery().taskCreatedOn(createTime);
        assertEquals(6, query.count());
        assertEquals(6, query.list().size());
    }

    @Test
    public void testQueryCreatedOnOr() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        // Exact matching of createTime, should result in 6 tasks
        Date createTime = sdf.parse("01/01/2001 01:01:01.000");

        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskCreatedOn(createTime);
        assertEquals(6, query.count());
        assertEquals(6, query.list().size());
    }

    @Test
    public void testQueryCreatedBefore() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        // Should result in 7 tasks
        Date before = sdf.parse("03/02/2002 02:02:02.000");

        TaskQuery query = taskService.createTaskQuery().taskCreatedBefore(before);
        assertEquals(7, query.count());
        assertEquals(7, query.list().size());

        before = sdf.parse("01/01/2001 01:01:01.000");
        query = taskService.createTaskQuery().taskCreatedBefore(before);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
    }

    @Test
    public void testQueryCreatedBeforeOr() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        // Should result in 7 tasks
        Date before = sdf.parse("03/02/2002 02:02:02.000");

        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskCreatedBefore(before);
        assertEquals(7, query.count());
        assertEquals(7, query.list().size());

        before = sdf.parse("01/01/2001 01:01:01.000");
        query = taskService.createTaskQuery().or().taskId("invalid").taskCreatedBefore(before);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
    }

    @Test
    public void testQueryCreatedAfter() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        // Should result in 3 tasks
        Date after = sdf.parse("03/03/2003 03:03:03.000");

        TaskQuery query = taskService.createTaskQuery().taskCreatedAfter(after);
        assertEquals(3, query.count());
        assertEquals(3, query.list().size());

        after = sdf.parse("05/05/2005 05:05:05.000");
        query = taskService.createTaskQuery().taskCreatedAfter(after);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
    }

    @Test
    public void testQueryCreatedAfterOr() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        // Should result in 3 tasks
        Date after = sdf.parse("03/03/2003 03:03:03.000");

        TaskQuery query = taskService.createTaskQuery().or().taskId("invalid").taskCreatedAfter(after);
        assertEquals(3, query.count());
        assertEquals(3, query.list().size());

        after = sdf.parse("05/05/2005 05:05:05.000");
        query = taskService.createTaskQuery().or().taskId("invalid").taskCreatedAfter(after);
        assertEquals(0, query.count());
        assertEquals(0, query.list().size());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/taskDefinitionProcess.bpmn20.xml")
    public void testTaskDefinitionKey() throws Exception {

        // Start process instance, 2 tasks will be available
        runtimeService.startProcessInstanceByKey("taskDefinitionKeyProcess");

        // 1 task should exist with key "taskKey1"
        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().taskDefinitionKey("taskKey1").list();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        assertEquals("taskKey1", tasks.get(0).getTaskDefinitionKey());

        // No task should be found with unexisting key
        Long count = taskService.createTaskQuery().taskDefinitionKey("unexistingKey").count();
        assertEquals(0L, count.longValue());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/taskDefinitionProcess.bpmn20.xml")
    public void testTaskDefinitionKeyOr() throws Exception {

        // Start process instance, 2 tasks will be available
        runtimeService.startProcessInstanceByKey("taskDefinitionKeyProcess");

        // 1 task should exist with key "taskKey1"
        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().or().taskId("invalid").taskDefinitionKey("taskKey1").list();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        assertEquals("taskKey1", tasks.get(0).getTaskDefinitionKey());

        // No task should be found with unexisting key
        Long count = taskService.createTaskQuery().or().taskId("invalid").taskDefinitionKey("unexistingKey").count();
        assertEquals(0L, count.longValue());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/taskDefinitionProcess.bpmn20.xml")
    public void testTaskDefinitionKeyLike() throws Exception {

        // Start process instance, 2 tasks will be available
        runtimeService.startProcessInstanceByKey("taskDefinitionKeyProcess");

        // Ends with matching, TaskKey1 and TaskKey123 match
        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().taskDefinitionKeyLike("taskKey1%").orderByTaskName().asc().list();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());

        assertEquals("taskKey1", tasks.get(0).getTaskDefinitionKey());
        assertEquals("taskKey123", tasks.get(1).getTaskDefinitionKey());

        // Starts with matching, TaskKey123 matches
        tasks = taskService.createTaskQuery().taskDefinitionKeyLike("%123").orderByTaskName().asc().list();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        assertEquals("taskKey123", tasks.get(0).getTaskDefinitionKey());

        // Contains matching, TaskKey123 matches
        tasks = taskService.createTaskQuery().taskDefinitionKeyLike("%Key12%").orderByTaskName().asc().list();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        assertEquals("taskKey123", tasks.get(0).getTaskDefinitionKey());

        // No task should be found with unexisting key
        Long count = taskService.createTaskQuery().taskDefinitionKeyLike("%unexistingKey%").count();
        assertEquals(0L, count.longValue());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/taskDefinitionProcess.bpmn20.xml")
    public void testTaskDefinitionKeyLikeOr() throws Exception {

        // Start process instance, 2 tasks will be available
        runtimeService.startProcessInstanceByKey("taskDefinitionKeyProcess");

        // Ends with matching, TaskKey1 and TaskKey123 match
        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().or().taskId("invalid").taskDefinitionKeyLike("taskKey1%").orderByTaskName().asc().list();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());

        assertEquals("taskKey1", tasks.get(0).getTaskDefinitionKey());
        assertEquals("taskKey123", tasks.get(1).getTaskDefinitionKey());

        // Starts with matching, TaskKey123 matches
        tasks = taskService.createTaskQuery().or().taskDefinitionKeyLike("%123").taskId("invalid").orderByTaskName().asc().list();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        assertEquals("taskKey123", tasks.get(0).getTaskDefinitionKey());

        // Contains matching, TaskKey123 matches
        tasks = taskService.createTaskQuery().or().taskDefinitionKeyLike("%Key12%").taskId("invalid").orderByTaskName().asc().list();
        assertNotNull(tasks);
        assertEquals(1, tasks.size());

        assertEquals("taskKey123", tasks.get(0).getTaskDefinitionKey());

        // No task should be found with unexisting key
        Long count = taskService.createTaskQuery().or().taskId("invalid").taskDefinitionKeyLike("%unexistingKey%").count();
        assertEquals(0L, count.longValue());
    }

    @Test
    @Deployment
    public void testTaskVariableValueEquals() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // No task should be found for an unexisting var
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("unexistingVar", "value").count());

        // Create a map with a variable for all default types
        Map<String, Object> variables = new HashMap<>();
        variables.put("longVar", 928374L);
        variables.put("shortVar", (short) 123);
        variables.put("integerVar", 1234);
        variables.put("stringVar", "stringValue");
        variables.put("booleanVar", true);
        Date date = Calendar.getInstance().getTime();
        variables.put("dateVar", date);
        variables.put("nullVar", null);

        taskService.setVariablesLocal(task.getId(), variables);

        // Test query matches
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals("longVar", 928374L).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals("shortVar", (short) 123).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals("integerVar", 1234).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals("stringVar", "stringValue").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals("booleanVar", true).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals("dateVar", date).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals("nullVar", null).count());

        // Test query for other values on existing variables
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("longVar", 999L).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("shortVar", (short) 999).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("integerVar", 999).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("stringVar", "999").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("booleanVar", false).count());
        Calendar otherDate = Calendar.getInstance();
        otherDate.add(Calendar.YEAR, 1);
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("dateVar", otherDate.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("nullVar", "999").count());

        // Test query for not equals
        assertEquals(1, taskService.createTaskQuery().taskVariableValueNotEquals("longVar", 999L).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueNotEquals("shortVar", (short) 999).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueNotEquals("integerVar", 999).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueNotEquals("stringVar", "999").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueNotEquals("booleanVar", false).count());

        // Test value-only variable equals
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals(928374L).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals((short) 123).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals(1234).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals("stringValue").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals(true).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals(date).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEquals(null).count());

        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals(999999L).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals((short) 999).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals(9999).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("unexistingstringvalue").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals(false).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals(otherDate.getTime()).count());

        assertEquals(1, taskService.createTaskQuery().taskVariableValueLike("stringVar", "string%").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueLike("stringVar", "String%").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueLike("stringVar", "%Value").count());

        assertEquals(1, taskService.createTaskQuery().taskVariableValueGreaterThan("integerVar", 1000).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueGreaterThan("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueGreaterThan("integerVar", 1240).count());

        assertEquals(1, taskService.createTaskQuery().taskVariableValueGreaterThanOrEqual("integerVar", 1000).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueGreaterThanOrEqual("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueGreaterThanOrEqual("integerVar", 1240).count());

        assertEquals(1, taskService.createTaskQuery().taskVariableValueLessThan("integerVar", 1240).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueLessThan("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueLessThan("integerVar", 1000).count());

        assertEquals(1, taskService.createTaskQuery().taskVariableValueLessThanOrEqual("integerVar", 1240).count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueLessThanOrEqual("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueLessThanOrEqual("integerVar", 1000).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testTaskVariableValueEquals.bpmn20.xml" })
    public void testTaskVariableValueEqualsOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // No task should be found for an unexisting var
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("unexistingVar", "value").count());

        // Create a map with a variable for all default types
        Map<String, Object> variables = new HashMap<>();
        variables.put("longVar", 928374L);
        variables.put("shortVar", (short) 123);
        variables.put("integerVar", 1234);
        variables.put("stringVar", "stringValue");
        variables.put("booleanVar", true);
        Date date = Calendar.getInstance().getTime();
        variables.put("dateVar", date);
        variables.put("nullVar", null);

        taskService.setVariablesLocal(task.getId(), variables);

        // Test query matches
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("longVar", 928374L).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("shortVar", (short) 123).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("integerVar", 1234).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("stringVar", "stringValue").count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("booleanVar", true).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("dateVar", date).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("nullVar", null).count());

        // Test query for other values on existing variables
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("longVar", 999L).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("shortVar", (short) 999).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("integerVar", 999).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("stringVar", "999").count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("booleanVar", false).count());
        Calendar otherDate = Calendar.getInstance();
        otherDate.add(Calendar.YEAR, 1);
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("dateVar", otherDate.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("nullVar", "999").count());

        // Test query for not equals
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueNotEquals("longVar", 999L).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueNotEquals("shortVar", (short) 999).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueNotEquals("integerVar", 999).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueNotEquals("stringVar", "999").count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueNotEquals("booleanVar", false).count());

        // Test value-only variable equals
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(928374L).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals((short) 123).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(1234).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("stringValue").count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(true).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(date).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(null).count());

        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(999999L).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals((short) 999).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(9999).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("unexistingstringvalue").count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(false).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals(otherDate.getTime()).count());

        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLike("stringVar", "string%").count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLike("stringVar", "String%").count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLike("stringVar", "%Value").count());

        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueGreaterThan("integerVar", 1000).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueGreaterThan("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueGreaterThan("integerVar", 1240).count());

        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueGreaterThanOrEqual("integerVar", 1000).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueGreaterThanOrEqual("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueGreaterThanOrEqual("integerVar", 1240).count());

        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLessThan("integerVar", 1240).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLessThan("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLessThan("integerVar", 1000).count());

        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLessThanOrEqual("integerVar", 1240).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLessThanOrEqual("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueLessThanOrEqual("integerVar", 1000).count());
    }

    @Test
    @Deployment
    public void testProcessVariableValueEquals() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("longVar", 928374L);
        variables.put("shortVar", (short) 123);
        variables.put("integerVar", 1234);
        variables.put("stringVar", "stringValue");
        variables.put("booleanVar", true);
        Date date = Calendar.getInstance().getTime();
        variables.put("dateVar", date);
        variables.put("nullVar", null);

        // Start process-instance with all types of variables
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        // Test query matches
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("longVar", 928374L).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("shortVar", (short) 123).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("integerVar", 1234).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("stringVar", "stringValue").count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("booleanVar", true).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("dateVar", date).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("nullVar", null).count());

        // Test query for other values on existing variables
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals("longVar", 999L).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals("shortVar", (short) 999).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals("integerVar", 999).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals("stringVar", "999").count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals("booleanVar", false).count());
        Calendar otherDate = Calendar.getInstance();
        otherDate.add(Calendar.YEAR, 1);
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals("dateVar", otherDate.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals("nullVar", "999").count());

        // Test querying for task variables don't match the process-variables
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("longVar", 928374L).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("shortVar", (short) 123).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("stringVar", "stringValue").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("booleanVar", true).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("dateVar", date).count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEquals("nullVar", null).count());

        // Test querying for task variables not equals
        assertEquals(1, taskService.createTaskQuery().processVariableValueNotEquals("longVar", 999L).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueNotEquals("shortVar", (short) 999).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueNotEquals("integerVar", 999).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueNotEquals("stringVar", "999").count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueNotEquals("booleanVar", false).count());

        // and query for the existing variable with NOT should result in nothing
        // found:
        assertEquals(0, taskService.createTaskQuery().processVariableValueNotEquals("longVar", 928374L).count());

        // Test value-only variable equals
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals(928374L).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals((short) 123).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals(1234).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("stringValue").count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals(true).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals(date).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals(null).count());

        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals(999999L).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals((short) 999).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals(9999).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals("unexistingstringvalue").count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals(false).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEquals(otherDate.getTime()).count());

        // Test combination of task-variable and process-variable
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.setVariableLocal(task.getId(), "taskVar", "theValue");
        taskService.setVariableLocal(task.getId(), "longVar", 928374L);

        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("longVar", 928374L).taskVariableValueEquals("taskVar", "theValue").count());

        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals("longVar", 928374L).taskVariableValueEquals("longVar", 928374L).count());

        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals(928374L).taskVariableValueEquals("theValue").count());

        assertEquals(1, taskService.createTaskQuery().processVariableValueEquals(928374L).taskVariableValueEquals(928374L).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessVariableValueEquals.bpmn20.xml" })
    public void testProcessVariableValueEqualsOn() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("longVar", 928374L);
        variables.put("shortVar", (short) 123);
        variables.put("integerVar", 1234);
        variables.put("stringVar", "stringValue");
        variables.put("booleanVar", true);
        Date date = Calendar.getInstance().getTime();
        variables.put("dateVar", date);
        variables.put("nullVar", null);

        // Start process-instance with all types of variables
        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        // Test query matches
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("longVar", 928374L).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("shortVar", (short) 123).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("integerVar", 1234).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("stringVar", "stringValue").count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("booleanVar", true).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("dateVar", date).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("nullVar", null).count());

        // Test query for other values on existing variables
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("longVar", 999L).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("shortVar", (short) 999).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("integerVar", 999).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("stringVar", "999").count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("booleanVar", false).count());
        Calendar otherDate = Calendar.getInstance();
        otherDate.add(Calendar.YEAR, 1);
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("dateVar", otherDate.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("nullVar", "999").count());

        // Test querying for task variables don't match the process-variables
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("longVar", 928374L).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("shortVar", (short) 123).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("integerVar", 1234).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("stringVar", "stringValue").count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("booleanVar", true).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("dateVar", date).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").taskVariableValueEquals("nullVar", null).count());

        // Test querying for task variables not equals
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueNotEquals("longVar", 999L).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueNotEquals("shortVar", (short) 999).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueNotEquals("integerVar", 999).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueNotEquals("stringVar", "999").count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueNotEquals("booleanVar", false).count());

        // and query for the existing variable with NOT should result in nothing
        // found:
        assertEquals(0, taskService.createTaskQuery().processVariableValueNotEquals("longVar", 928374L).count());

        // Test value-only variable equals
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(928374L).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals((short) 123).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(1234).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("stringValue").count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(true).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(date).count());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(null).count());

        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(999999L).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals((short) 999).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(9999).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals("unexistingstringvalue").count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(false).count());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processVariableValueEquals(otherDate.getTime()).count());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml")
    public void testVariableValueEqualsIgnoreCase() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertNotNull(task);

        Map<String, Object> variables = new HashMap<>();
        variables.put("mixed", "AzerTY");
        variables.put("upper", "AZERTY");
        variables.put("lower", "azerty");
        taskService.setVariablesLocal(task.getId(), variables);

        assertEquals(1, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("mixed", "azerTY").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("mixed", "azerty").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("mixed", "uiop").count());

        assertEquals(1, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("upper", "azerTY").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("upper", "azerty").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("upper", "uiop").count());

        assertEquals(1, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("lower", "azerTY").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("lower", "azerty").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueEqualsIgnoreCase("lower", "uiop").count());

        // Test not-equals
        assertEquals(0, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("mixed", "azerTY").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("mixed", "azerty").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("mixed", "uiop").count());

        assertEquals(0, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("upper", "azerTY").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("upper", "azerty").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("upper", "uiop").count());

        assertEquals(0, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("lower", "azerTY").count());
        assertEquals(0, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("lower", "azerty").count());
        assertEquals(1, taskService.createTaskQuery().taskVariableValueNotEqualsIgnoreCase("lower", "uiop").count());

    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml")
    public void testProcessVariableValueEqualsIgnoreCase() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("mixed", "AzerTY");
        variables.put("upper", "AZERTY");
        variables.put("lower", "azerty");

        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        assertEquals(1, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("mixed", "azerTY").count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("mixed", "azerty").count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("mixed", "uiop").count());

        assertEquals(1, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("upper", "azerTY").count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("upper", "azerty").count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("upper", "uiop").count());

        assertEquals(1, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("lower", "azerTY").count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("lower", "azerty").count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueEqualsIgnoreCase("lower", "uiop").count());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml")
    public void testProcessVariableValueLike() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("mixed", "AzerTY");

        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        assertEquals(1, taskService.createTaskQuery().processVariableValueLike("mixed", "Azer%").count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueLike("mixed", "A%").count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueLike("mixed", "a%").count());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml")
    public void testProcessVariableValueLikeIgnoreCase() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("mixed", "AzerTY");

        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        assertEquals(1, taskService.createTaskQuery().processVariableValueLikeIgnoreCase("mixed", "azer%").count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueLikeIgnoreCase("mixed", "a%").count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueLikeIgnoreCase("mixed", "Azz%").count());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml")
    public void testProcessVariableValueGreaterThan() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("number", 10);

        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        assertEquals(1, taskService.createTaskQuery().processVariableValueGreaterThan("number", 5).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueGreaterThan("number", 10).count());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml")
    public void testProcessVariableValueGreaterThanOrEquals() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("number", 10);

        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        assertEquals(1, taskService.createTaskQuery().processVariableValueGreaterThanOrEqual("number", 5).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueGreaterThanOrEqual("number", 10).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueGreaterThanOrEqual("number", 11).count());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml")
    public void testProcessVariableValueLessThan() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("number", 10);

        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        assertEquals(1, taskService.createTaskQuery().processVariableValueLessThan("number", 12).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueLessThan("number", 10).count());
    }

    @Test
    @Deployment(resources = "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml")
    public void testProcessVariableValueLessThanOrEquals() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("number", 10);

        runtimeService.startProcessInstanceByKey("oneTaskProcess", variables);

        assertEquals(1, taskService.createTaskQuery().processVariableValueLessThanOrEqual("number", 12).count());
        assertEquals(1, taskService.createTaskQuery().processVariableValueLessThanOrEqual("number", 10).count());
        assertEquals(0, taskService.createTaskQuery().processVariableValueLessThanOrEqual("number", 8).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessDefinitionId() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).list();
        assertEquals(1, tasks.size());
        assertEquals(processInstance.getId(), tasks.get(0).getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().processDefinitionId("unexisting").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessDefinitionIdOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().or().taskId("invalid").processDefinitionId(processInstance.getProcessDefinitionId()).list();
        assertEquals(1, tasks.size());
        assertEquals(processInstance.getId(), tasks.get(0).getProcessInstanceId());

        tasks = taskService.createTaskQuery()
                .or()
                .taskId("invalid")
                .processDefinitionId(processInstance.getProcessDefinitionId())
                .endOr()
                .or()
                .processDefinitionKey("oneTaskProcess")
                .processDefinitionId("invalid")
                .endOr()
                .list();
        assertEquals(1, tasks.size());
        assertEquals(processInstance.getId(), tasks.get(0).getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery()
                .or()
                .taskId("invalid")
                .processDefinitionId("unexisting").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessDefinitionKey() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().processDefinitionKey("oneTaskProcess").list();
        assertEquals(1, tasks.size());
        assertEquals(processInstance.getId(), tasks.get(0).getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().processDefinitionKey("unexisting").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessDefinitionKeyOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().or().taskId("invalid").processDefinitionKey("oneTaskProcess").list();
        assertEquals(1, tasks.size());
        assertEquals(processInstance.getId(), tasks.get(0).getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processDefinitionKey("unexisting").count());

        assertEquals(1, taskService.createTaskQuery().or().taskId(taskIds.get(0)).processDefinitionKey("unexisting").endOr().count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessDefinitionKeyIn() throws Exception {
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        List<String> includeIds = new ArrayList<>();

        assertEquals(13, taskService.createTaskQuery().processDefinitionKeyIn(includeIds).count());
        includeIds.add("unexisting");
        assertEquals(0, taskService.createTaskQuery().processDefinitionKeyIn(includeIds).count());
        includeIds.add("oneTaskProcess");
        assertEquals(1, taskService.createTaskQuery().processDefinitionKeyIn(includeIds).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessDefinitionKeyInOr() throws Exception {
        runtimeService.startProcessInstanceByKey("oneTaskProcess");

        List<String> includeIds = new ArrayList<>();
        assertEquals(0, taskService.createTaskQuery()
                .or().taskId("invalid")
                .processDefinitionKeyIn(includeIds)
                .count());

        includeIds.add("unexisting");
        assertEquals(0, taskService.createTaskQuery()
                .or().taskId("invalid")
                .processDefinitionKeyIn(includeIds)
                .count());

        includeIds.add("oneTaskProcess");
        assertEquals(1, taskService.createTaskQuery()
                .or().taskId("invalid")
                .processDefinitionKeyIn(includeIds)
                .count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessDefinitionName() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().processDefinitionName("The One Task Process").list();
        assertEquals(1, tasks.size());
        assertEquals(processInstance.getId(), tasks.get(0).getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().processDefinitionName("unexisting").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessDefinitionNameOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().or().taskId("invalid").processDefinitionName("The One Task Process").list();
        assertEquals(1, tasks.size());
        assertEquals(processInstance.getId(), tasks.get(0).getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processDefinitionName("unexisting").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessCategoryIn() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        final org.flowable.task.api.Task task = taskService.createTaskQuery().processCategoryIn(Collections.singletonList("Examples")).singleResult();
        assertNotNull(task);
        assertEquals("theTask", task.getTaskDefinitionKey());
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().processCategoryIn(Collections.singletonList("unexisting")).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessCategoryInOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        org.flowable.task.api.Task task = taskService.createTaskQuery()
                .or()
                .taskId("invalid")
                .processCategoryIn(Collections.singletonList("Examples")).singleResult();
        assertNotNull(task);
        assertEquals("theTask", task.getTaskDefinitionKey());
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        task = taskService.createTaskQuery()
                .or()
                .taskId("invalid")
                .processCategoryIn(Collections.singletonList("Examples"))
                .endOr()
                .or()
                .taskId(task.getId())
                .processCategoryIn(Collections.singletonList("Examples2"))
                .endOr()
                .singleResult();
        assertNotNull(task);
        assertEquals("theTask", task.getTaskDefinitionKey());
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processCategoryIn(Collections.singletonList("unexisting")).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessCategoryNotIn() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        final org.flowable.task.api.Task task = taskService.createTaskQuery().processCategoryNotIn(Collections.singletonList("unexisting")).singleResult();
        assertNotNull(task);
        assertEquals("theTask", task.getTaskDefinitionKey());
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().processCategoryNotIn(Collections.singletonList("Examples")).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessCategoryNotInOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        final org.flowable.task.api.Task task = taskService.createTaskQuery().or().taskId("invalid").processCategoryNotIn(Collections.singletonList("unexisting")).singleResult();
        assertNotNull(task);
        assertEquals("theTask", task.getTaskDefinitionKey());
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processCategoryNotIn(Collections.singletonList("Examples")).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessInstanceIdIn() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        final org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceIdIn(Collections.singletonList(processInstance.getId())).singleResult();
        assertNotNull(task);
        assertEquals("theTask", task.getTaskDefinitionKey());
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().processInstanceIdIn(Collections.singletonList("unexisting")).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessInstanceIdInOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        final org.flowable.task.api.Task task = taskService.createTaskQuery().or().taskId("invalid").processInstanceIdIn(Collections.singletonList(
                processInstance.getId())).singleResult();
        assertNotNull(task);
        assertEquals("theTask", task.getTaskDefinitionKey());
        assertEquals(processInstance.getId(), task.getProcessInstanceId());

        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processInstanceIdIn(Collections.singletonList("unexisting")).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessInstanceIdInMultiple() throws Exception {
        ProcessInstance processInstance1 = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        ProcessInstance processInstance2 = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        assertEquals(2, taskService.createTaskQuery().processInstanceIdIn(Arrays.asList(processInstance1.getId(), processInstance2.getId())).count());
        assertEquals(2, taskService.createTaskQuery().processInstanceIdIn(Arrays.asList(processInstance1.getId(), processInstance2.getId(), "unexisting")).count());

        assertEquals(0, taskService.createTaskQuery().processInstanceIdIn(Arrays.asList("unexisting1", "unexisting2")).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessInstanceIdInOrMultiple() throws Exception {
        ProcessInstance processInstance1 = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        ProcessInstance processInstance2 = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        assertEquals(2, taskService.createTaskQuery().or().taskId("invalid").processInstanceIdIn(Arrays.asList(processInstance1.getId(), processInstance2.getId())).count());
        assertEquals(2, taskService.createTaskQuery().or().taskId("invalid").processInstanceIdIn(Arrays.asList(processInstance1.getId(), processInstance2.getId(), "unexisting")).count());

        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processInstanceIdIn(Arrays.asList("unexisting1", "unexisting2")).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessInstanceBusinessKey() throws Exception {
        runtimeService.startProcessInstanceByKey("oneTaskProcess", "BUSINESS-KEY-1");

        assertEquals(1, taskService.createTaskQuery().processDefinitionName("The One Task Process").processInstanceBusinessKey("BUSINESS-KEY-1").list().size());
        assertEquals(1, taskService.createTaskQuery().processInstanceBusinessKey("BUSINESS-KEY-1").list().size());
        assertEquals(0, taskService.createTaskQuery().processInstanceBusinessKey("NON-EXISTING").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testProcessInstanceBusinessKeyOr() throws Exception {
        runtimeService.startProcessInstanceByKey("oneTaskProcess", "BUSINESS-KEY-1");

        assertEquals(1, taskService.createTaskQuery().processDefinitionName("The One Task Process").or().taskId("invalid").processInstanceBusinessKey("BUSINESS-KEY-1").list().size());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").processInstanceBusinessKey("BUSINESS-KEY-1").list().size());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").processInstanceBusinessKey("NON-EXISTING").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testTaskDueDate() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // Set due-date on task
        Date dueDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/02/2003 01:12:13");
        task.setDueDate(dueDate);
        taskService.saveTask(task);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueDate(dueDate).count());

        Calendar otherDate = Calendar.getInstance();
        otherDate.add(Calendar.YEAR, 1);
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueDate(otherDate.getTime()).count());

        Calendar priorDate = Calendar.getInstance();
        priorDate.setTime(dueDate);
        priorDate.roll(Calendar.YEAR, -1);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueAfter(priorDate.getTime()).count());

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueBefore(otherDate.getTime()).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testTaskDueDateOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // Set due-date on task
        Date dueDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/02/2003 01:12:13");
        task.setDueDate(dueDate);
        taskService.saveTask(task);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueDate(dueDate).count());

        Calendar otherDate = Calendar.getInstance();
        otherDate.add(Calendar.YEAR, 1);
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueDate(otherDate.getTime()).count());

        Calendar priorDate = Calendar.getInstance();
        priorDate.setTime(dueDate);
        priorDate.roll(Calendar.YEAR, -1);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueAfter(priorDate.getTime()).count());

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueBefore(otherDate.getTime()).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testTaskDueBefore() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // Set due-date on task
        Calendar dueDateCal = Calendar.getInstance();
        task.setDueDate(dueDateCal.getTime());
        taskService.saveTask(task);

        Calendar oneHourAgo = Calendar.getInstance();
        oneHourAgo.setTime(dueDateCal.getTime());
        oneHourAgo.add(Calendar.HOUR, -1);

        Calendar oneHourLater = Calendar.getInstance();
        oneHourLater.setTime(dueDateCal.getTime());
        oneHourLater.add(Calendar.HOUR, 1);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueBefore(oneHourLater.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueBefore(oneHourAgo.getTime()).count());

        // Update due-date to null, shouldn't show up anymore in query that
        // matched before
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        task.setDueDate(null);
        taskService.saveTask(task);

        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueBefore(oneHourLater.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueBefore(oneHourAgo.getTime()).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testTaskDueBeforeOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // Set due-date on task
        Calendar dueDateCal = Calendar.getInstance();
        task.setDueDate(dueDateCal.getTime());
        taskService.saveTask(task);

        Calendar oneHourAgo = Calendar.getInstance();
        oneHourAgo.setTime(dueDateCal.getTime());
        oneHourAgo.add(Calendar.HOUR, -1);

        Calendar oneHourLater = Calendar.getInstance();
        oneHourLater.setTime(dueDateCal.getTime());
        oneHourLater.add(Calendar.HOUR, 1);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueBefore(oneHourLater.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueBefore(oneHourAgo.getTime()).count());

        // Update due-date to null, shouldn't show up anymore in query that
        // matched before
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        task.setDueDate(null);
        taskService.saveTask(task);

        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueBefore(oneHourLater.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueBefore(oneHourAgo.getTime()).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testTaskDueAfter() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // Set due-date on task
        Calendar dueDateCal = Calendar.getInstance();
        task.setDueDate(dueDateCal.getTime());
        taskService.saveTask(task);

        Calendar oneHourAgo = Calendar.getInstance();
        oneHourAgo.setTime(dueDateCal.getTime());
        oneHourAgo.add(Calendar.HOUR, -1);

        Calendar oneHourLater = Calendar.getInstance();
        oneHourLater.setTime(dueDateCal.getTime());
        oneHourLater.add(Calendar.HOUR, 1);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueAfter(oneHourAgo.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueAfter(oneHourLater.getTime()).count());

        // Update due-date to null, shouldn't show up anymore in query that
        // matched before
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        task.setDueDate(null);
        taskService.saveTask(task);

        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueAfter(oneHourLater.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDueAfter(oneHourAgo.getTime()).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testTaskDueAfterOn() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // Set due-date on task
        Calendar dueDateCal = Calendar.getInstance();
        task.setDueDate(dueDateCal.getTime());
        taskService.saveTask(task);

        Calendar oneHourAgo = Calendar.getInstance();
        oneHourAgo.setTime(dueDateCal.getTime());
        oneHourAgo.add(Calendar.HOUR, -1);

        Calendar oneHourLater = Calendar.getInstance();
        oneHourLater.setTime(dueDateCal.getTime());
        oneHourLater.add(Calendar.HOUR, 1);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueAfter(oneHourAgo.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueAfter(oneHourLater.getTime()).count());

        // Update due-date to null, shouldn't show up anymore in query that
        // matched before
        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        task.setDueDate(null);
        taskService.saveTask(task);

        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueAfter(oneHourLater.getTime()).count());
        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").taskDueAfter(oneHourAgo.getTime()).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testTaskWithoutDueDate() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).withoutTaskDueDate().singleResult();

        // Set due-date on task
        Date dueDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/02/2003 01:12:13");
        task.setDueDate(dueDate);
        taskService.saveTask(task);

        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).withoutTaskDueDate().count());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // Clear due-date on task
        task.setDueDate(null);
        taskService.saveTask(task);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).withoutTaskDueDate().count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testTaskWithoutDueDateOr() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").withoutTaskDueDate().singleResult();

        // Set due-date on task
        Date dueDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("01/02/2003 01:12:13");
        task.setDueDate(dueDate);
        taskService.saveTask(task);

        assertEquals(0, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").withoutTaskDueDate().count());

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        // Clear due-date on task
        task.setDueDate(null);
        taskService.saveTask(task);

        assertEquals(1, taskService.createTaskQuery().processInstanceId(processInstance.getId()).or().taskId("invalid").withoutTaskDueDate().count());
    }

    @Test
    public void testQueryPaging() {
        TaskQuery query = taskService.createTaskQuery().taskCandidateUser("kermit");

        assertEquals(11, query.listPage(0, Integer.MAX_VALUE).size());

        // Verifying the un-paged results
        assertEquals(11, query.count());
        assertEquals(11, query.list().size());

        // Verifying paged results
        assertEquals(2, query.listPage(0, 2).size());
        assertEquals(2, query.listPage(2, 2).size());
        assertEquals(3, query.listPage(4, 3).size());
        assertEquals(1, query.listPage(10, 3).size());
        assertEquals(1, query.listPage(10, 1).size());

        // Verifying odd usages
        assertEquals(0, query.listPage(0, 0).size());
        assertEquals(0, query.listPage(11, 2).size()); // 10 is the last index
                                                       // with a result
        assertEquals(11, query.listPage(0, 15).size()); // there are only 11
                                                        // tasks
    }

    @Test
    public void testQuerySorting() {
        assertEquals(12, taskService.createTaskQuery().orderByTaskId().asc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskName().asc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskPriority().asc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskAssignee().asc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskDescription().asc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByProcessInstanceId().asc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByExecutionId().asc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskCreateTime().asc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskDueDate().asc().list().size());

        assertEquals(12, taskService.createTaskQuery().orderByTaskId().desc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskName().desc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskPriority().desc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskAssignee().desc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskDescription().desc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByProcessInstanceId().desc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByExecutionId().desc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskCreateTime().desc().list().size());
        assertEquals(12, taskService.createTaskQuery().orderByTaskDueDate().desc().list().size());

        assertEquals(6, taskService.createTaskQuery().orderByTaskId().taskName("testTask").asc().list().size());
        assertEquals(6, taskService.createTaskQuery().orderByTaskId().taskName("testTask").desc().list().size());
    }

    @Test
    public void testNativeQueryPaging() {
        assertEquals("ACT_RU_TASK", managementService.getTableName(org.flowable.task.api.Task.class));
        assertEquals("ACT_RU_TASK", managementService.getTableName(TaskEntity.class));
        assertEquals(5, taskService.createNativeTaskQuery().sql("SELECT * FROM " + managementService.getTableName(org.flowable.task.api.Task.class)).listPage(0, 5).size());
        assertEquals(2, taskService.createNativeTaskQuery().sql("SELECT * FROM " + managementService.getTableName(org.flowable.task.api.Task.class)).listPage(10, 12).size());
    }

    @Test
    public void testNativeQuery() {
        assertEquals("ACT_RU_TASK", managementService.getTableName(org.flowable.task.api.Task.class));
        assertEquals("ACT_RU_TASK", managementService.getTableName(TaskEntity.class));
        assertEquals(12, taskService.createNativeTaskQuery().sql("SELECT * FROM " + managementService.getTableName(org.flowable.task.api.Task.class)).list().size());
        assertEquals(12, taskService.createNativeTaskQuery().sql("SELECT count(*) FROM " + managementService.getTableName(org.flowable.task.api.Task.class)).count());

        assertEquals(144, taskService.createNativeTaskQuery().sql("SELECT count(*) FROM ACT_RU_TASK T1, ACT_RU_TASK T2").count());

        // join task and variable instances
        assertEquals(
                1,
                taskService.createNativeTaskQuery()
                        .sql("SELECT count(*) FROM " + managementService.getTableName(org.flowable.task.api.Task.class) + " T1, " + managementService.getTableName(VariableInstanceEntity.class) + " V1 WHERE V1.TASK_ID_ = T1.ID_")
                        .count());
        List<org.flowable.task.api.Task> tasks = taskService.createNativeTaskQuery()
                .sql("SELECT * FROM " + managementService.getTableName(org.flowable.task.api.Task.class) + " T1, " + managementService.getTableName(VariableInstanceEntity.class) + " V1 WHERE V1.TASK_ID_ = T1.ID_").list();
        assertEquals(1, tasks.size());
        assertEquals("gonzoTask", tasks.get(0).getName());

        // select with distinct
        assertEquals(12, taskService.createNativeTaskQuery().sql("SELECT DISTINCT T1.* FROM ACT_RU_TASK T1").list().size());

        assertEquals(1, taskService.createNativeTaskQuery().sql("SELECT count(*) FROM " + managementService.getTableName(org.flowable.task.api.Task.class) + " T WHERE T.NAME_ = 'gonzoTask'").count());
        assertEquals(1, taskService.createNativeTaskQuery().sql("SELECT * FROM " + managementService.getTableName(org.flowable.task.api.Task.class) + " T WHERE T.NAME_ = 'gonzoTask'").list().size());

        // use parameters
        assertEquals(1, taskService.createNativeTaskQuery().sql("SELECT count(*) FROM " + managementService.getTableName(org.flowable.task.api.Task.class) + " T WHERE T.NAME_ = #{taskName}").parameter("taskName", "gonzoTask")
                .count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testIncludeIdentityLinks() throws Exception {
        // Start process with a binary variable
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess", Collections.singletonMap("binaryVariable", (Object) "It is I, le binary".getBytes()));
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertNotNull(task);
        taskService.setVariableLocal(task.getId(), "binaryTaskVariable", (Object) "It is I, le binary".getBytes());

        taskService.addCandidateGroup(task.getId(), "group1");

        // Query task, including identity links
        task = taskService.createTaskQuery().taskId(task.getId()).includeIdentityLinks().singleResult();
        assertNotNull(task);
        assertNotNull(task.getIdentityLinks());
        assertEquals(1, task.getIdentityLinks().size());

        // Query task, including identity links, process variables, and task variables
        task = taskService.createTaskQuery().taskId(task.getId()).includeIdentityLinks().includeProcessVariables().includeTaskLocalVariables().singleResult();
        assertNotNull(task);
        assertNotNull(task.getIdentityLinks());
        assertEquals(1, task.getIdentityLinks().size());
        IdentityLinkInfo identityLink = task.getIdentityLinks().get(0);
        assertNull(identityLink.getProcessInstanceId());
        assertEquals("candidate", identityLink.getType());
        assertEquals("group1", identityLink.getGroupId());
        assertNull(identityLink.getUserId());
        assertEquals(task.getId(), identityLink.getTaskId());

        assertNotNull(task.getProcessVariables());
        byte[] bytes = (byte[]) task.getProcessVariables().get("binaryVariable");
        assertEquals("It is I, le binary", new String(bytes));

        assertNotNull(task.getTaskLocalVariables());
        bytes = (byte[]) task.getTaskLocalVariables().get("binaryTaskVariable");
        assertEquals("It is I, le binary", new String(bytes));
    }

    /**
     * Test confirming fix for ACT-1731
     */
    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testIncludeBinaryVariables() throws Exception {
        // Start process with a binary variable
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess", Collections.singletonMap("binaryVariable", (Object) "It is I, le binary".getBytes()));
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertNotNull(task);
        taskService.setVariableLocal(task.getId(), "binaryTaskVariable", (Object) "It is I, le binary".getBytes());

        // Query task, including processVariables
        task = taskService.createTaskQuery().taskId(task.getId()).includeProcessVariables().singleResult();
        assertNotNull(task);
        assertNotNull(task.getProcessVariables());
        byte[] bytes = (byte[]) task.getProcessVariables().get("binaryVariable");
        assertEquals("It is I, le binary", new String(bytes));

        // Query task, including taskVariables
        task = taskService.createTaskQuery().taskId(task.getId()).includeTaskLocalVariables().singleResult();
        assertNotNull(task);
        assertNotNull(task.getTaskLocalVariables());
        bytes = (byte[]) task.getTaskLocalVariables().get("binaryTaskVariable");
        assertEquals("It is I, le binary", new String(bytes));
    }

    /**
     * Test confirming fix for ACT-1731
     */
    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testIncludeBinaryVariablesOr() throws Exception {
        // Start process with a binary variable
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess", Collections.singletonMap("binaryVariable", (Object) "It is I, le binary".getBytes()));
        org.flowable.task.api.Task task = taskService.createTaskQuery().or().taskName("invalid").processInstanceId(processInstance.getId()).singleResult();
        assertNotNull(task);
        taskService.setVariableLocal(task.getId(), "binaryTaskVariable", (Object) "It is I, le binary".getBytes());

        // Query task, including processVariables
        task = taskService.createTaskQuery().or().taskName("invalid").taskId(task.getId()).includeProcessVariables().singleResult();
        assertNotNull(task);
        assertNotNull(task.getProcessVariables());
        byte[] bytes = (byte[]) task.getProcessVariables().get("binaryVariable");
        assertEquals("It is I, le binary", new String(bytes));

        // Query task, including taskVariables
        task = taskService.createTaskQuery().or().taskName("invalid").taskId(task.getId()).includeTaskLocalVariables().singleResult();
        assertNotNull(task);
        assertNotNull(task.getTaskLocalVariables());
        bytes = (byte[]) task.getTaskLocalVariables().get("binaryTaskVariable");
        assertEquals("It is I, le binary", new String(bytes));
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testQueryByDeploymentId() throws Exception {
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        assertNotNull(taskService.createTaskQuery().deploymentId(deployment.getId()).singleResult());
        assertEquals(1, taskService.createTaskQuery().deploymentId(deployment.getId()).count());
        assertNull(taskService.createTaskQuery().deploymentId("invalid").singleResult());
        assertEquals(0, taskService.createTaskQuery().deploymentId("invalid").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testQueryByDeploymentIdOr() throws Exception {
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        assertNotNull(taskService.createTaskQuery().or().taskId("invalid").deploymentId(deployment.getId()).singleResult());
        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").deploymentId(deployment.getId()).count());
        assertNull(taskService.createTaskQuery().deploymentId("invalid").singleResult());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").deploymentId("invalid").count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testQueryByDeploymentIdIn() throws Exception {
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        List<String> deploymentIds = new ArrayList<>();
        deploymentIds.add(deployment.getId());
        assertNotNull(taskService.createTaskQuery().deploymentIdIn(deploymentIds).singleResult());
        assertEquals(1, taskService.createTaskQuery().deploymentIdIn(deploymentIds).count());

        deploymentIds.add("invalid");
        assertNotNull(taskService.createTaskQuery().deploymentIdIn(deploymentIds).singleResult());
        assertEquals(1, taskService.createTaskQuery().deploymentIdIn(deploymentIds).count());

        deploymentIds = new ArrayList<>();
        deploymentIds.add("invalid");
        assertNull(taskService.createTaskQuery().deploymentIdIn(deploymentIds).singleResult());
        assertEquals(0, taskService.createTaskQuery().deploymentIdIn(deploymentIds).count());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testQueryByDeploymentIdInOr() throws Exception {
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        List<String> deploymentIds = new ArrayList<>();
        deploymentIds.add(deployment.getId());
        assertNotNull(taskService.createTaskQuery().or().taskId("invalid").deploymentIdIn(deploymentIds).singleResult());

        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").deploymentIdIn(deploymentIds).count());

        deploymentIds.add("invalid");
        assertNotNull(taskService.createTaskQuery().or().taskId("invalid").deploymentIdIn(deploymentIds).singleResult());

        assertEquals(1, taskService.createTaskQuery().or().taskId("invalid").deploymentIdIn(deploymentIds).count());

        deploymentIds = new ArrayList<>();
        deploymentIds.add("invalid");
        assertNull(taskService.createTaskQuery().deploymentIdIn(deploymentIds).singleResult());
        assertEquals(0, taskService.createTaskQuery().or().taskId("invalid").deploymentIdIn(deploymentIds).count());
    }

    @Test
    public void testQueryByTaskNameLikeIgnoreCase() {

        // Runtime
        assertEquals(12, taskService.createTaskQuery().taskNameLikeIgnoreCase("%task%").count());
        assertEquals(12, taskService.createTaskQuery().taskNameLikeIgnoreCase("%Task%").count());
        assertEquals(12, taskService.createTaskQuery().taskNameLikeIgnoreCase("%TASK%").count());
        assertEquals(12, taskService.createTaskQuery().taskNameLikeIgnoreCase("%TasK%").count());
        assertEquals(1, taskService.createTaskQuery().taskNameLikeIgnoreCase("gonzo%").count());
        assertEquals(1, taskService.createTaskQuery().taskNameLikeIgnoreCase("%Gonzo%").count());
        assertEquals(0, taskService.createTaskQuery().taskNameLikeIgnoreCase("Task%").count());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(12, historyService.createHistoricTaskInstanceQuery().taskNameLikeIgnoreCase("%task%").count());
            assertEquals(12, historyService.createHistoricTaskInstanceQuery().taskNameLikeIgnoreCase("%Task%").count());
            assertEquals(12, historyService.createHistoricTaskInstanceQuery().taskNameLikeIgnoreCase("%TASK%").count());
            assertEquals(12, historyService.createHistoricTaskInstanceQuery().taskNameLikeIgnoreCase("%TasK%").count());
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskNameLikeIgnoreCase("gonzo%").count());
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskNameLikeIgnoreCase("%Gonzo%").count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().taskNameLikeIgnoreCase("Task%").count());
        }
    }

    @Test
    public void testQueryByTaskNameOrDescriptionLikeIgnoreCase() {

        // Runtime
        assertEquals(12, taskService.createTaskQuery().or().taskNameLikeIgnoreCase("%task%").taskDescriptionLikeIgnoreCase("%task%").endOr().count());

        assertEquals(9, taskService.createTaskQuery().or().taskNameLikeIgnoreCase("ACCOUN%").taskDescriptionLikeIgnoreCase("%ESCR%").endOr().count());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(12, historyService.createHistoricTaskInstanceQuery().or().taskNameLikeIgnoreCase("%task%").taskDescriptionLikeIgnoreCase("%task%").endOr().count());

            assertEquals(9, historyService.createHistoricTaskInstanceQuery().or().taskNameLikeIgnoreCase("ACCOUN%").taskDescriptionLikeIgnoreCase("%ESCR%").endOr().count());
        }

    }

    @Test
    public void testQueryByTaskDescriptionLikeIgnoreCase() {

        // Runtime
        assertEquals(6, taskService.createTaskQuery().taskDescriptionLikeIgnoreCase("%task%").count());
        assertEquals(6, taskService.createTaskQuery().taskDescriptionLikeIgnoreCase("%Task%").count());
        assertEquals(6, taskService.createTaskQuery().taskDescriptionLikeIgnoreCase("%TASK%").count());
        assertEquals(6, taskService.createTaskQuery().taskDescriptionLikeIgnoreCase("%TaSk%").count());
        assertEquals(0, taskService.createTaskQuery().taskDescriptionLikeIgnoreCase("task%").count());
        assertEquals(1, taskService.createTaskQuery().taskDescriptionLikeIgnoreCase("gonzo%").count());
        assertEquals(1, taskService.createTaskQuery().taskDescriptionLikeIgnoreCase("Gonzo%").count());
        assertEquals(0, taskService.createTaskQuery().taskDescriptionLikeIgnoreCase("%manage%").count());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskDescriptionLikeIgnoreCase("%task%").count());
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskDescriptionLikeIgnoreCase("%Task%").count());
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskDescriptionLikeIgnoreCase("%TASK%").count());
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskDescriptionLikeIgnoreCase("%TaSk%").count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().taskDescriptionLikeIgnoreCase("task%").count());
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskDescriptionLikeIgnoreCase("gonzo%").count());
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskDescriptionLikeIgnoreCase("Gonzo%").count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().taskDescriptionLikeIgnoreCase("%manage%").count());
        }
    }

    @Test
    public void testQueryByAssigneeLikeIgnoreCase() {

        // Runtime
        assertEquals(1, taskService.createTaskQuery().taskAssigneeLikeIgnoreCase("%gonzo%").count());
        assertEquals(1, taskService.createTaskQuery().taskAssigneeLikeIgnoreCase("%GONZO%").count());
        assertEquals(1, taskService.createTaskQuery().taskAssigneeLikeIgnoreCase("%Gon%").count());
        assertEquals(1, taskService.createTaskQuery().taskAssigneeLikeIgnoreCase("gon%").count());
        assertEquals(1, taskService.createTaskQuery().taskAssigneeLikeIgnoreCase("%nzo%").count());
        assertEquals(0, taskService.createTaskQuery().taskAssigneeLikeIgnoreCase("%doesnotexist%").count());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskAssigneeLikeIgnoreCase("%gonzo%").count());
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskAssigneeLikeIgnoreCase("%GONZO%").count());
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskAssigneeLikeIgnoreCase("%Gon%").count());
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskAssigneeLikeIgnoreCase("gon%").count());
            assertEquals(1, historyService.createHistoricTaskInstanceQuery().taskAssigneeLikeIgnoreCase("%nzo%").count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().taskAssigneeLikeIgnoreCase("%doesnotexist%").count());
        }
    }

    @Test
    public void testQueryByOwnerLikeIgnoreCase() {

        // Runtime
        assertEquals(6, taskService.createTaskQuery().taskOwnerLikeIgnoreCase("%gonzo%").count());
        assertEquals(6, taskService.createTaskQuery().taskOwnerLikeIgnoreCase("%GONZO%").count());
        assertEquals(6, taskService.createTaskQuery().taskOwnerLikeIgnoreCase("%Gon%").count());
        assertEquals(6, taskService.createTaskQuery().taskOwnerLikeIgnoreCase("gon%").count());
        assertEquals(6, taskService.createTaskQuery().taskOwnerLikeIgnoreCase("%nzo%").count());
        assertEquals(0, taskService.createTaskQuery().taskOwnerLikeIgnoreCase("%doesnotexist%").count());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskOwnerLikeIgnoreCase("%gonzo%").count());
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskOwnerLikeIgnoreCase("%GONZO%").count());
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskOwnerLikeIgnoreCase("%Gon%").count());
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskOwnerLikeIgnoreCase("gon%").count());
            assertEquals(6, historyService.createHistoricTaskInstanceQuery().taskOwnerLikeIgnoreCase("%nzo%").count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().taskOwnerLikeIgnoreCase("%doesnotexist%").count());
        }
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testQueryByBusinessKeyLikeIgnoreCase() {
        runtimeService.startProcessInstanceByKey("oneTaskProcess", "BUSINESS-KEY-1");
        runtimeService.startProcessInstanceByKey("oneTaskProcess", "Business-Key-2");
        runtimeService.startProcessInstanceByKey("oneTaskProcess", "KeY-3");

        // Runtime
        assertEquals(3, taskService.createTaskQuery().processInstanceBusinessKeyLikeIgnoreCase("%key%").count());
        assertEquals(3, taskService.createTaskQuery().processInstanceBusinessKeyLikeIgnoreCase("%KEY%").count());
        assertEquals(3, taskService.createTaskQuery().processInstanceBusinessKeyLikeIgnoreCase("%EY%").count());
        assertEquals(2, taskService.createTaskQuery().processInstanceBusinessKeyLikeIgnoreCase("%business%").count());
        assertEquals(2, taskService.createTaskQuery().processInstanceBusinessKeyLikeIgnoreCase("business%").count());
        assertEquals(0, taskService.createTaskQuery().processInstanceBusinessKeyLikeIgnoreCase("%doesnotexist%").count());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(3, historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKeyLikeIgnoreCase("%key%").count());
            assertEquals(3, historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKeyLikeIgnoreCase("%KEY%").count());
            assertEquals(3, historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKeyLikeIgnoreCase("%EY%").count());
            assertEquals(2, historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKeyLikeIgnoreCase("%business%").count());
            assertEquals(2, historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKeyLikeIgnoreCase("business%").count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKeyLikeIgnoreCase("%doesnotexist%").count());
        }
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testQueryByProcessDefinitionKeyLikeIgnoreCase() {

        // Runtime
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        runtimeService.startProcessInstanceByKey("oneTaskProcess");

        assertEquals(4, taskService.createTaskQuery().processDefinitionKeyLikeIgnoreCase("%one%").count());
        assertEquals(4, taskService.createTaskQuery().processDefinitionKeyLikeIgnoreCase("%ONE%").count());
        assertEquals(4, taskService.createTaskQuery().processDefinitionKeyLikeIgnoreCase("ON%").count());
        assertEquals(0, taskService.createTaskQuery().processDefinitionKeyLikeIgnoreCase("%fake%").count());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(4, historyService.createHistoricTaskInstanceQuery().processDefinitionKeyLikeIgnoreCase("%one%").count());
            assertEquals(4, historyService.createHistoricTaskInstanceQuery().processDefinitionKeyLikeIgnoreCase("%ONE%").count());
            assertEquals(4, historyService.createHistoricTaskInstanceQuery().processDefinitionKeyLikeIgnoreCase("ON%").count());
            assertEquals(0, historyService.createHistoricTaskInstanceQuery().processDefinitionKeyLikeIgnoreCase("%fake%").count());
        }
    }

    @Test
    public void testCombinationOfOrAndLikeIgnoreCase() {

        // Runtime
        assertEquals(12, taskService.createTaskQuery().or().taskNameLikeIgnoreCase("%task%").taskDescriptionLikeIgnoreCase("%desc%").taskAssigneeLikeIgnoreCase("Gonz%").taskOwnerLike("G%").endOr()
                .count());

        if (HistoryTestHelper.isHistoryLevelAtLeast(HistoryLevel.AUDIT, processEngineConfiguration)) {
            // History
            assertEquals(12, historyService.createHistoricTaskInstanceQuery().or().taskNameLikeIgnoreCase("%task%").taskDescriptionLikeIgnoreCase("%desc%").taskAssigneeLikeIgnoreCase("Gonz%")
                    .taskOwnerLike("G%").endOr().count());
        }
    }

    // Test for https://jira.codehaus.org/browse/ACT-2103
    @Test
    public void testTaskLocalAndProcessInstanceVariableEqualsInOr() {

        deployOneTaskTestProcess();
        for (int i = 0; i < 10; i++) {
            runtimeService.startProcessInstanceByKey("oneTaskProcess");
        }

        List<org.flowable.task.api.Task> allTasks = taskService.createTaskQuery().processDefinitionKey("oneTaskProcess").list();
        assertEquals(10, allTasks.size());

        // Give two tasks a task local variable
        taskService.setVariableLocal(allTasks.get(0).getId(), "localVar", "someValue");
        taskService.setVariableLocal(allTasks.get(1).getId(), "localVar", "someValue");

        // Give three tasks a proc inst var
        runtimeService.setVariable(allTasks.get(2).getProcessInstanceId(), "var", "theValue");
        runtimeService.setVariable(allTasks.get(3).getProcessInstanceId(), "var", "theValue");
        runtimeService.setVariable(allTasks.get(4).getProcessInstanceId(), "var", "theValue");

        assertEquals(2, taskService.createTaskQuery().taskVariableValueEquals("localVar", "someValue").list().size());
        assertEquals(3, taskService.createTaskQuery().processVariableValueEquals("var", "theValue").list().size());

        assertEquals(5, taskService.createTaskQuery().or()
                .taskVariableValueEquals("localVar", "someValue")
                .processVariableValueEquals("var", "theValue")
                .endOr().list().size());

        assertEquals(5, taskService.createTaskQuery()
                .or()
                .taskVariableValueEquals("localVar", "someValue")
                .processVariableValueEquals("var", "theValue")
                .endOr()
                .or()
                .processDefinitionKey("oneTaskProcess")
                .processDefinitionId("notexisting")
                .endOr()
                .list()
                .size());
    }

    @Test
    @Deployment(resources = { "org/flowable/engine/test/api/task/TaskQueryTest.testProcessDefinition.bpmn20.xml" })
    public void testLocalizeTasks() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");

        List<org.flowable.task.api.Task> tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).list();
        assertEquals(1, tasks.size());
        assertEquals("my task", tasks.get(0).getName());
        assertEquals("My Task Description", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("es").list();
        assertEquals(1, tasks.size());
        assertEquals("Mi Tarea", tasks.get(0).getName());
        assertEquals("Mi Tarea Descripci??n", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("it").list();
        assertEquals(1, tasks.size());
        assertEquals("Il mio compito", tasks.get(0).getName());
        assertEquals("Il mio compito Descrizione", tasks.get(0).getDescription());

        ObjectNode infoNode = dynamicBpmnService.getProcessDefinitionInfo(processInstance.getProcessDefinitionId());

        dynamicBpmnService.changeLocalizationName("en-GB", "theTask", "My 'en-GB' localized name", infoNode);
        dynamicBpmnService.changeLocalizationDescription("en-GB", "theTask", "My 'en-GB' localized description", infoNode);
        dynamicBpmnService.saveProcessDefinitionInfo(processInstance.getProcessDefinitionId(), infoNode);

        dynamicBpmnService.changeLocalizationName("en", "theTask", "My 'en' localized name", infoNode);
        dynamicBpmnService.changeLocalizationDescription("en", "theTask", "My 'en' localized description", infoNode);
        dynamicBpmnService.saveProcessDefinitionInfo(processInstance.getProcessDefinitionId(), infoNode);

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).list();
        assertEquals(1, tasks.size());
        assertEquals("my task", tasks.get(0).getName());
        assertEquals("My Task Description", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("es").list();
        assertEquals(1, tasks.size());
        assertEquals("Mi Tarea", tasks.get(0).getName());
        assertEquals("Mi Tarea Descripci??n", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("it").list();
        assertEquals(1, tasks.size());
        assertEquals("Il mio compito", tasks.get(0).getName());
        assertEquals("Il mio compito Descrizione", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("en-GB").list();
        assertEquals(1, tasks.size());
        assertEquals("My 'en-GB' localized name", tasks.get(0).getName());
        assertEquals("My 'en-GB' localized description", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).listPage(0, 10);
        assertEquals(1, tasks.size());
        assertEquals("my task", tasks.get(0).getName());
        assertEquals("My Task Description", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("es").listPage(0, 10);
        assertEquals(1, tasks.size());
        assertEquals("Mi Tarea", tasks.get(0).getName());
        assertEquals("Mi Tarea Descripci??n", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("it").listPage(0, 10);
        assertEquals(1, tasks.size());
        assertEquals("Il mio compito", tasks.get(0).getName());
        assertEquals("Il mio compito Descrizione", tasks.get(0).getDescription());

        tasks = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("en-GB").listPage(0, 10);
        assertEquals(1, tasks.size());
        assertEquals("My 'en-GB' localized name", tasks.get(0).getName());
        assertEquals("My 'en-GB' localized description", tasks.get(0).getDescription());

        org.flowable.task.api.Task task = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        assertEquals("my task", task.getName());
        assertEquals("My Task Description", task.getDescription());

        task = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("es").singleResult();
        assertEquals("Mi Tarea", task.getName());
        assertEquals("Mi Tarea Descripci??n", task.getDescription());

        task = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("it").singleResult();
        assertEquals("Il mio compito", task.getName());
        assertEquals("Il mio compito Descrizione", task.getDescription());

        task = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("en-GB").singleResult();
        assertEquals("My 'en-GB' localized name", task.getName());
        assertEquals("My 'en-GB' localized description", task.getDescription());

        task = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        assertEquals("my task", task.getName());
        assertEquals("My Task Description", task.getDescription());

        task = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("en").singleResult();
        assertEquals("My 'en' localized name", task.getName());
        assertEquals("My 'en' localized description", task.getDescription());

        task = taskService.createTaskQuery().processDefinitionId(processInstance.getProcessDefinitionId()).locale("en-AU").withLocalizationFallback().singleResult();
        assertEquals("My 'en' localized name", task.getName());
        assertEquals("My 'en' localized description", task.getDescription());
    }

    /**
     * Generates some test tasks. - 6 tasks where kermit is a candidate - 1 tasks where gonzo is assignee - 2 tasks assigned to management group - 2 tasks assigned to accountancy group - 1 task
     * assigned to both the management and accountancy group
     */
    private List<String> generateTestTasks() throws Exception {
        List<String> ids = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        // 6 tasks for kermit
        processEngineConfiguration.getClock().setCurrentTime(sdf.parse("01/01/2001 01:01:01.000"));
        for (int i = 0; i < 6; i++) {
            org.flowable.task.api.Task task = taskService.newTask();
            task.setName("testTask");
            task.setDescription("testTask description");
            task.setOwner("gonzo");
            task.setPriority(3);
            taskService.saveTask(task);
            ids.add(task.getId());
            taskService.addCandidateUser(task.getId(), "kermit");
        }

        processEngineConfiguration.getClock().setCurrentTime(sdf.parse("02/02/2002 02:02:02.000"));
        // 1 task for gonzo
        org.flowable.task.api.Task task = taskService.newTask();
        task.setName("gonzoTask");
        task.setDescription("gonzo description");
        task.setPriority(4);
        taskService.saveTask(task);
        taskService.setAssignee(task.getId(), "gonzo");
        taskService.setVariable(task.getId(), "testVar", "someVariable");
        ids.add(task.getId());

        processEngineConfiguration.getClock().setCurrentTime(sdf.parse("03/03/2003 03:03:03.000"));
        // 2 tasks for management group
        for (int i = 0; i < 2; i++) {
            task = taskService.newTask();
            task.setName("managementTask");
            task.setPriority(10);
            taskService.saveTask(task);
            taskService.addCandidateGroup(task.getId(), "management");
            ids.add(task.getId());
        }

        processEngineConfiguration.getClock().setCurrentTime(sdf.parse("04/04/2004 04:04:04.000"));
        // 2 tasks for accountancy group
        for (int i = 0; i < 2; i++) {
            task = taskService.newTask();
            task.setName("accountancyTask");
            task.setDescription("accountancy description");
            taskService.saveTask(task);
            taskService.addCandidateGroup(task.getId(), "accountancy");
            ids.add(task.getId());
        }

        processEngineConfiguration.getClock().setCurrentTime(sdf.parse("05/05/2005 05:05:05.000"));
        // 1 task assigned to management and accountancy group
        task = taskService.newTask();
        task.setName("managementAndAccountancyTask");
        taskService.saveTask(task);
        taskService.addCandidateGroup(task.getId(), "management");
        taskService.addCandidateGroup(task.getId(), "accountancy");
        ids.add(task.getId());

        return ids;
    }

}
