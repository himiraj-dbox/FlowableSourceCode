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

package org.flowable.engine.test.api.identity;

import java.util.Arrays;
import java.util.List;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.FlowableOptimisticLockingException;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.Picture;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.IdmEngines;
import org.flowable.idm.engine.impl.authentication.ApacheDigester;
import org.flowable.idm.engine.impl.authentication.ClearTextPasswordEncoder;
import org.junit.jupiter.api.Test;

/**
 * @author Frederik Heremans
 */
public class IdentityServiceTest extends PluggableFlowableTestCase {

    @Test
    public void testUserInfo() {
        User user = identityService.newUser("testuser");
        identityService.saveUser(user);

        identityService.setUserInfo("testuser", "myinfo", "myvalue");
        assertEquals("myvalue", identityService.getUserInfo("testuser", "myinfo"));

        identityService.setUserInfo("testuser", "myinfo", "myvalue2");
        assertEquals("myvalue2", identityService.getUserInfo("testuser", "myinfo"));

        identityService.deleteUserInfo("testuser", "myinfo");
        assertNull(identityService.getUserInfo("testuser", "myinfo"));

        identityService.deleteUser(user.getId());
    }

    @Test
    public void testCreateExistingUser() {
        User user = identityService.newUser("testuser");
        identityService.saveUser(user);
        try {
            User secondUser = identityService.newUser("testuser");
            identityService.saveUser(secondUser);
            fail("Exception should have been thrown");
        } catch (RuntimeException re) {
            // Expected exception while saving new user with the same name as an
            // existing one.
        }

        identityService.deleteUser(user.getId());
    }

    @Test
    public void testUpdateUser() {
        // First, create a new user
        User user = identityService.newUser("johndoe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@alfresco.com");
        user.setTenantId("originalTenantId");
        identityService.saveUser(user);

        // Fetch and update the user
        user = identityService.createUserQuery().userId("johndoe").singleResult();
        user.setEmail("updated@alfresco.com");
        user.setFirstName("Jane");
        user.setLastName("Donnel");
        user.setTenantId("flowable");
        identityService.saveUser(user);

        user = identityService.createUserQuery().userId("johndoe").singleResult();
        assertEquals("Jane", user.getFirstName());
        assertEquals("Donnel", user.getLastName());
        assertEquals("updated@alfresco.com", user.getEmail());
        assertEquals("flowable", user.getTenantId());

        identityService.deleteUser(user.getId());
    }

    @Test
    public void testCreateUserWithoutTenantId() {
        // First, create a new user
        User user = identityService.newUser("johndoe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@alfresco.com");
        identityService.saveUser(user);

        // Fetch and update the user
        user = identityService.createUserQuery().userId("johndoe").singleResult();
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe@alfresco.com", user.getEmail());
        assertEquals(null, user.getTenantId());

        identityService.deleteUser(user.getId());
    }

    @Test
    public void testUserPicture() {
        // First, create a new user
        User user = identityService.newUser("johndoe");
        identityService.saveUser(user);
        String userId = user.getId();

        Picture picture = new Picture("niceface".getBytes(), "image/string");
        identityService.setUserPicture(userId, picture);

        picture = identityService.getUserPicture(userId);

        // Fetch and update the user
        user = identityService.createUserQuery().userId("johndoe").singleResult();
        assertTrue("byte arrays differ", Arrays.equals("niceface".getBytes(), picture.getBytes()));
        assertEquals("image/string", picture.getMimeType());

        // interface definition states that setting picture to null should delete it
        identityService.setUserPicture(userId, null);
        assertNull("it should be possible to nullify user picture", identityService.getUserPicture(userId));
        user = identityService.createUserQuery().userId("johndoe").singleResult();
        assertNull("it should be possible to delete user picture", identityService.getUserPicture(userId));

        identityService.deleteUser(user.getId());
    }

    @Test
    public void testUpdateGroup() {
        Group group = identityService.newGroup("sales");
        group.setName("Sales");
        identityService.saveGroup(group);

        group = identityService.createGroupQuery().groupId("sales").singleResult();
        group.setName("Updated");
        identityService.saveGroup(group);

        group = identityService.createGroupQuery().groupId("sales").singleResult();
        assertEquals("Updated", group.getName());

        identityService.deleteGroup(group.getId());
    }

    @Test
    public void testFindUserByUnexistingId() {
        User user = identityService.createUserQuery().userId("unexistinguser").singleResult();
        assertNull(user);
    }

    @Test
    public void testFindGroupByUnexistingId() {
        Group group = identityService.createGroupQuery().groupId("unexistinggroup").singleResult();
        assertNull(group);
    }

    @Test
    public void testCreateMembershipUnexistingGroup() {
        User johndoe = identityService.newUser("johndoe");
        identityService.saveUser(johndoe);

        try {
            identityService.createMembership(johndoe.getId(), "unexistinggroup");
            fail("Expected exception");
        } catch (RuntimeException re) {
            // Exception expected
        }

        identityService.deleteUser(johndoe.getId());
    }

    @Test
    public void testCreateMembershipUnexistingUser() {
        Group sales = identityService.newGroup("sales");
        identityService.saveGroup(sales);

        try {
            identityService.createMembership("unexistinguser", sales.getId());
            fail("Expected exception");
        } catch (RuntimeException re) {
            // Exception expected
        }

        identityService.deleteGroup(sales.getId());
    }

    @Test
    public void testCreateMembershipAlreadyExisting() {
        Group sales = identityService.newGroup("sales");
        identityService.saveGroup(sales);
        User johndoe = identityService.newUser("johndoe");
        identityService.saveUser(johndoe);

        // Create the membership
        identityService.createMembership(johndoe.getId(), sales.getId());

        try {
            identityService.createMembership(johndoe.getId(), sales.getId());
        } catch (RuntimeException re) {
            // Expected exception, membership already exists
        }

        identityService.deleteGroup(sales.getId());
        identityService.deleteUser(johndoe.getId());
    }

    @Test
    public void testSaveGroupNullArgument() {
        try {
            identityService.saveGroup(null);
            fail("ActivitiException expected");
        } catch (FlowableIllegalArgumentException ae) {
            assertTextPresent("group is null", ae.getMessage());
        }
    }

    @Test
    public void testSaveUserNullArgument() {
        try {
            identityService.saveUser(null);
            fail("ActivitiException expected");
        } catch (FlowableIllegalArgumentException ae) {
            assertTextPresent("user is null", ae.getMessage());
        }
    }

    @Test
    public void testFindGroupByIdNullArgument() {
        try {
            identityService.createGroupQuery().groupId(null).singleResult();
            fail("ActivitiException expected");
        } catch (FlowableIllegalArgumentException ae) {
            assertTextPresent("id is null", ae.getMessage());
        }
    }

    @Test
    public void testCreateMembershipNullArguments() {
        try {
            identityService.createMembership(null, "group");
            fail("ActivitiException expected");
        } catch (FlowableIllegalArgumentException ae) {
            assertTextPresent("userId is null", ae.getMessage());
        }

        try {
            identityService.createMembership("userId", null);
            fail("ActivitiException expected");
        } catch (FlowableException ae) {
            assertTextPresent("groupId is null", ae.getMessage());
        }
    }

    @Test
    public void testFindGroupsByUserIdNullArguments() {
        try {
            identityService.createGroupQuery().groupMember(null).singleResult();
            fail("ActivitiException expected");
        } catch (FlowableIllegalArgumentException ae) {
            assertTextPresent("userId is null", ae.getMessage());
        }
    }

    @Test
    public void testFindUsersByGroupUnexistingGroup() {
        List<User> users = identityService.createUserQuery().memberOfGroup("unexistinggroup").list();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    public void testDeleteGroupNullArguments() {
        try {
            identityService.deleteGroup(null);
            fail("ActivitiException expected");
        } catch (FlowableIllegalArgumentException ae) {
            assertTextPresent("groupId is null", ae.getMessage());
        }
    }

    @Test
    public void testDeleteMembership() {
        Group sales = identityService.newGroup("sales");
        identityService.saveGroup(sales);

        User johndoe = identityService.newUser("johndoe");
        identityService.saveUser(johndoe);
        // Add membership
        identityService.createMembership(johndoe.getId(), sales.getId());

        List<Group> groups = identityService.createGroupQuery().groupMember(johndoe.getId()).list();
        assertEquals(1, groups.size());
        assertEquals("sales", groups.get(0).getId());

        // Delete the membership and check members of sales group
        identityService.deleteMembership(johndoe.getId(), sales.getId());
        groups = identityService.createGroupQuery().groupMember(johndoe.getId()).list();
        assertTrue(groups.isEmpty());

        identityService.deleteGroup("sales");
        identityService.deleteUser("johndoe");
    }

    @Test
    public void testDeleteMembershipWhenUserIsNoMember() {
        Group sales = identityService.newGroup("sales");
        identityService.saveGroup(sales);

        User johndoe = identityService.newUser("johndoe");
        identityService.saveUser(johndoe);

        // Delete the membership when the user is no member
        identityService.deleteMembership(johndoe.getId(), sales.getId());

        identityService.deleteGroup("sales");
        identityService.deleteUser("johndoe");
    }

    @Test
    public void testDeleteMembershipUnexistingGroup() {
        User johndoe = identityService.newUser("johndoe");
        identityService.saveUser(johndoe);
        // No exception should be thrown when group doesn't exist
        identityService.deleteMembership(johndoe.getId(), "unexistinggroup");
        identityService.deleteUser(johndoe.getId());
    }

    @Test
    public void testDeleteMembershipUnexistingUser() {
        Group sales = identityService.newGroup("sales");
        identityService.saveGroup(sales);
        // No exception should be thrown when user doesn't exist
        identityService.deleteMembership("unexistinguser", sales.getId());
        identityService.deleteGroup(sales.getId());
    }

    @Test
    public void testDeleteMemberschipNullArguments() {
        try {
            identityService.deleteMembership(null, "group");
            fail("ActivitiException expected");
        } catch (FlowableIllegalArgumentException ae) {
            assertTextPresent("userId is null", ae.getMessage());
        }

        try {
            identityService.deleteMembership("user", null);
            fail("ActivitiException expected");
        } catch (FlowableException ae) {
            assertTextPresent("groupId is null", ae.getMessage());
        }
    }

    @Test
    public void testDeleteUserNullArguments() {
        try {
            identityService.deleteUser(null);
            fail("ActivitiException expected");
        } catch (FlowableIllegalArgumentException ae) {
            assertTextPresent("userId is null", ae.getMessage());
        }
    }

    @Test
    public void testDeleteUserUnexistingUserId() {
        // No exception should be thrown. Deleting an unexisting user should
        // be ignored silently
        identityService.deleteUser("unexistinguser");
    }

    @Test
    public void testCheckPasswordNullSafe() {
        assertFalse(identityService.checkPassword("userId", null));
        assertFalse(identityService.checkPassword(null, "passwd"));
        assertFalse(identityService.checkPassword(null, null));
    }

    @Test
    public void testChangePassword() {

        IdmEngineConfiguration idmEngineConfiguration = IdmEngines.getDefaultIdmEngine().getIdmEngineConfiguration();
        idmEngineConfiguration.setPasswordEncoder(new ApacheDigester(ApacheDigester.Digester.MD5));

        try {
            User user = identityService.newUser("johndoe");
            user.setPassword("xxx");
            identityService.saveUser(user);

            user = identityService.createUserQuery().userId("johndoe").list().get(0);
            user.setFirstName("John Doe");
            identityService.saveUser(user);
            User johndoe = identityService.createUserQuery().userId("johndoe").list().get(0);
            assertFalse(johndoe.getPassword().equals("xxx"));
            assertEquals("John Doe", johndoe.getFirstName());
            assertTrue(identityService.checkPassword("johndoe", "xxx"));

            user = identityService.createUserQuery().userId("johndoe").list().get(0);
            user.setPassword("yyy");
            identityService.saveUser(user);
            assertTrue(identityService.checkPassword("johndoe", "xxx"));

            user = identityService.createUserQuery().userId("johndoe").list().get(0);
            user.setPassword("yyy");
            identityService.updateUserPassword(user);
            assertTrue(identityService.checkPassword("johndoe", "yyy"));

            identityService.deleteUser("johndoe");

        } finally {
            idmEngineConfiguration.setPasswordEncoder(ClearTextPasswordEncoder.getInstance());
        }
    }

    @Test
    public void testUserOptimisticLockingException() {
        User user = identityService.newUser("kermit");
        identityService.saveUser(user);

        User user1 = identityService.createUserQuery().singleResult();
        User user2 = identityService.createUserQuery().singleResult();

        user1.setFirstName("name one");
        identityService.saveUser(user1);

        try {

            user2.setFirstName("name two");
            identityService.saveUser(user2);

            fail("Expected an exception");
        } catch (FlowableOptimisticLockingException e) {
            // Expected an exception
        }

        identityService.deleteUser(user.getId());
    }

    @Test
    public void testGroupOptimisticLockingException() {
        Group group = identityService.newGroup("group");
        identityService.saveGroup(group);

        Group group1 = identityService.createGroupQuery().singleResult();
        Group group2 = identityService.createGroupQuery().singleResult();

        group1.setName("name one");
        identityService.saveGroup(group1);

        try {

            group2.setName("name two");
            identityService.saveGroup(group2);

            fail("Expected an exception");
        } catch (FlowableOptimisticLockingException e) {
            // Expected an exception
        }

        identityService.deleteGroup(group.getId());
    }

}
